package compute.engine;

import model.entities.FeeCalculationRequest;
import model.entities.Account;
import model.entities.FeeCalculationResponse;
import model.entities.FeeRule;
import providers.IFeeRulesProvider;

import java.util.List;

public interface IFeeComputation {
    List<FeeCalculationResponse> computeFeeCommissionCharge(IFeeRulesProvider feeRulesProvider,
                                                            FeeCalculationRequest fcr,
                                                            Account account,
                                                            Boolean isCommissionAllInFee,
                                                            String defaultFeeExchange,
                                                            Double consideration,
                                                            String tickerSymbol,
                                                            String tickerExch,
                                                            String oldHostOrderId);

    List<FeeCalculationResponse> computeFeeBaseCharge(IFeeRulesProvider feeRulesProvider,
                                                      FeeCalculationRequest fcr,
                                                      Account account,
                                                      String feeLevel,
                                                      Double consideration,
                                                      Boolean isChargedPerOwner,
                                                      Boolean isCommissionAllInFee,
                                                      String defaultFeeExchange,
                                                      String tickerSymbol,
                                                      String tickerRoot,
                                                      String tickerExch,
                                                      String oldHostOrderId);

    List<FeeCalculationResponse> computeFeeOutsideCommCharge(FeeCalculationRequest fcr, Double consideration);

    List<FeeCalculationResponse> computeFees(
            FeeCalculationRequest fcr,
            Account account,
            List<FeeRule> feeRules,
            String feeLevel,
            Double consideration,
            Boolean isChargedPerOwner,
            Boolean isCommissionAllInFee,
            String oldHostOrderId);
}
