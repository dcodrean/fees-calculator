package compute;

import compute.model.FeeCalculationRequest;
import model.AllocationExcludedType;
import model.AssetType;
import model.CurrencyType;
import model.TradeSpecType;
import model.entities.Account;
import model.entities.FeeApplicationResult;
import model.entities.FeeRule;
import providers.AccountProvider;
import providers.FeeRulesProvider;
import providers.IAccountProvider;
import providers.IFeeRulesProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Fee Compute for a specific input
 */
public class FeeCalculator {

    IAccountProvider accountProvider;
    IFeeRulesProvider feeRulesProvider;

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



    /**
     * @param accountProvider
     * @param feeRulesProvider
     */
    public FeeCalculator(IAccountProvider accountProvider, IFeeRulesProvider feeRulesProvider) {
        this.accountProvider = accountProvider;
        this.feeRulesProvider = feeRulesProvider;
    }


    /**
     * @param fcr
     * @return
     */
    private List<FeeApplicationResult> getFeePerTrade(FeeCalculationRequest fcr) {
        // preliminary check against input
        if (isInvalidRequestData(fcr)) {
            return null;
        }
        // adjust input data to be as expected
        manipulateRequestData(fcr);

        // get account details
        Account account = accountProvider.get(fcr.getAccountId());

        // handle billable flags
        handleBillableFlags(fcr, account);

        // list of valid rules
        return listOfValidRules(fcr);
    }

    private void handleBillableFlags(FeeCalculationRequest fcr, Account account) {
        if (account.getAccountSource() != null && account.getAccountSource().getAssetType().equals(fcr.getAssetType())) {
            isChargedPerOwner = "YES";
        }
        if (fcr.getTradeSpecType() != null) {
            if(fcr.getTradeSpecType().contains(TradeSpecType.DONE_AWAY.name())) {
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
    private List<FeeApplicationResult> listOfValidRules(FeeCalculationRequest fcr) {
        List<FeeRule> feeRules = feeRulesProvider.getAll();
        return new ArrayList<>();
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

    /**
     * TO BE REMOVED
     *
     * @param args
     */
    public static void main(String[] args) {
        IAccountProvider accountProvider = new AccountProvider();
        IFeeRulesProvider feeRulesProvider = new FeeRulesProvider();

        FeeCalculator feeCalculator = new FeeCalculator(accountProvider, feeRulesProvider);
        feeCalculator.getFeePerTrade(new FeeCalculationRequest());
    }

}
