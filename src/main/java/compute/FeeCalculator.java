package compute;

import model.AllocationExcludedType;
import model.AssetType;
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
    List<Account> listAccounts;

    IAccountProvider accountProvider;
    IFeeRulesProvider feeRulesProvider;

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

        // NOT NULL check against primary columns
        if (feeCalculationRequest.getQuantity() == null ||
                feeCalculationRequest.getPrice() == null ||
                feeCalculationRequest.getAssetType() == null ||
                !Arrays.stream(AssetType.values()).anyMatch(AssetType.valueOf(feeCalculationRequest.getAssetType())::equals) ||
                feeCalculationRequest.getExecutingBrokerName() == null ||
                feeCalculationRequest.getSymbolCurrency() == null
        ) {
            System.err.println("Preliminary checks failed");

            return null;
        }

        // not allow specific allocation types
        if(feeCalculationRequest.getAllocationType() != null) {
            if(Arrays.stream(AssetType.values()).anyMatch(AllocationExcludedType.valueOf(feeCalculationRequest.getAssetType())::equals)) {
                System.err.println("Allocation type is one of excluded Types. ");

                return null;
            }
        }

        List<FeeRule> feeRules = feeRulesProvider.getAll();
        Account account = accountProvider.get(feeCalculationRequest.getAccountId());


        for (FeeRule feeRule : feeRules) {

        }

        return new ArrayList<>();
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
