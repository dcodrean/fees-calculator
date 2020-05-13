package compute;

import model.entities.Account;
import model.entities.FeeApplicationResult;
import model.entities.FeeRule;
import providers.AccountProvider;
import providers.FeeRulesProvider;
import providers.IAccountProvider;
import providers.IFeeRulesProvider;

import java.util.ArrayList;
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
