package compute.filters;

import model.entities.FeeCalculationRequest;
import model.entities.FeeRule;
import model.entities.FeeRuleBase;
import model.entities.FeeRuleComm;
import model.types.AllocationExcludedType;
import model.types.AssetType;
import model.types.CurrencyType;
import model.types.FeeRegulatoryRuleType;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class Filters implements IFilters {

    @Override
    public boolean filterOnCommAccountId(FeeRuleComm feeRuleComm, String accountId) {
        if (feeRuleComm != null && feeRuleComm.getAccountId().equals(accountId)) {
            return true;
        }
        return false;
    }


    @Override
    public boolean filterOnCommAllInExchangeMIC(FeeRuleComm feeRuleComm, String exchangeMIC) {
        if (exchangeMIC != null
                && feeRuleComm != null
                && feeRuleComm.getAllInExchangeMIC() != null
                && feeRuleComm.getAllInExchangeMIC().equals(exchangeMIC)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean filterOnCommTradeTime(FeeRuleComm feeRuleComm, Date tradeTime) {
        if (feeRuleComm != null && feeRuleComm.getDateFrom().before(tradeTime) && feeRuleComm.getDateTo().after(tradeTime)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean filterOnDefaultExchangeMIC(FeeRule feeRule, String defaultExchangeMIC) {
        if (feeRule.getExchangeMIC().equals(defaultExchangeMIC)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean filterOnExchangeMIC(FeeRule feeRule, String defaultExchangeMIC, String exchangeMIC) {
        if ((feeRule.getExchangeMIC() == null) ||
                (feeRule.getExchangeMIC() != null
                        && (feeRule.getExchangeMIC().equals(defaultExchangeMIC)
                        ||
                        feeRule.getExchangeMIC().equals(exchangeMIC)))) {
            return true;
        }

        return false;
    }

    @Override
    public boolean filterOnMarketMIC(FeeRule feeRule, String marketMIC) {
        if (feeRule.getMarketMIC() == null
                ||
                feeRule.getMarketMIC().equals(marketMIC)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean filterOnAssetType(FeeRule feeRule, String assetType) {
        if (feeRule.getAssetType().equals(assetType)) {
            return true;
        }

        return false;
    }


    @Override
    public boolean filterOnExecutionType(FeeRule feeRule, String executionType) {
        if (feeRule.getExecutionType().equals(executionType)) {
            return true;
        }
        return false;
    }


    @Override
    public boolean filterOnPrice(FeeRule feeRule, Double price) {
        if ((feeRule.getPriceStart() == null && feeRule.getPriceEnd() == null)
                ||
                (feeRule.getPriceEnd() == null && feeRule.getPriceStart() <= price)
                ||
                (feeRule.getPriceStart() == null && feeRule.getPriceEnd() > price)
                ||
                (feeRule.getPriceStart() <= price && feeRule.getPriceEnd() > price)
        ) {
            return true;
        }
        return false;
    }


    /**
     * Filter on CCY NAME
     *
     * @param feeRule
     * @param ccyName
     * @return
     */
    @Override
    public boolean filterOnCCYName(FeeRule feeRule, String ccyName) {
        if (feeRule.getCurrencyName() == null || feeRule.getCurrencyName().equals(ccyName)) {
            return true;
        }

        return false;
    }


    @Override
    public boolean filterOnCommissionAllInFeeLevel(FeeRuleComm feeRuleComm,
                                                   String account,
                                                   String exchangeMIC,
                                                   Date tradeTime) {
        if (exchangeMIC != null && feeRuleComm != null && feeRuleComm.getAccountId().equals(account) && feeRuleComm.getAllInExchangeMIC().equals(exchangeMIC)
                && feeRuleComm.getDateFrom().before(tradeTime) && feeRuleComm.getDateTo().after(tradeTime)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean filterOnFeeRulesBaseDate(FeeRuleBase feeRuleBase, Date tradeTime) {
        if (feeRuleBase != null && feeRuleBase.getDateFrom().before(tradeTime) || feeRuleBase.getDateTo().after(tradeTime)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean filterOnIsActive(FeeRule feeRule) {
        if (feeRule.getIsActive() == true) {
            return true;
        }
        return false;
    }

    @Override
    public boolean filterOnInstrumentIsNull(FeeRule feeRule) {
        if (feeRule.getInstrument() == null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean filterOnIsSaleOrBuy(FeeRule feeRule, Integer quantity) {
        if (feeRule.getIsSaleOrBuy() == null ||
                (feeRule.getIsSaleOrBuy() == true && quantity < 0) || (feeRule.getIsSaleOrBuy() == true && quantity > 0)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean filterOnTradeFlags(FeeRule feeRule, String tradeFlags) {
        if ((feeRule.getTradeFlags() == null &&
                (feeRule.getIsAggressor() == null
                        || feeRule.getIsAggressor() == false))
                || (feeRule.getTradeFlags() != null && feeRule.getTradeFlags().equals(tradeFlags))) {
            return true;
        }
        return false;
    }

    @Override
    public boolean filterOnIsCashDesk(FeeRule feeRule, Boolean isCashDesk, String destination) {
        if ((isCashDesk != null
                && isCashDesk == true
                && feeRule.getIsCashDesk() != null
                && feeRule.getIsCashDesk() == true
                && destination != null
                && feeRule.getDestination() != null
                && feeRule.getDestination().equals(destination))
                ||
                (
                        (isCashDesk == null || isCashDesk == false)
                                &&
                                (feeRule.getIsCashDesk() == null || feeRule.getIsCashDesk() == false)
                )

        ) {
            return true;
        }

        return false;
    }

    @Override
    public boolean filterOnIsFeePerExecutionBrokerCode(FeeRule feeRule,
                                                       Boolean isFeePerExecutionBrokerCode,
                                                       String brokerCode) {
        if
        (
                (isFeePerExecutionBrokerCode != null
                        && isFeePerExecutionBrokerCode == true
                        && feeRule.getIsPerExecutionBrokerCode() == true
                        && brokerCode != null
                        && feeRule.getExecutionBrokerCode().equals(brokerCode)
                ) ||
                        (
                                (isFeePerExecutionBrokerCode == null || !isFeePerExecutionBrokerCode == true)
                                        && (feeRule.getIsPerExecutionBrokerCode() == null || !feeRule.getIsPerExecutionBrokerCode() == true)
                        )
        ) {
            return true;
        }

        return false;
    }

    @Override
    public boolean filterOnQuantity(FeeRule feeRule, Integer quantity) {
        if ((feeRule.getMinQuantity() == null && feeRule.getMaxQuantity() == null)
                ||
                (feeRule.getMaxQuantity() == null && (feeRule.getMinQuantity() != null && (feeRule.getMinQuantity() <= Math.abs(quantity)))) ||
                (feeRule.getMinQuantity() == null && (feeRule.getMaxQuantity() != null && (feeRule.getMaxQuantity() > Math.abs(quantity)))) ||
                ((feeRule.getMinQuantity() != null && feeRule.getMaxQuantity() != null) && (feeRule.getMinQuantity() <= Math.abs(quantity) && feeRule.getMaxQuantity() > Math.abs(quantity)))
        ) {
            return true;
        }
        return false;
    }

    @Override
    public boolean filterOnPrincipal(FeeRule feeRule, Double consideration) {
        if ((feeRule.getMaxPrincipal() == null && feeRule.getMinPrincipal() == null)
                ||
                (feeRule.getMaxPrincipal() == null && (feeRule.getMinPrincipal() != null && (feeRule.getMinPrincipal() <= consideration))) ||
                (feeRule.getMinPrincipal() == null && (feeRule.getMaxPrincipal() != null && (feeRule.getMaxPrincipal() > consideration))) ||
                ((feeRule.getMinPrincipal() != null && feeRule.getMaxPrincipal() != null) && (feeRule.getMinPrincipal() <= consideration && feeRule.getMaxPrincipal() > consideration))
        ) {
            return true;
        }
        return false;
    }

    @Override
    public boolean filterOnIsPerExecutingBrokerAccountName(FeeRule feeRule, String executingBrokerAccountName) {
        if ((feeRule.getIsPerExecutingBrokerAccountName() == null)
                ||
                (feeRule.getIsPerExecutingBrokerAccountName() == true && feeRule.getExecutingBrokerAccountName().equals(executingBrokerAccountName))
        ) {
            return true;
        }
        return false;
    }

    @Override
    public boolean filterOnFeeCategory(FeeRule feeRule, String feeCategory, boolean isExchangeRule) {
        if (isExchangeRule) {
            if (feeRule.getFeeCategory().equals(feeCategory)) {
                return true;
            }
        } else {
            if (!feeRule.getFeeCategory().equals(feeCategory)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean filterOnExecutingBrokerName(FeeRule feeRule, String executingBrokerName, String tickerSymbol, String tickerExch) {
        if (feeRule.getExecutingBrokerName() != null) {
            if (feeRule.getExecutingBrokerName().contains(executingBrokerName)) {
                String instr = feeRule.getInstrument();
                if (instr != null) {
                    String root = instr.substring(0, instr.lastIndexOf("."));
                    String exch = instr.substring(instr.lastIndexOf(".") + 1);

                    if (root.equals(tickerSymbol) && exch.equals(tickerExch)) {
                        return true;
                    }

                } else {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * SKIP OPTIONS ON INDEX / OPTIONS ON FUTURES FROM SEC FEE CHARGED
     *
     * @param feeRule
     * @param underlyingType
     * @param assetType
     * @return
     */
    @Override
    public boolean filterOnSkipSEC(FeeRule feeRule, String underlyingType, String assetType) {
        if (underlyingType != null) {
            if ((assetType.equals(AssetType.O.name())
                    && underlyingType.equals(AssetType.F.name())
                    && feeRule.getFeeSubCategory().equals(FeeRegulatoryRuleType.SEC.name())
            ) || (assetType.equals(AssetType.O.name())
                    && underlyingType.equals(AssetType.I.name())
                    && feeRule.getFeeSubCategory().equals(FeeRegulatoryRuleType.SEC.name()))) {
                return false;
            }
        } else {
            return true;
        }
        return true;
    }

    @Override
    public boolean filterOnUnderlyingType(FeeRule feeRule, String underlyingType) {
        if (feeRule.getUnderlyingType() != null) {
            if (underlyingType != null && !feeRule.getUnderlyingType().equals(underlyingType)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isCommissionAllInStatus(List<FeeRuleComm> feeRuleCommList, String account, String exchangeMIC, Date tradeTime) {
        if (exchangeMIC != null) {
            List<FeeRuleComm> data = feeRuleCommList.stream().filter(p -> filterOnCommissionAllInFeeLevel(p, account, exchangeMIC, tradeTime)).collect(Collectors.toList());

            if (data.size() > 0) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean filterOnInstrumentAndExchangeMatch(FeeRule feeRule, String ticker, String exchange) {
        String instrument = feeRule.getInstrument();

        if (instrument != null) {
            String instrumentSymbol = instrument.substring(0, instrument.lastIndexOf("."));
            String instrumentExch = instrument.substring(instrument.lastIndexOf(".") + 1);

            if (instrumentSymbol.equals(ticker) && instrumentExch.equals(exchange)) {
                return true;
            }
        }

        return false;

    }

    /**
     * A set of validators against request
     *
     * @param fcr
     * @return
     */
    @Override
    public boolean isInvalidRequestData(FeeCalculationRequest fcr) {
        // NOT NULL check against primary columns
        if (fcr.getQuantity() == null ||
                fcr.getPrice() == null ||
                fcr.getAssetType() == null ||
                !enumContainsData(AssetType.class, fcr.getAssetType()) ||
                fcr.getFullExecutingBrokerName() == null ||
                fcr.getSymbolCurrency() == null
        ) {
            System.err.println("Preliminary checks failed");

            return true;
        }

        // not allow specific allocation types
        if (fcr.getAllocationType() != null) {
            if (enumContainsData(AllocationExcludedType.class, fcr.getAllocationType())) {
                System.err.println("Allocation type is one of excluded Types. " + fcr.getAllocationType());

                return true;
            }
        }

        // validate the symbol currency is one of excepted
        if (!enumContainsData(CurrencyType.class, fcr.getSymbolCurrency())) {
            System.err.println("Symbol currency is not recognized " + fcr.getSymbolCurrency());

            return true;
        }

        return false;
    }

    public static <E extends Enum<E>> boolean enumContainsData(Class<E> _enumClass,
                                                               String value) {
        try {
            if (value != null && value.equals(AssetType.I.name())) {
                // Indexes are not supported
                return false;
            }
            return EnumSet.allOf(_enumClass)
                    .contains(Enum.valueOf(_enumClass, value));
        } catch (Exception e) {
            return false;
        }
    }
}