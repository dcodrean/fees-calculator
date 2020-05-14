package compute.filters;

import model.entities.FeeRule;
import model.entities.FeeRuleComm;

import java.util.Date;

public class Filters {
    public static boolean filterOnPrice(FeeRule feeRule, Double min, Double max) {
        // TODO
        return false;
    }

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

}
