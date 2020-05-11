package compute;

import model.entities.Account;
import model.entities.Asset;
import model.entities.FeeApplicationResult;

import java.util.List;

/**
 * Fee Compute for a specific input
 */
/*
 * IAccountsProvider - pt tabelele legate de structura organiz.
 * IFeeRulesProvider - si FeeLevels
 *
 * Instruments & Assets - ISymbologyProvider not needed anymore
 */
public class FeeCalculator {
    List<Account> listAccounts;
    List<Asset> listAssets;

    /**
     * @param feeCalculationRequest
     * @return
     */
    private List<FeeApplicationResult> getFeePerTrade(FeeCalculationRequest feeCalculationRequest) {
        // TODO - add logic
        return null;
    }


}
