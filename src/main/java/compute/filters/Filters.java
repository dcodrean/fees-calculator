package compute.filters;

import model.entities.FeeRule;

public class Filters {
    static boolean filterOnPrice(FeeRule feeRule, Double min, Double max) {
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
    static boolean filterOnExchangeMIC(FeeRule feeRule, String exchangeMIC, String defaultExchangeMIC) {
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
    static boolean filterOnCCYName(FeeRule feeRule, String ccyName) {
        if (feeRule.getCurrencyName() == null || feeRule.getCurrencyName().equals(ccyName)) {
            return true;
        }

        return false;
    }
}
