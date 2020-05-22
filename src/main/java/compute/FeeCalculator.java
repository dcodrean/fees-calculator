package compute;

import compute.filters.Filters;
import compute.helper.FeeCalculationRequestValidator;
import compute.helper.FeeCalculatorHelper;
import compute.model.FeeCalculationRequest;
import model.entities.*;
import model.types.*;
import providers.IAccountProvider;
import providers.IExternalTempProvider;
import providers.IFeeRulesProvider;

import java.util.ArrayList;
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
    Boolean isChargedPerOwner = false;
    // default exchange
    String defaultFeeExchange;
    // ALL-IN commission
    boolean isCommissionAllInFee = false;
    // Fee calculation variables
    Double amount;
    Double maxBPValue;
    FeeCalculatorHelper fch = new FeeCalculatorHelper();
    FeeCalculationRequestValidator fcrv = new FeeCalculationRequestValidator();

    /**
     * Initialize constructor with providers
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
        if (fcrv.isInvalidRequestData(fcr)) {
            return null;
        }
        // adjust input data to be as expected
        manipulateRequestData(fcr);

        // get account details
        Account account = accountProvider.get(fcr.getAccountId());
        List<FeeRuleComm> feeRuleComms = feeRulesProvider.getByAccount(fcr.getAccountId());

        // handle billable flags
        Billable billable = fch.handleBillableFlags(fcr, account);
        isChargedPerOwner = billable.getChargedPerOwner();

        // check if ALL IN comm status
        isCommissionAllInFee = Filters.isCommissionAllInStatus(feeRuleComms, fcr.getAccountId(), fcr.getExchangeMIC(), fcr.getTradeTime());

        // -- FEE BASE ALLOCATION -- //
        if (billable.getBaseFeeCharge()) {
            feeApplicationResults.addAll(computeFeeBaseCharge(fcr, account));
        }

        if (billable.getCommFeeCharge()) {
            feeApplicationResults.addAll(computeFeeCommissionCharge(fcr, account));
        }

        if (billable.getCommOutsideFeeCharge()) {
            feeApplicationResults.addAll(computeFeeOutsideCommCharge(fcr));
        }

        // save into external temp if we had a special host order id (PER_TICKET)
        if (hostOrderId != null) {
            externalTempProvider.add(hostOrderId, fcr.getAccountId(), fcr.getTradeTime());
        }

        return feeApplicationResults;
    }


    private List<FeeRule> retrieveCommisionRules(FeeCalculationRequest fcr) {
        List<FeeRule> feeRules = filterCommRules(fcr);

        return feeRules;
    }

    private List<FeeRule> retrieveNonExchangeBaseRules(FeeCalculationRequest fcr, boolean isExchangeRule) {
        List<FeeRule> feeRules = filterBaseRules(fcr, true);

        return feeRules;
    }

    private List<FeeRule> retrieveExchangeBaseRules(FeeCalculationRequest fcr, boolean isExchangeRule) {
        List<FeeRule> feeRules = new ArrayList<>();
        if (fcr.getAssetType().equals(AssetType.S.name()) || fcr.getAssetType().equals(AssetType.O.name())) {
            feeRules = filterBaseRules(fcr, isExchangeRule);
        }

        if (fcr.getAssetType().equals(AssetType.F.name())) {
            feeRules = filterFutureBaseRules(fcr, isExchangeRule);
        }
        return feeRules;
    }

    private List<FeeApplicationResult> computeFeeCommissionCharge(FeeCalculationRequest fcr,
                                                                  Account account) {

        List<FeeApplicationResult> feeApplicationResults = new ArrayList<>();

        // STEP 1 - SEARCH COMMISSION RULES
        List<FeeRule> feeRuleList = retrieveCommisionRules(fcr);
        // STEP 2 - compute fee for COMMISSION
        feeApplicationResults.addAll(computeFees(fcr, account, feeRuleList));

        return feeApplicationResults;
    }


    /**
     * Compute Fee Base charges
     * Exchange vs Non-Exchange charges
     *
     * @param fcr
     * @param account
     * @return
     */
    private List<FeeApplicationResult> computeFeeBaseCharge(FeeCalculationRequest fcr,
                                                            Account account) {
        List<FeeApplicationResult> feeApplicationResults = new ArrayList<>();

        // STEP 1 - SEARCH BASE (NON-EXCHANGE RULES)
        List<FeeRule> feeNonExchangeRules = retrieveNonExchangeBaseRules(fcr, false);
        // compute fee for NON-EXCHANGE
        feeApplicationResults.addAll(computeFees(fcr, account, feeNonExchangeRules));

        // STEP 2 - SEARCH BASE (EXCHANGE RULES)
        List<FeeRule> feeExchangeRules = retrieveExchangeBaseRules(fcr, true);
        // compute fee for NON-EXCHANGE
        feeApplicationResults.addAll(computeFees(fcr, account, feeExchangeRules));

        return feeApplicationResults;
    }

    /**
     * Compute fees outside of procedure
     *
     * @param fcr
     * @return
     */
    private List<FeeApplicationResult> computeFeeOutsideCommCharge(FeeCalculationRequest fcr) {
        List<FeeApplicationResult> applicationResults = new ArrayList<>();
        // reset amount
        amount = 0.0;

        if (fcr.getExternalCommType().equals(ExternalCommType.PER_TICKET.name())) {
            amount += fcr.getExternalCommRate();
        } else if (fcr.getExternalCommType().equals(ExternalCommType.PER_UNIT.name())) {
            Double amountOutsideCommCurrent = fcr.getExternalCommRate() * Math.abs(fcr.getQuantity());
            amount += amountOutsideCommCurrent;
        } else if (fcr.getExternalCommType().equals(ExternalCommType.BPS.name())) {
            if (fcr.getExternalCommRate() > 1) {
                fcr.setExternalCommRate(fcr.getExternalCommRate() / 10000);
            }

            Double amountOutsideBasisCommCurrent = fcr.getExternalCommRate() * consideration;

            amount += amountOutsideBasisCommCurrent;
        }

        if (amount != 0) {
            if (fcr.getSymbolCurrency() != null) {
                FeeApplicationResult applicationResult = new FeeApplicationResult();
                // TODO - add value
                applicationResults.add(applicationResult);
            }
        }

        return applicationResults;
    }

    /**
     * Compute fees based on input data
     *
     * @param fcr
     * @param account
     * @param feeRules
     * @return
     */
    private List<FeeApplicationResult> computeFees(FeeCalculationRequest fcr,
                                                   Account account,
                                                   List<FeeRule> feeRules) {
        List<FeeApplicationResult> feeApplicationResults = new ArrayList<>();
        for (FeeRule feeRule : feeRules) {
            amount = 0.0;

            Double amountCurrent = 0.0;
            Double amountBasisCurrent = 0.0;
            Double currentComputeRate = 0.0;

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
                currentComputeRate = flatFlee;

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
                currentComputeRate = feePerContract;

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

                currentComputeRate = basisPoints;

                if (feeRule.getIsRoundedUp() != null) {
                    // TODO  - set amount as round up - to create method
                }
            }

            if (isAppliedPerTicket != null && isAppliedPerTicket == 1) {
                String oldHostOrderId = externalTempProvider.get(hostOrderId, fcr.getAccountId(), fcr.getTradeTime()).getHostOrderId();

                // If current Trade is the first trade for this Ticket, apply Rule, otherwise skip.
                if (oldHostOrderId == null) {
                    if (isChargedPerOwner) {
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


    private List<FeeRule> filterFutureBaseRules(FeeCalculationRequest fcr, boolean isExchangeRule) {
        List<FeeRule> feeRules = feeRulesProvider.getAll();

        List<FeeRule> valid = new ArrayList<>();

        feeRules.stream()
                .filter(feeRule -> Filters.filterOnExchangeMIC(feeRule, defaultFeeExchange, fcr.getExchangeMIC()))
                .filter(feeRule -> Filters.filterOnCCYName(feeRule, fcr.getSymbolCurrency()))
                .filter(feeRule -> Filters.filterOnFeeRulesBaseDate(feeRulesProvider.getByFeeRule(feeRule), fcr.getTradeTime()))
                .filter(feeRule -> Filters.filterOnMarketMIC(feeRule, fcr.getMarketMIC()))
                .filter(feeRule -> Filters.filterOnAssetName(feeRule, AssetNameType.STOCKS.name()))
                .filter(feeRule -> Filters.filterOnExecutionType(feeRule, ExecutionType.Trade.name()))
                .filter(feeRule -> Filters.filterOnIsActive(feeRule))
                .filter(feeRule -> Filters.filterOnPrice(feeRule, fcr.getPrice()))
                .filter(feeRule -> Filters.filterOnIsSaleOrBuy(feeRule, fcr.getQuantity()))
                .filter(feeRule -> Filters.filterOnTradeFlags(feeRule, fcr.getTradeFlags()))
                .filter(feeRule -> Filters.filterOnIsCashDesk(feeRule, fcr.getIsCashDesk(), fcr.getDestination()))
                .filter(feeRule -> Filters.filterOnIsFeePerExecutionBrokerCode(feeRule, fcr.getIsFeePerExecutionBrokerCode(), fcr.getBrokerCode()))
                .filter(feeRule -> Filters.filterOnIsPerExecutingBrokerAccountName(feeRule, fcr.getExecutingBrokerAccountName()))
                .filter(feeRule -> Filters.filterOnFeeCategory(feeRule, FeeCategoryType.Exchange.name(), isExchangeRule))
                .collect(Collectors.toList());

        // add extra checks

        for (FeeRule feeRule : feeRules) {
            if (feeRule.getExecutingBrokerName() != null) {
                if (feeRule.getExecutingBrokerName().contains(fcr.getShortExecutingBrokerName())) {
                    if (feeRule.getInstrument() != null) {
                        String currentRoot = feeRule.getInstrument().substring(0, feeRule.getInstrument().lastIndexOf("."));
                        String currentExch = feeRule.getInstrument().substring(feeRule.getInstrument().lastIndexOf(".") + 1);

                        if (currentExch.equals(tickerExch) && currentRoot.equals(tickerRoot)) {
                            if (feeRule.getMinQuantity() != null) {
                                if (Math.abs(fcr.getQuantity()) >= feeRule.getMinQuantity()) {
                                    if (feeRule.getMaxQuantity() != null) {
                                        if (Math.abs(fcr.getQuantity()) < feeRule.getMaxQuantity()) {
                                            valid.add(feeRule);
                                        }
                                    }
                                }
                            } else {
                                valid.add(feeRule);
                            }
                        }
                    }
                }
            }
        }

        if (valid.size() == 0) {
            // reduce filters and get again the data with less criteria
            valid = feeRules.stream()
                    .filter(feeRule -> Filters.filterOnDefaultExchangeMIC(feeRule, defaultFeeExchange))
                    .filter(feeRule -> Filters.filterOnCCYName(feeRule, fcr.getSymbolCurrency()))
                    .filter(feeRule -> Filters.filterOnFeeRulesBaseDate(feeRulesProvider.getByFeeRule(feeRule), fcr.getTradeTime()))
                    .filter(feeRule -> Filters.filterOnMarketMIC(feeRule, fcr.getMarketMIC()))
                    .filter(feeRule -> Filters.filterOnAssetName(feeRule, AssetNameType.STOCKS.name()))
                    .filter(feeRule -> Filters.filterOnIsActive(feeRule))
                    .filter(feeRule -> Filters.filterOnIsCashDesk(feeRule, fcr.getIsCashDesk(), fcr.getDestination()))
                    .filter(feeRule -> Filters.filterOnIsFeePerExecutionBrokerCode(feeRule, fcr.getIsFeePerExecutionBrokerCode(), fcr.getBrokerCode()))
                    .filter(feeRule -> Filters.filterOnIsPerExecutingBrokerAccountName(feeRule, fcr.getExecutingBrokerAccountName()))
                    .filter(feeRule -> Filters.filterOnExecutionType(feeRule, ExecutionType.Trade.name()))
                    .filter(feeRule -> Filters.filterOnFeeCategory(feeRule, FeeCategoryType.Exchange.name(), isExchangeRule))
                    .collect(Collectors.toList());
        }

        return valid;
    }

    /**
     * @param fcr
     * @return
     */
    private List<FeeRule> filterBaseRules(FeeCalculationRequest fcr, boolean isExchangeRule) {
        List<FeeRule> feeRules = feeRulesProvider.getAll();

        feeRules.stream()
                .filter(feeRule -> Filters.filterOnExchangeMIC(feeRule, defaultFeeExchange, fcr.getExchangeMIC()))
                .filter(feeRule -> Filters.filterOnCCYName(feeRule, fcr.getSymbolCurrency()))
                .filter(feeRule -> Filters.filterOnFeeRulesBaseDate(feeRulesProvider.getByFeeRule(feeRule), fcr.getTradeTime()))
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
                .filter(feeRule -> Filters.filterOnFeeCategory(feeRule, FeeCategoryType.Exchange.name(), isExchangeRule))
                .filter(feeRule -> Filters.filterOnExecutingBrokerName(feeRule, fcr.getShortExecutingBrokerName(), tickerSymbol, tickerExch))
                .filter(feeRule -> Filters.filterOnSkipSEC(feeRule, fcr.getUnderlyingType(), fcr.getAssetType()))
                .filter(feeRule -> Filters.filterOnUnderlyingType(feeRule, fcr.getUnderlyingType()))
                .collect(Collectors.toList());

        return feeRules;
    }

    private List<FeeRule> filterCommRules(FeeCalculationRequest fcr) {
        List<FeeRule> feeRules = feeRulesProvider.getAll();
        List<FeeRule> feeRulesFiltered = new ArrayList<>();

        if (isCommissionAllInFee) {
            feeRulesFiltered = feeRules.stream()
                    .filter(feeRule -> Filters.filterOnCommAccountId(feeRulesProvider.getByRuleIdAndAccount(feeRule.getRuleId(), fcr.getAccountId()), fcr.getAccountId()))
                    .filter(feeRule -> Filters.filterOnCommAllInExchangeMIC(feeRulesProvider.getByRuleIdAndAccount(feeRule.getRuleId(), fcr.getAccountId()), fcr.getExchangeMIC()))
                    .filter(feeRule -> Filters.filterOnCommTradeTime(feeRulesProvider.getByRuleIdAndAccount(feeRule.getRuleId(), fcr.getAccountId()), fcr.getTradeTime()))
                    .filter(feeRule -> Filters.filterOnAssetName(feeRule, fcr.getAssetType()))
                    .filter(feeRule -> Filters.filterOnIsActive(feeRule))
                    .filter(feeRule -> Filters.filterOnExecutionType(feeRule, ExecutionType.Trade.name()))
                    .filter(feeRule -> Filters.filterOnPrice(feeRule, fcr.getPrice()))
                    .filter(feeRule -> Filters.filterOnIsSaleOrBuy(feeRule, fcr.getQuantity()))
                    .filter(feeRule -> Filters.filterOnTradeFlags(feeRule, fcr.getTradeFlags()))
                    .filter(feeRule -> Filters.filterOnIsCashDesk(feeRule, fcr.getIsCashDesk(), fcr.getDestination()))
                    .filter(feeRule -> Filters.filterOnIsFeePerExecutionBrokerCode(feeRule, fcr.getIsFeePerExecutionBrokerCode(), fcr.getBrokerCode()))
                    .filter(feeRule -> Filters.filterOnIsPerExecutingBrokerAccountName(feeRule, fcr.getExecutingBrokerAccountName()))
                    .filter(feeRule -> Filters.filterOnPrincipal(feeRule, consideration))
                    .collect(Collectors.toList());
        } else {
            feeRulesFiltered = feeRules.stream()
                    .filter(feeRule -> Filters.filterOnCommAccountId(feeRulesProvider.getByRuleIdAndAccount(feeRule.getRuleId(), fcr.getAccountId()), fcr.getAccountId()))
                    .filter(feeRule -> Filters.filterOnExchangeMIC(feeRule, defaultFeeExchange, fcr.getExchangeMIC()))
                    .filter(feeRule -> Filters.filterOnCCYName(feeRule, fcr.getSymbolCurrency()))
                    .filter(feeRule -> Filters.filterOnCommTradeTime(feeRulesProvider.getByRuleIdAndAccount(feeRule.getRuleId(), fcr.getAccountId()), fcr.getTradeTime()))
                    .filter(feeRule -> Filters.filterOnMarketMIC(feeRule, fcr.getMarketMIC()))
                    .filter(feeRule -> Filters.filterOnAssetName(feeRule, fcr.getAssetType()))
                    .filter(feeRule -> Filters.filterOnIsActive(feeRule))
                    .filter(feeRule -> Filters.filterOnExecutionType(feeRule, ExecutionType.Trade.name()))
                    .filter(feeRule -> Filters.filterOnPrice(feeRule, fcr.getPrice()))
                    .filter(feeRule -> Filters.filterOnIsSaleOrBuy(feeRule, fcr.getQuantity()))
                    .filter(feeRule -> Filters.filterOnTradeFlags(feeRule, fcr.getTradeFlags()))
                    .filter(feeRule -> Filters.filterOnIsCashDesk(feeRule, fcr.getIsCashDesk(), fcr.getDestination()))
                    .filter(feeRule -> Filters.filterOnIsFeePerExecutionBrokerCode(feeRule, fcr.getIsFeePerExecutionBrokerCode(), fcr.getBrokerCode()))
                    .filter(feeRule -> Filters.filterOnPrincipal(feeRule, consideration))
                    .filter(feeRule -> Filters.filterOnIsPerExecutingBrokerAccountName(feeRule, fcr.getExecutingBrokerAccountName()))
                    .filter(feeRule -> Filters.filterOnInstrumentAndExchangeMatch(feeRule, tickerSymbol, tickerExch))
                    .collect(Collectors.toList());

            if (feeRulesFiltered.size() == 0) {
                feeRulesFiltered = feeRules.stream()
                        .filter(feeRule -> Filters.filterOnCommAccountId(feeRulesProvider.getByRuleIdAndAccount(feeRule.getRuleId(), fcr.getAccountId()), fcr.getAccountId()))
                        .filter(feeRule -> Filters.filterOnExchangeMIC(feeRule, defaultFeeExchange, fcr.getExchangeMIC()))
                        .filter(feeRule -> Filters.filterOnCCYName(feeRule, fcr.getSymbolCurrency()))
                        .filter(feeRule -> Filters.filterOnCommTradeTime(feeRulesProvider.getByRuleIdAndAccount(feeRule.getRuleId(), fcr.getAccountId()), fcr.getTradeTime()))
                        .filter(feeRule -> Filters.filterOnMarketMIC(feeRule, fcr.getMarketMIC()))
                        .filter(feeRule -> Filters.filterOnAssetName(feeRule, fcr.getAssetType()))
                        .filter(feeRule -> Filters.filterOnInstrumentIsNull(feeRule))
                        .filter(feeRule -> Filters.filterOnIsActive(feeRule))
                        .filter(feeRule -> Filters.filterOnExecutionType(feeRule, ExecutionType.Trade.name()))
                        .filter(feeRule -> Filters.filterOnPrice(feeRule, fcr.getPrice()))
                        .filter(feeRule -> Filters.filterOnIsSaleOrBuy(feeRule, fcr.getQuantity()))
                        .filter(feeRule -> Filters.filterOnTradeFlags(feeRule, fcr.getTradeFlags()))
                        .filter(feeRule -> Filters.filterOnIsCashDesk(feeRule, fcr.getIsCashDesk(), fcr.getDestination()))
                        .filter(feeRule -> Filters.filterOnIsFeePerExecutionBrokerCode(feeRule, fcr.getIsFeePerExecutionBrokerCode(), fcr.getBrokerCode()))
                        .filter(feeRule -> Filters.filterOnIsPerExecutingBrokerAccountName(feeRule, fcr.getExecutingBrokerAccountName()))
                        .collect(Collectors.toList());
            }
        }

        return feeRulesFiltered;
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
                    hostOrderId = fch.buildHostOrderId(fcr, "E-");
                }

                tickerSymbol = ticker.substring(0, ticker.lastIndexOf("."));
                tickerExch = ticker.substring(ticker.lastIndexOf(".") + 1);

                break;
            case "O":
                if (fcr.getTicketId() != null) {
                    hostOrderId = fch.buildHostOrderId(fcr, "O-");
                }

                if (fcr.getUnderlyingTicker() == null || fcr.getUnderlyingTicker().equals("")) {
                    fcr.setUnderlyingTicker(ticker);
                }

                tickerSymbol = fcr.getUnderlyingTicker().substring(0, ticker.lastIndexOf("."));
                tickerExch = "OCC";

                break;
            case "F":
                if (fcr.getTicketId() != null) {
                    hostOrderId = fch.buildHostOrderId(fcr, "F-");
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
}
