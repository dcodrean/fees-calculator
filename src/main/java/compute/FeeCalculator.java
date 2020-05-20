package compute;

import compute.filters.Filters;
import compute.model.FeeCalculationRequest;
import model.entities.Account;
import model.entities.FeeApplicationResult;
import model.entities.FeeRule;
import model.entities.FeeRuleComm;
import model.types.*;
import providers.IAccountProvider;
import providers.IExternalTempProvider;
import providers.IFeeRulesProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Fee Compute for a specific input
 */
public class FeeCalculator {
    // service providers
    IAccountProvider accountProvider;
    IFeeRulesProvider feeRulesProvider;
    IExternalTempProvider externalTempProvider;

    // host order id
    String hostOrderId;
    // consideration
    Double consideration;
    // define symbol details
    String tickerSymbol;
    String tickerExch;
    String tickerRoot;
    // status charged by owner
    String isChargedPerOwner = "NO";
    // BILLABLE elements
    String baseFeeCharge;
    String commFeeCharge;
    String externalCommFeeCharge;
    // default exchange
    String defaultFeeExchange;
    // ALL-IN commission
    boolean isCommissionAllInFee;
    // Fee calculation variables
    Double amount;
    Double maxBPValue;

    /**
     * Providers
     *
     * @param accountProvider
     * @param feeRulesProvider
     * @param externalTempProvider
     */
    public FeeCalculator(IAccountProvider accountProvider,
                         IFeeRulesProvider feeRulesProvider,
                         IExternalTempProvider externalTempProvider) {
        this.accountProvider = accountProvider;
        this.feeRulesProvider = feeRulesProvider;
        this.externalTempProvider = externalTempProvider;
    }


    /**
     * @param fcr
     * @return
     */
    private List<FeeApplicationResult> getFeePerTrade(FeeCalculationRequest fcr) {
        List<FeeApplicationResult> feeApplicationResults = new ArrayList<>();

        // preliminary check against input
        if (isInvalidRequestData(fcr)) {
            return null;
        }
        // adjust input data to be as expected
        manipulateRequestData(fcr);

        // get account details
        Account account = accountProvider.get(fcr.getAccountId());
        List<FeeRuleComm> feeRuleComms = feeRulesProvider.getFeeRuleComm(fcr.getAccountId());

        // handle billable flags
        handleBillableFlags(fcr, account);

        // check if ALL IN comm status
        isCommissionAllInFee = isCommissionAllInStatus(feeRuleComms, fcr.getAccountId(), fcr.getExchangeMIC(), fcr.getTradeTime());

        // -- FEE BASE ALLOCATION -- //
        if (baseFeeCharge.equals("YES")) {
            feeApplicationResults = computeFeeBaseCharge(fcr, account);
        }

        if (commFeeCharge.equals("YES")) {
            //TODO - do implement
        }

        if (externalCommFeeCharge.equals("YES")) {

        }
        return feeApplicationResults;
    }

    private List<FeeApplicationResult> computeFeeBaseCharge(FeeCalculationRequest fcr,
                                                            Account account) {
        List<FeeApplicationResult> feeApplicationResults = new ArrayList<>();
        // search for NON-exchange rules
        // list of valid rules
        List<FeeRule> feeNonExchangeRules = listOfNonExchangeBaseRules(fcr);

        // compute fee
        return computeBaseNonExchangeFee(fcr, account, feeNonExchangeRules);
    }

