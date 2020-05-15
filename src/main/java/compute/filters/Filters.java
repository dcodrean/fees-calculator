package compute.filters;

import model.entities.FeeRule;
import model.entities.FeeRuleBase;
import model.entities.FeeRuleComm;

import java.util.Date;

public class Filters {
    /**
     * Filter on exchange MIC
     *
     * @param feeRule
     * @param exchangeMIC
     * @param defaultExchangeMIC
     * @return
     */
    public static boolean filterOnExchangeMIC(FeeRule feeRule, String exchangeMIC, String defaultExchangeMIC) {
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


    public static boolean filterOnExecutionType(FeeRule feeRule, String assetName) {
        if (feeRule.getAssetName().equals(assetName)) {
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
        if (feeRule.getIsSaleOrBuy() == null || (feeRule.getIsSaleOrBuy() == 1 && quantity < 0) || (feeRule.getIsSaleOrBuy() == 0 && quantity > 0)) {
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
}
