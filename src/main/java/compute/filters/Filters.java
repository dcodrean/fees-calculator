package compute.filters;

import model.entities.FeeRule;
import model.entities.FeeRuleBase;
import model.entities.FeeRuleComm;

import java.util.Date;

public class Filters {

    public static boolean filterOnExchangeMIC(FeeRule feeRule, String defaultExchangeMIC, String exchangeMIC) {
        if (feeRule.getExchangeMIC().equals(defaultExchangeMIC)
                ||
                feeRule.getExchangeMIC().equals(exchangeMIC)) {
            return true;
        }

        return false;
    }

    public static boolean filterOnMarketMIC(FeeRule feeRule, String marketMIC) {
        if (feeRule.getMarketMIC() == null
                ||
                feeRule.getMarketMIC().equals(marketMIC)) {
            return true;
        }

        return false;
    }

    public static boolean filterOnAssetName(FeeRule feeRule, String assetName) {
        if (feeRule.getAssetName().equals(assetName)) {
            return true;
        }

        return false;
    }


    public static boolean filterOnExecutionType(FeeRule feeRule, String executionType) {
        if (feeRule.getExecutionType().equals(executionType)) {
            return true;
        }
        return false;
    }


    public static boolean filterOnPrice(FeeRule feeRule, Double price) {
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
    public static boolean filterOnCCYName(FeeRule feeRule, String ccyName) {
        if (feeRule.getCurrencyName() == null || feeRule.getCurrencyName().equals(ccyName)) {
            return true;
        }

        return false;
    }


    public static boolean filterOnCommissionAllInFeeLevel(FeeRuleComm feeRuleComm,
                                                          String account,
                                                          String exchangeMIC,
                                                          Date tradeTime) {
        if (feeRuleComm.getAccountId().equals(account) && feeRuleComm.getAllInExchangeMIC().equals(exchangeMIC)
                && feeRuleComm.getDateFrom().before(tradeTime) && feeRuleComm.getDateTo().after(tradeTime)) {
            return true;
        }
        return false;
    }

    public static boolean filterOnFeeRulesBaseDate(FeeRuleBase feeRuleBase, Date tradeTime) {
        if (feeRuleBase.getDateFrom().before(tradeTime) || feeRuleBase.getDateTo().after(tradeTime)) {
            return true;
        }
        return false;
    }

    public static boolean filterOnIsActive(FeeRule feeRule) {
        if (feeRule.getIsActive() == 1) {
            return true;
        }
        return false;
    }

    public static boolean filterOnIsSaleOrBuy(FeeRule feeRule, Integer quantity) {
        if (feeRule.getIsSaleOrBuy() == null ||
                (feeRule.getIsSaleOrBuy() == 1 && quantity < 0) || (feeRule.getIsSaleOrBuy() == 0 && quantity > 0)) {
            return true;
        }
        return false;
    }

    public static boolean filterOnTradeFlags(FeeRule feeRule, String tradeFlags) {
        if ((feeRule.getTradeFlags() == null &&
                (feeRule.getIsAggressor() == null
                        || feeRule.getIsAggressor() == 0))
                || (feeRule.getTradeFlags() != null && feeRule.getTradeFlags().equals(tradeFlags))) {
            return true;
        }
        return false;
    }

    public static boolean filterOnIsCashDesk(FeeRule feeRule, String isCashDesk, String destination) {
        if ((isCashDesk != null
                && isCashDesk.equals("YES")
                && feeRule.getIsCashDesk() != null
                && feeRule.getIsCashDesk().equals("YES")
                && destination != null
                && feeRule.getDestination().equals(destination))
                ||
                (
                        (isCashDesk == null || !isCashDesk.equals("YES"))
                                &&
                                (feeRule.getIsCashDesk() == null || !feeRule.getIsCashDesk().equals("YES"))
                )

        ) {
            return true;
        }
        return false;
    }

    public static boolean filterOnIsFeePerExecutionBrokerCode(FeeRule feeRule,
                                                              String isFeePerExecutionBrokerCode,
                                                              String brokerCode) {
        if
        (
                (isFeePerExecutionBrokerCode != null
                        && isFeePerExecutionBrokerCode.equals("YES")
                        && feeRule.getIsPerExecutionBrokerCode().equals("YES")
                        && brokerCode != null
                        && feeRule.getExecutionBrokerCode().equals(brokerCode)
                ) ||
                        (
                                (isFeePerExecutionBrokerCode == null || !isFeePerExecutionBrokerCode.equals("YES"))
                                        && (feeRule.getIsPerExecutionBrokerCode() == null || !feeRule.getIsPerExecutionBrokerCode().equals("YES"))
                        )
        ) {
            return true;
        }

        return false;
    }

    public static boolean filterOnQuantity(FeeRule feeRule, Integer quantity) {
        if ((feeRule.getMinQuantity() == null && feeRule.getMaxQuantity() == null)
                ||
                (feeRule.getMaxQuantity() == null && feeRule.getMinQuantity() <= Math.abs(quantity)) ||
                (feeRule.getMinQuantity() == null && feeRule.getMaxQuantity() > Math.abs(quantity)) ||
                (feeRule.getMinQuantity() <= Math.abs(quantity) && feeRule.getMaxQuantity() > Math.abs(quantity))
        ) {
            return true;
        }
        return false;
    }

    public static boolean filterOnPrincipal(FeeRule feeRule, Double consideration) {
        if ((feeRule.getMinPrincipal() == null && feeRule.getMaxPrincipal() == null)
                ||
                (feeRule.getMaxPrincipal() == null && feeRule.getMinPrincipal() <= consideration) ||
                (feeRule.getMinPrincipal() == null && feeRule.getMaxPrincipal() > consideration) ||
                (feeRule.getMinPrincipal() <= consideration && feeRule.getMaxPrincipal() > consideration)
        ) {
            return true;
        }
        return false;
    }

    public static boolean filterOnIsPerExecutingBrokerAccountName(FeeRule feeRule, String executingBrokerAccountName) {
        if ((feeRule.getIsPerExecutingBrokerAccountName() == null)
                ||
                (feeRule.getIsPerExecutingBrokerAccountName() == 1 && feeRule.getExecutingBrokerAccountName().equals(executingBrokerAccountName))
        ) {
            return true;
        }
        return false;
    }

    public static boolean filterOnFeeCategory(FeeRule feeRule, String feeCategory) {
        if (!feeRule.getFeeCategory().equals(feeCategory)) {
            return true;
        }
        return false;
    }

    public static boolean filterOnFilteredNonExchangeBaseRules(FeeRule feeRule, String executingBrokerName, String tickerSymbol, String tickerExch) {
        if (feeRule.getExecutingBrokerName() != null) {
            if (feeRule.getExecutingBrokerName().contains(executingBrokerName)) {
                String instr = feeRule.getInstrument();
                if (instr != null) {
                    String root = instr.substring(0, instr.lastIndexOf("."));
                    String exch = instr.substring(instr.lastIndexOf(".") + 1);

                    if(root.equals(tickerSymbol) && exch.equals(tickerExch)) {
                        return true;
                    }

                } else {
                    return true;
                }
            }
        }

        return false;
    }
}
