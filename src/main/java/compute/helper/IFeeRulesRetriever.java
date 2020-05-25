package compute.helper;

import compute.model.FeeCalculationRequest;
import model.entities.FeeRule;
import providers.IFeeRulesProvider;

import java.util.List;

public interface IFeeRulesRetriever {
    List<FeeRule> retrieveCommissionRules(IFeeRulesProvider feeRulesProvider,
                                          FeeCalculationRequest fcr,
                                          Boolean isCommissionAllInFee,
                                          String defaultFeeExchange,
                                          Double consideration,
                                          String tickerSymbol,
                                          String tickerExch);

    List<FeeRule> retrieveNonExchangeBaseRules(IFeeRulesProvider feeRulesProvider,
                                               FeeCalculationRequest fcr,
                                               Double consideration,
                                               boolean isExchangeRule,
                                               String defaultFeeExchange,
                                               String tickerSymbol,
                                               String tickerRoot,
                                               String tickerExch);

    List<FeeRule> retrieveExchangeBaseRules(IFeeRulesProvider feeRulesProvider,
                                            FeeCalculationRequest fcr,
                                            Double consideration,
                                            boolean isExchangeRule,
                                            String defaultFeeExchange,
                                            String tickerSymbol,
                                            String tickerRoot,
                                            String tickerExch);
}
