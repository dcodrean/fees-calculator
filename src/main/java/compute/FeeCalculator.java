package compute;

import model.entities.Account;
import model.entities.FeeApplicationResult;
import model.entities.FeeRule;
import providers.AccountProvider;
import providers.FeeRulesProvider;

import java.util.List;

/**
 * Fee Compute for a specific input
 */
public class FeeCalculator {
    List<Account> listAccounts;

    /**
     * @param feeCalculationRequest
     * @return
     */
    private List<FeeApplicationResult> getFeePerTrade(FeeCalculationRequest feeCalculationRequest) {

        List<FeeRule> feeRules = new FeeRulesProvider().getAll();
        Account account = new AccountProvider().get(feeCalculationRequest.getAccountId());

        for (FeeRule feeRule : feeRules) {

        }
        return null;
    }


}
