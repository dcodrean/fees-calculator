package compute;

import compute.model.FeeCalculationRequest;
import model.AllocationExcludedType;
import model.AssetType;
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

    /**
     * @param accountProvider
     * @param feeRulesProvider
     */
    public FeeCalculator(IAccountProvider accountProvider, IFeeRulesProvider feeRulesProvider) {
        this.accountProvider = accountProvider;
        this.feeRulesProvider = feeRulesProvider;
    }


    /**
     * @param feeCalculationRequest
     * @return
     */
    private List<FeeApplicationResult> getFeePerTrade(FeeCalculationRequest feeCalculationRequest) {
        // preliminary check against input
        if (isInvalidRequestData(feeCalculationRequest)) {
            return null;
        }
        // adjust input data to be as expected
        manipulateRequestData(feeCalculationRequest);

        // list of valid rules
        return listOfValidRules(feeCalculationRequest);
    }

    private List<FeeApplicationResult> listOfValidRules(FeeCalculationRequest feeCalculationRequest) {
        List<FeeRule> feeRules = feeRulesProvider.getAll();
        return new ArrayList<>();
    }

    private void manipulateRequestData(FeeCalculationRequest feeCalculationRequest) {
        // if TicketId is not null we need to construct the HostOrderId
        if (feeCalculationRequest.getTicketId() != null) {
            switch (feeCalculationRequest.getAssetType()) {
                case "S":
                    hostOrderId = buildHostOrderId(feeCalculationRequest, "E-");
                    break;
                case "O":
                    hostOrderId = buildHostOrderId(feeCalculationRequest, "O-");
                    break;
                case "F":
                    hostOrderId = buildHostOrderId(feeCalculationRequest, "F-");
                    break;
            }
        }

        // fix contract multiplier and ccy multiplier
        if (feeCalculationRequest.getContractMultiplier() == null) {
            feeCalculationRequest.setContractMultiplier(1);
        }
        if (feeCalculationRequest.getCcyMultiplier() == null) {
            feeCalculationRequest.setCcyMultiplier(1d);
        }
    }

    /**
     * A set of validators against request
     *
     * @param feeCalculationRequest
     * @return
     */
    private boolean isInvalidRequestData(FeeCalculationRequest feeCalculationRequest) {
        // NOT NULL check against primary columns
        if (feeCalculationRequest.getQuantity() == null ||
                feeCalculationRequest.getPrice() == null ||
                feeCalculationRequest.getAssetType() == null ||
                !Arrays.stream(AssetType.values()).anyMatch(AssetType.valueOf(feeCalculationRequest.getAssetType())::equals) ||
                feeCalculationRequest.getExecutingBrokerName() == null ||
                feeCalculationRequest.getSymbolCurrency() == null
        ) {
            System.err.println("Preliminary checks failed");

            return true;
        }

        // not allow specific allocation types
        if (feeCalculationRequest.getAllocationType() != null) {
            if (Arrays.stream(AssetType.values()).anyMatch(AllocationExcludedType.valueOf(feeCalculationRequest.getAllocationType())::equals)) {
                System.err.println("Allocation type is one of excluded Types. ");

                return true;
            }
        }
        return false;
    }

    /**
     * Build Host Order Id
     *
     * @param feeCalculationRequest
     * @param s
     * @return
     */
    private String buildHostOrderId(FeeCalculationRequest feeCalculationRequest, String s) {
        return s + feeCalculationRequest.getAccountId() + "-" + feeCalculationRequest.getTicketId();
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