    private List<FeeApplicationResult> computeBaseNonExchangeFee(FeeCalculationRequest fcr, Account account, List<FeeRule> feeNonExchangeRules) {
        List<FeeApplicationResult> feeApplicationResults = new ArrayList<>();
        for (FeeRule feeRule : feeNonExchangeRules) {
            amount = 0.0;

            Double amountCurrent = 0.0;
            Double amountBasisCurrent = 0.0;
            Double currentNotExchangeRate = 0.0;

            Double flatFlee = feeRule.getFlatFee();
            Double feePerContract = feeRule.getFeePerContract();
            Double feePerContractMin = feeRule.getFeePerContractMin();
            Double feePerContractMaxBP = feeRule.getFeePerContractMaxBP();
            Double basisPoints = feeRule.getBasisPoints();
            Double basisPointsFeeMax = feeRule.getBasisPointsFeeMax();
            Double basisPointsFeeMin = feeRule.getBasisPointsFeeMin();

            Integer isAppliedPerExecution = feeRule.getIsAppliedPerExecution();
            Integer isAppliedPerTicket = feeRule.getIsAppliedPerTicket();

            if (flatFlee != null) {
                amount += flatFlee;
                currentNotExchangeRate = flatFlee;

            }

            if (feePerContract != null) {
                if (isAppliedPerExecution != null && isAppliedPerExecution == 1) {
                    amountCurrent = feePerContract;
                } else {
                    amountCurrent = feePerContract * Math.abs(fcr.getQuantity());
                }

                boolean foundValue = false;
                if (feePerContractMin != null && amountCurrent < feePerContractMin) {
                    amount += feePerContractMin;

                    feePerContract = feePerContractMin;

                    foundValue = true;
                } else if (feePerContractMaxBP != null) {
                    maxBPValue = feePerContractMaxBP * consideration;

                    if (amountCurrent > maxBPValue) {
                        amount += maxBPValue;
                        foundValue = true;
                    }
                }

                if (foundValue == false) {
                    amount += amountCurrent;
                }
                currentNotExchangeRate = feePerContract;

                if (feeRule.getIsRoundedUp() != null) {
                    // TODO  - set amount as round up - to create method
                }

            }

            if (basisPoints != null) {
                if (isAppliedPerExecution != null && isAppliedPerExecution == 1) {
                    amountBasisCurrent = basisPoints;
                } else {
                    amountBasisCurrent = basisPoints * consideration;
                }
                boolean foundValue = false;

                if (basisPointsFeeMax != null && amountBasisCurrent > basisPointsFeeMax) {
                    amount += basisPointsFeeMax;
                    foundValue = true;
                } else if (basisPointsFeeMin != null && amountBasisCurrent < basisPointsFeeMin) {
                    amount += basisPointsFeeMin;
                    foundValue = true;
                }

                if (foundValue) {
                    amount += amountBasisCurrent;
                }

                currentNotExchangeRate = basisPoints;

                if (feeRule.getIsRoundedUp() != null) {
                    // TODO  - set amount as round up - to create method
                }
            }

            if (isAppliedPerTicket != null && isAppliedPerTicket == 1) {
                String oldHostOrderId = externalTempProvider.get(hostOrderId, fcr.getAccountId(), fcr.getTradeTime()).getHostOrderId();

                // If current Trade is the first trade for this Ticket, apply Rule, otherwise skip.
                if (oldHostOrderId == null) {
                    if (isChargedPerOwner.equals("YES")) {
                        if (feeRule.getOwnersList() != null && feeRule.getOwnersList().contains(account.getAccountSource().getSource())) {
                            if (feeRule.getFeeCurrencyName() != null) {
                                // TODO - create response object
                                FeeApplicationResult feeApplicationResult = new FeeApplicationResult();
                                feeApplicationResults.add(feeApplicationResult);
                                if (isCommissionAllInFee != false) {
                                    // TODO - create response object
                                    FeeApplicationResult feeApplicationResult2 = new FeeApplicationResult();
                                    feeApplicationResults.add(feeApplicationResult2);
                                }
                            }
                        }
                    } else {
                        if (feeRule.getFeeCurrencyName() != null) {
                            // TODO - create response object
                            FeeApplicationResult feeApplicationResult = new FeeApplicationResult();
                            feeApplicationResults.add(feeApplicationResult);
                            if (isCommissionAllInFee != false) {
                                // TODO - create response object
                                FeeApplicationResult feeApplicationResult2 = new FeeApplicationResult();
                                feeApplicationResults.add(feeApplicationResult2);
                            }
                        }
                    }
                }
            }
        }
        return feeApplicationResults;
    }

    private boolean isCommissionAllInStatus(List<FeeRuleComm> feeRuleCommList, String account, String exchangeMIC, Date tradeTime) {

        List<FeeRuleComm> data = feeRuleCommList.stream().filter(p -> Filters.filterOnCommissionAllInFeeLevel(p, account, exchangeMIC, tradeTime)).collect(Collectors.toList());

        if (data.size() > 0) {
            return true;
        }
        return false;
    }

