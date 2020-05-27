package compute.engine.impl;

import compute.filters.Filters;
import compute.filters.IFilters;
import compute.engine.IFeeRulesRetriever;
import model.entities.FeeCalculationRequest;
import model.entities.FeeRule;
import model.types.AssetNameType;
import model.types.AssetType;
import model.types.ExecutionType;
import model.types.FeeCategoryType;
import providers.IFeeRulesProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FeeRulesRetriever implements IFeeRulesRetriever {
    IFilters filters = new Filters();

    @Override
    public List<FeeRule> retrieveCommissionRules(IFeeRulesProvider feeRulesProvider,
                                                 FeeCalculationRequest fcr,
                                                 Boolean isCommissionAllInFee,
                                                 String defaultFeeExchange,
                                                 Double consideration,
                                                 String tickerSymbol,
                                                 String tickerExch) {
        List<FeeRule> feeRules = filterCommRules(feeRulesProvider, fcr, isCommissionAllInFee, defaultFeeExchange, consideration, tickerSymbol, tickerExch);

        return feeRules;
    }

    @Override
    public List<FeeRule> retrieveNonExchangeBaseRules(IFeeRulesProvider feeRulesProvider,
                                                      FeeCalculationRequest fcr,
                                                      Double consideration,
                                                      boolean isExchangeRule,
                                                      String defaultFeeExchange,
                                                      String tickerSymbol,
                                                      String tickerRoot,
                                                      String tickerExch) {
        List<FeeRule> feeRules = filterBaseRules(feeRulesProvider, fcr, true, defaultFeeExchange, consideration, tickerSymbol, tickerExch);

        return feeRules;
    }

    @Override
    public List<FeeRule> retrieveExchangeBaseRules(IFeeRulesProvider feeRulesProvider,
                                                   FeeCalculationRequest fcr,
                                                   Double consideration,
                                                   boolean isExchangeRule,
                                                   String defaultFeeExchange,
                                                   String tickerSymbol,
                                                   String tickerRoot,
                                                   String tickerExch) {
        List<FeeRule> feeRules = new ArrayList<>();
        if (fcr.getAssetType().equals(AssetType.S.name()) || fcr.getAssetType().equals(AssetType.O.name())) {
            feeRules = filterBaseRules(feeRulesProvider, fcr, isExchangeRule, defaultFeeExchange, consideration, tickerSymbol, tickerExch);
        }

        if (fcr.getAssetType().equals(AssetType.F.name())) {
            feeRules = filterFutureBaseRules(feeRulesProvider, fcr, isExchangeRule, defaultFeeExchange, tickerRoot, tickerExch);
        }
        return feeRules;
    }


    private List<FeeRule> filterFutureBaseRules(IFeeRulesProvider feeRulesProvider,
                                                FeeCalculationRequest fcr,
                                                boolean isExchangeRule,
                                                String defaultFeeExchange,
                                                String tickerRoot,
                                                String tickerExch) {
        List<FeeRule> feeRules = feeRulesProvider.getAll();

        List<FeeRule> valid = new ArrayList<>();

        feeRules.stream()
                .filter(feeRule -> filters.filterOnExchangeMIC(feeRule, defaultFeeExchange, fcr.getExchangeMIC()))
                .filter(feeRule -> filters.filterOnCCYName(feeRule, fcr.getSymbolCurrency()))
                .filter(feeRule -> filters.filterOnFeeRulesBaseDate(feeRulesProvider.getByFeeRule(feeRule), fcr.getTradeTime()))
                .filter(feeRule -> filters.filterOnMarketMIC(feeRule, fcr.getMarketMIC()))
                .filter(feeRule -> filters.filterOnAssetName(feeRule, AssetNameType.STOCKS.name()))
                .filter(feeRule -> filters.filterOnExecutionType(feeRule, ExecutionType.Trade.name()))
                .filter(feeRule -> filters.filterOnIsActive(feeRule))
                .filter(feeRule -> filters.filterOnPrice(feeRule, fcr.getPrice()))
                .filter(feeRule -> filters.filterOnIsSaleOrBuy(feeRule, fcr.getQuantity()))
                .filter(feeRule -> filters.filterOnTradeFlags(feeRule, fcr.getTradeFlags()))
                .filter(feeRule -> filters.filterOnIsCashDesk(feeRule, fcr.getIsCashDesk(), fcr.getDestination()))
                .filter(feeRule -> filters.filterOnIsFeePerExecutionBrokerCode(feeRule, fcr.getIsFeePerExecutionBrokerCode(), fcr.getBrokerCode()))
                .filter(feeRule -> filters.filterOnIsPerExecutingBrokerAccountName(feeRule, fcr.getExecutingBrokerAccountName()))
                .filter(feeRule -> filters.filterOnFeeCategory(feeRule, FeeCategoryType.Exchange.name(), isExchangeRule))
                .collect(Collectors.toList());

        // add extra checks

        for (FeeRule feeRule : feeRules) {
            if (feeRule.getExecutingBrokerName() != null) {
                if (feeRule.getExecutingBrokerName().contains(fcr.getShortExecutingBrokerName())) {
                    if (feeRule.getInstrument() != null) {
                        String currentRoot = feeRule.getInstrument().substring(0, feeRule.getInstrument().lastIndexOf("."));
                        String currentExch = feeRule.getInstrument().substring(feeRule.getInstrument().lastIndexOf(".") + 1);

                        if (currentExch.equals(tickerExch) && currentRoot.equals(tickerRoot)) {
                            if (feeRule.getMinQuantity() != null) {
                                if (Math.abs(fcr.getQuantity()) >= feeRule.getMinQuantity()) {
                                    if (feeRule.getMaxQuantity() != null) {
                                        if (Math.abs(fcr.getQuantity()) < feeRule.getMaxQuantity()) {
                                            valid.add(feeRule);
                                        }
                                    }
                                }
                            } else {
                                valid.add(feeRule);
                            }
                        }
                    }
                }
            }
        }

        if (valid.size() == 0) {
            // reduce filters and get again the data with less criteria
            valid = feeRules.stream()
                    .filter(feeRule -> filters.filterOnDefaultExchangeMIC(feeRule, defaultFeeExchange))
                    .filter(feeRule -> filters.filterOnCCYName(feeRule, fcr.getSymbolCurrency()))
                    .filter(feeRule -> filters.filterOnFeeRulesBaseDate(feeRulesProvider.getByFeeRule(feeRule), fcr.getTradeTime()))
                    .filter(feeRule -> filters.filterOnMarketMIC(feeRule, fcr.getMarketMIC()))
                    .filter(feeRule -> filters.filterOnAssetName(feeRule, AssetNameType.STOCKS.name()))
                    .filter(feeRule -> filters.filterOnIsActive(feeRule))
                    .filter(feeRule -> filters.filterOnIsCashDesk(feeRule, fcr.getIsCashDesk(), fcr.getDestination()))
                    .filter(feeRule -> filters.filterOnIsFeePerExecutionBrokerCode(feeRule, fcr.getIsFeePerExecutionBrokerCode(), fcr.getBrokerCode()))
                    .filter(feeRule -> filters.filterOnIsPerExecutingBrokerAccountName(feeRule, fcr.getExecutingBrokerAccountName()))
                    .filter(feeRule -> filters.filterOnExecutionType(feeRule, ExecutionType.Trade.name()))
                    .filter(feeRule -> filters.filterOnFeeCategory(feeRule, FeeCategoryType.Exchange.name(), isExchangeRule))
                    .collect(Collectors.toList());
        }

        return valid;
    }

    /**
     * @param fcr
     * @return
     */
    private List<FeeRule> filterBaseRules(IFeeRulesProvider feeRulesProvider,
                                          FeeCalculationRequest fcr,
                                          boolean isExchangeRule,
                                          String defaultFeeExchange,
                                          Double consideration,
                                          String tickerSymbol,
                                          String tickerExch) {
        List<FeeRule> feeRules = feeRulesProvider.getAll();

        feeRules.stream()
                .filter(feeRule -> filters.filterOnExchangeMIC(feeRule, defaultFeeExchange, fcr.getExchangeMIC()))
                .filter(feeRule -> filters.filterOnCCYName(feeRule, fcr.getSymbolCurrency()))
                .filter(feeRule -> filters.filterOnFeeRulesBaseDate(feeRulesProvider.getByFeeRule(feeRule), fcr.getTradeTime()))
                .filter(feeRule -> filters.filterOnIsActive(feeRule))
                .filter(feeRule -> filters.filterOnMarketMIC(feeRule, fcr.getMarketMIC()))
                .filter(feeRule -> filters.filterOnAssetName(feeRule, fcr.getAssetType()))
                .filter(feeRule -> filters.filterOnExecutionType(feeRule, ExecutionType.Trade.name()))
                .filter(feeRule -> filters.filterOnPrice(feeRule, fcr.getPrice()))
                .filter(feeRule -> filters.filterOnIsSaleOrBuy(feeRule, fcr.getQuantity()))
                .filter(feeRule -> filters.filterOnTradeFlags(feeRule, fcr.getTradeFlags()))
                .filter(feeRule -> filters.filterOnIsCashDesk(feeRule, fcr.getIsCashDesk(), fcr.getDestination()))
                .filter(feeRule -> filters.filterOnIsFeePerExecutionBrokerCode(feeRule, fcr.getIsFeePerExecutionBrokerCode(), fcr.getBrokerCode()))
                .filter(feeRule -> filters.filterOnQuantity(feeRule, fcr.getQuantity()))
                .filter(feeRule -> filters.filterOnPrincipal(feeRule, consideration))
                .filter(feeRule -> filters.filterOnIsPerExecutingBrokerAccountName(feeRule, fcr.getExecutingBrokerAccountName()))
                .filter(feeRule -> filters.filterOnFeeCategory(feeRule, FeeCategoryType.Exchange.name(), isExchangeRule))
                .filter(feeRule -> filters.filterOnExecutingBrokerName(feeRule, fcr.getShortExecutingBrokerName(), tickerSymbol, tickerExch))
                .filter(feeRule -> filters.filterOnSkipSEC(feeRule, fcr.getUnderlyingType(), fcr.getAssetType()))
                .filter(feeRule -> filters.filterOnUnderlyingType(feeRule, fcr.getUnderlyingType()))
                .collect(Collectors.toList());

        return feeRules;
    }

    private List<FeeRule> filterCommRules(IFeeRulesProvider feeRulesProvider,
                                          FeeCalculationRequest fcr,
                                          Boolean isCommissionAllInFee,
                                          String defaultFeeExchange,
                                          Double consideration,
                                          String tickerSymbol,
                                          String tickerExch) {
        List<FeeRule> feeRules = feeRulesProvider.getAll();
        List<FeeRule> feeRulesFiltered = new ArrayList<>();

        if (isCommissionAllInFee) {
            feeRulesFiltered = feeRules.stream()
                    .filter(feeRule -> filters.filterOnCommAccountId(feeRulesProvider.getByRuleIdAndAccount(feeRule.getRuleId(), fcr.getAccountId()), fcr.getAccountId()))
                    .filter(feeRule -> filters.filterOnCommAllInExchangeMIC(feeRulesProvider.getByRuleIdAndAccount(feeRule.getRuleId(), fcr.getAccountId()), fcr.getExchangeMIC()))
                    .filter(feeRule -> filters.filterOnCommTradeTime(feeRulesProvider.getByRuleIdAndAccount(feeRule.getRuleId(), fcr.getAccountId()), fcr.getTradeTime()))
                    .filter(feeRule -> filters.filterOnAssetName(feeRule, fcr.getAssetType()))
                    .filter(feeRule -> filters.filterOnIsActive(feeRule))
                    .filter(feeRule -> filters.filterOnExecutionType(feeRule, ExecutionType.Trade.name()))
                    .filter(feeRule -> filters.filterOnPrice(feeRule, fcr.getPrice()))
                    .filter(feeRule -> filters.filterOnIsSaleOrBuy(feeRule, fcr.getQuantity()))
                    .filter(feeRule -> filters.filterOnTradeFlags(feeRule, fcr.getTradeFlags()))
                    .filter(feeRule -> filters.filterOnIsCashDesk(feeRule, fcr.getIsCashDesk(), fcr.getDestination()))
                    .filter(feeRule -> filters.filterOnIsFeePerExecutionBrokerCode(feeRule, fcr.getIsFeePerExecutionBrokerCode(), fcr.getBrokerCode()))
                    .filter(feeRule -> filters.filterOnIsPerExecutingBrokerAccountName(feeRule, fcr.getExecutingBrokerAccountName()))
                    .filter(feeRule -> filters.filterOnPrincipal(feeRule, consideration))
                    .collect(Collectors.toList());
        } else {
            feeRulesFiltered = feeRules.stream()
                    .filter(feeRule -> filters.filterOnCommAccountId(feeRulesProvider.getByRuleIdAndAccount(feeRule.getRuleId(), fcr.getAccountId()), fcr.getAccountId()))
                    .filter(feeRule -> filters.filterOnExchangeMIC(feeRule, defaultFeeExchange, fcr.getExchangeMIC()))
                    .filter(feeRule -> filters.filterOnCCYName(feeRule, fcr.getSymbolCurrency()))
                    .filter(feeRule -> filters.filterOnCommTradeTime(feeRulesProvider.getByRuleIdAndAccount(feeRule.getRuleId(), fcr.getAccountId()), fcr.getTradeTime()))
                    .filter(feeRule -> filters.filterOnMarketMIC(feeRule, fcr.getMarketMIC()))
                    .filter(feeRule -> filters.filterOnAssetName(feeRule, fcr.getAssetType()))
                    .filter(feeRule -> filters.filterOnIsActive(feeRule))
                    .filter(feeRule -> filters.filterOnExecutionType(feeRule, ExecutionType.Trade.name()))
                    .filter(feeRule -> filters.filterOnPrice(feeRule, fcr.getPrice()))
                    .filter(feeRule -> filters.filterOnIsSaleOrBuy(feeRule, fcr.getQuantity()))
                    .filter(feeRule -> filters.filterOnTradeFlags(feeRule, fcr.getTradeFlags()))
                    .filter(feeRule -> filters.filterOnIsCashDesk(feeRule, fcr.getIsCashDesk(), fcr.getDestination()))
                    .filter(feeRule -> filters.filterOnIsFeePerExecutionBrokerCode(feeRule, fcr.getIsFeePerExecutionBrokerCode(), fcr.getBrokerCode()))
                    .filter(feeRule -> filters.filterOnPrincipal(feeRule, consideration))
                    .filter(feeRule -> filters.filterOnIsPerExecutingBrokerAccountName(feeRule, fcr.getExecutingBrokerAccountName()))
                    .filter(feeRule -> filters.filterOnInstrumentAndExchangeMatch(feeRule, tickerSymbol, tickerExch))
                    .collect(Collectors.toList());

            if (feeRulesFiltered.size() == 0) {
                feeRulesFiltered = feeRules.stream()
                        .filter(feeRule -> filters.filterOnCommAccountId(feeRulesProvider.getByRuleIdAndAccount(feeRule.getRuleId(), fcr.getAccountId()), fcr.getAccountId()))
                        .filter(feeRule -> filters.filterOnExchangeMIC(feeRule, defaultFeeExchange, fcr.getExchangeMIC()))
                        .filter(feeRule -> filters.filterOnCCYName(feeRule, fcr.getSymbolCurrency()))
                        .filter(feeRule -> filters.filterOnCommTradeTime(feeRulesProvider.getByRuleIdAndAccount(feeRule.getRuleId(), fcr.getAccountId()), fcr.getTradeTime()))
                        .filter(feeRule -> filters.filterOnMarketMIC(feeRule, fcr.getMarketMIC()))
                        .filter(feeRule -> filters.filterOnAssetName(feeRule, fcr.getAssetType()))
                        .filter(feeRule -> filters.filterOnInstrumentIsNull(feeRule))
                        .filter(feeRule -> filters.filterOnIsActive(feeRule))
                        .filter(feeRule -> filters.filterOnExecutionType(feeRule, ExecutionType.Trade.name()))
                        .filter(feeRule -> filters.filterOnPrice(feeRule, fcr.getPrice()))
                        .filter(feeRule -> filters.filterOnIsSaleOrBuy(feeRule, fcr.getQuantity()))
                        .filter(feeRule -> filters.filterOnTradeFlags(feeRule, fcr.getTradeFlags()))
                        .filter(feeRule -> filters.filterOnIsCashDesk(feeRule, fcr.getIsCashDesk(), fcr.getDestination()))
                        .filter(feeRule -> filters.filterOnIsFeePerExecutionBrokerCode(feeRule, fcr.getIsFeePerExecutionBrokerCode(), fcr.getBrokerCode()))
                        .filter(feeRule -> filters.filterOnIsPerExecutingBrokerAccountName(feeRule, fcr.getExecutingBrokerAccountName()))
                        .collect(Collectors.toList());
            }
        }

        return feeRulesFiltered;
    }
}
