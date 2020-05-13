package compute;

import compute.model.FeeCalculationRequest;
import model.AllocationExcludedType;
import model.AssetType;
import model.CurrencyType;
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
    Double consideration;

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

        // list of valid rules
        return listOfValidRules(fcr);
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
        if (fcr.getTicketId() != null) {
            switch (fcr.getAssetType()) {
                case "S":
                    hostOrderId = buildHostOrderId(fcr, "E-");
                    break;
                case "O":
                    hostOrderId = buildHostOrderId(fcr, "O-");
                    break;
                case "F":
                    hostOrderId = buildHostOrderId(fcr, "F-");
                    break;
            }
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
                fcr.getExecutingBrokerName() == null ||
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