    private void handleBillableFlags(FeeCalculationRequest fcr, Account account) {
        if (account.getAccountSource() != null && account.getAccountSource().getAssetType().equals(fcr.getAssetType())) {
            isChargedPerOwner = "YES";
        }
        if (fcr.getTradeSpecType() != null) {
            if (fcr.getTradeSpecType().contains(TradeSpecType.DONE_AWAY.name())) {
                switch (fcr.getIsBillableFlag()) {
                    case "YES":
                        baseFeeCharge = "YES";
                        commFeeCharge = "YES";
                        externalCommFeeCharge = "NO";
                        break;
                    case "SPECIFIED":
                        baseFeeCharge = "YES";
                        commFeeCharge = "NO";
                        externalCommFeeCharge = "YES";
                        break;
                    case "NO":
                        baseFeeCharge = "YES";
                        commFeeCharge = "NO";
                        externalCommFeeCharge = "NO";
                        break;
                }
                fcr.setExchangeMIC(fcr.getShortExecutingBrokerName() + "." + fcr.getMarketMIC());
            } else {
                switch (fcr.getIsBillableFlag()) {
                    case "YES":
                        baseFeeCharge = "YES";
                        commFeeCharge = "YES";
                        externalCommFeeCharge = "NO";
                        break;
                    case "SPECIFIED":
                        baseFeeCharge = "YES";
                        commFeeCharge = "NO";
                        externalCommFeeCharge = "YES";
                        break;
                    case "NO":
                        baseFeeCharge = "NO";
                        commFeeCharge = "NO";
                        externalCommFeeCharge = "NO";
                        break;
                }

                fcr.setExchangeMIC(fcr.getShortExecutingBrokerName() + "." + fcr.getExchangeMIC());
            }

        }
    }

    /**
     * @param fcr
     * @return
     */
    private List<FeeRule> listOfNonExchangeBaseRules(FeeCalculationRequest fcr) {
        List<FeeRule> feeRules = feeRulesProvider.getAll();

        feeRules.stream()
                .filter(feeRule -> Filters.filterOnExchangeMIC(feeRule, defaultFeeExchange, fcr.getExchangeMIC()))
                .filter(feeRule -> Filters.filterOnCCYName(feeRule, fcr.getSymbolCurrency()))
                .filter(feeRule -> Filters.filterOnFeeRulesBaseDate(feeRulesProvider.getFeeRuleBase(feeRule), fcr.getTradeTime()))
                .filter(feeRule -> Filters.filterOnIsActive(feeRule))
                .filter(feeRule -> Filters.filterOnMarketMIC(feeRule, fcr.getMarketMIC()))
                .filter(feeRule -> Filters.filterOnAssetName(feeRule, AssetNameType.STOCKS.name()))
                .filter(feeRule -> Filters.filterOnExecutionType(feeRule, ExecutionType.Trade.name()))
                .filter(feeRule -> Filters.filterOnPrice(feeRule, fcr.getPrice()))
                .filter(feeRule -> Filters.filterOnIsSaleOrBuy(feeRule, fcr.getQuantity()))
                .filter(feeRule -> Filters.filterOnTradeFlags(feeRule, fcr.getTradeFlags()))
                .filter(feeRule -> Filters.filterOnIsCashDesk(feeRule, fcr.getIsCashDesk(), fcr.getDestination()))
                .filter(feeRule -> Filters.filterOnIsFeePerExecutionBrokerCode(feeRule, fcr.getIsFeePerExecutionBrokerCode(), fcr.getBrokerCode()))
                .filter(feeRule -> Filters.filterOnQuantity(feeRule, fcr.getQuantity()))
                .filter(feeRule -> Filters.filterOnPrincipal(feeRule, consideration))
                .filter(feeRule -> Filters.filterOnIsPerExecutingBrokerAccountName(feeRule, fcr.getExecutingBrokerAccountName()))
                .filter(feeRule -> Filters.filterOnFeeCategory(feeRule, FeeCategoryType.Exchange.name()))
                .filter(feeRule -> Filters.filterOnExecutingBrokerName(feeRule, fcr.getShortExecutingBrokerName(), tickerSymbol, tickerExch))
                .filter(feeRule -> Filters.filterOnSkipSEC(feeRule, fcr.getUnderlyingType(), fcr.getAssetType()))
                .filter(feeRule -> Filters.filterOnUnderlyingType(feeRule, fcr.getUnderlyingType()))
                .collect(Collectors.toList());

        return feeRules;
    }

