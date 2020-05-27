package compute.filters;

import compute.model.FeeCalculationRequest;
import model.entities.FeeRule;
import model.entities.FeeRuleBase;
import model.entities.FeeRuleComm;

import java.util.Date;
import java.util.List;

public interface IFilters {
    boolean filterOnCommAccountId(FeeRuleComm feeRuleComm, String accountId);

    boolean filterOnCommAllInExchangeMIC(FeeRuleComm feeRuleComm, String allInExchangeMIC);

    boolean filterOnCommTradeTime(FeeRuleComm feeRuleComm, Date tradeTime);

    boolean filterOnDefaultExchangeMIC(FeeRule feeRule, String defaultExchangeMIC);

    boolean filterOnExchangeMIC(FeeRule feeRule, String defaultExchangeMIC, String exchangeMIC);

    boolean filterOnMarketMIC(FeeRule feeRule, String marketMIC);

    boolean filterOnAssetName(FeeRule feeRule, String assetName);

    boolean filterOnExecutionType(FeeRule feeRule, String executionType);

    boolean filterOnPrice(FeeRule feeRule, Double price);

    boolean filterOnCCYName(FeeRule feeRule, String ccyName);

    boolean filterOnCommissionAllInFeeLevel(FeeRuleComm feeRuleComm,
                                            String account,
                                            String exchangeMIC,
                                            Date tradeTime);

    boolean filterOnFeeRulesBaseDate(FeeRuleBase feeRuleBase, Date tradeTime);

    boolean filterOnIsActive(FeeRule feeRule);

    boolean filterOnInstrumentIsNull(FeeRule feeRule);

    boolean filterOnIsSaleOrBuy(FeeRule feeRule, Integer quantity);

    boolean filterOnTradeFlags(FeeRule feeRule, String tradeFlags);

    boolean filterOnIsCashDesk(FeeRule feeRule, String isCashDesk, String destination);

    boolean filterOnIsFeePerExecutionBrokerCode(FeeRule feeRule,
                                                String isFeePerExecutionBrokerCode,
                                                String brokerCode);

    boolean filterOnQuantity(FeeRule feeRule, Integer quantity);

    boolean filterOnPrincipal(FeeRule feeRule, Double consideration);

    boolean filterOnIsPerExecutingBrokerAccountName(FeeRule feeRule, String executingBrokerAccountName);

    boolean filterOnFeeCategory(FeeRule feeRule, String feeCategory, boolean isExchangeRule);

    boolean filterOnExecutingBrokerName(FeeRule feeRule, String executingBrokerName, String tickerSymbol, String tickerExch);

    boolean filterOnSkipSEC(FeeRule feeRule, String underlyingType, String assetType);

    boolean filterOnUnderlyingType(FeeRule feeRule, String underlyingType);

    boolean isCommissionAllInStatus(List<FeeRuleComm> feeRuleCommList, String account, String exchangeMIC, Date tradeTime);

    boolean filterOnInstrumentAndExchangeMatch(FeeRule feeRule, String ticker, String exchange);

    boolean isInvalidRequestData(FeeCalculationRequest fcr);
}