    /**
     * Manipulate Request Data
     *
     * @param fcr
     */
    private void manipulateRequestData(FeeCalculationRequest fcr) {
        // if TicketId is not null we need to construct the HostOrderId
        // adjust symbol names/root/exch as per asset type
        String ticker = fcr.getTicker();

        switch (fcr.getAssetType()) {
            case "S":
                if (fcr.getTicketId() != null) {
                    hostOrderId = buildHostOrderId(fcr, "E-");
                }

                tickerSymbol = ticker.substring(0, ticker.lastIndexOf("."));
                tickerExch = ticker.substring(ticker.lastIndexOf(".") + 1);

                break;
            case "O":
                if (fcr.getTicketId() != null) {
                    hostOrderId = buildHostOrderId(fcr, "O-");
                }

                if (fcr.getUnderlyingTicker() == null || fcr.getUnderlyingTicker().equals("")) {
                    fcr.setUnderlyingTicker(ticker);
                }

                tickerSymbol = fcr.getUnderlyingTicker().substring(0, ticker.lastIndexOf("."));
                tickerExch = "OCC";

                break;
            case "F":
                if (fcr.getTicketId() != null) {
                    hostOrderId = buildHostOrderId(fcr, "F-");
                }

                tickerSymbol = ticker.substring(0, ticker.lastIndexOf("."));
                tickerRoot = ticker.substring(0, ticker.indexOf("/"));
                tickerExch = ticker.substring(ticker.indexOf('.') + 1);

                break;
        }

        // compute consideration
        consideration = Math.abs(fcr.getQuantity()) *
                fcr.getPrice() *
                (fcr.getContractMultiplier() == null ? 1 : fcr.getContractMultiplier()) *
                (fcr.getCcyMultiplier() == null ? 1.0 : fcr.getCcyMultiplier());

        // adjust currency name and price if GBX
        if (fcr.getSymbolCurrency().equals(CurrencyType.GBX.name())) {
            fcr.setSymbolCurrency(CurrencyType.GBP.name());
            fcr.setPrice(fcr.getPrice() / 100);
        }

        // adjust executing broker name based on trade type
        if (fcr.getTradeSpecType() != null && fcr.getTradeSpecType().equals(TradeSpecType.DONE_AWAY.name())) {
            fcr.setFullExecutingBrokerName("DA_" + fcr.getFullExecutingBrokerName());
        }

        // adjust executing broker name based on trade type
        if (fcr.getIsDropCopy() != null && fcr.getIsDropCopy().equals("YES")) {
            fcr.setFullExecutingBrokerName("DC_" + fcr.getFullExecutingBrokerName());
        }

        // create default fee exchange
        defaultFeeExchange = fcr.getShortExecutingBrokerName() + ".NO_EXCH";
    }

    /**
     * A set of validators against request
     *
     * @param fcr
     * @return
     */
    private boolean isInvalidRequestData(FeeCalculationRequest fcr) {
        // NOT NULL check against primary columns
        if (fcr.getQuantity() == null ||
                fcr.getPrice() == null ||
                fcr.getAssetType() == null ||
                !Arrays.stream(AssetType.values()).anyMatch(AssetType.valueOf(fcr.getAssetType())::equals) ||
                fcr.getFullExecutingBrokerName() == null ||
                fcr.getSymbolCurrency() == null
        ) {
            System.err.println("Preliminary checks failed");

            return true;
        }

        // not allow specific allocation types
        if (fcr.getAllocationType() != null) {
            if (Arrays.stream(AssetType.values()).anyMatch(AllocationExcludedType.valueOf(fcr.getAllocationType())::equals)) {
                System.err.println("Allocation type is one of excluded Types. " + fcr.getAllocationType());

                return true;
            }
        }

        // validate the symbol currency is one of excepted type
        if (!Arrays.stream(CurrencyType.values()).anyMatch(CurrencyType.valueOf(fcr.getSymbolCurrency())::equals)) {
            System.err.println("Symbol currency is not recognized " + fcr.getSymbolCurrency());

            return true;
        }

        return false;
    }

    /**
     * Build Host Order Id
     *
     * @param fcr
     * @param s
     * @return
     */
    private String buildHostOrderId(FeeCalculationRequest fcr, String s) {
        return s + fcr.getAccountId() + "-" + fcr.getTicketId();
    }
}
