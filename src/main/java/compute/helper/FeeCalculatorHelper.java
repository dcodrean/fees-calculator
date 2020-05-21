package compute.helper;

import compute.model.FeeCalculationRequest;
import model.entities.Account;
import model.entities.Billable;
import model.types.TradeSpecType;

public class FeeCalculatorHelper {
    /**
     * Construct HOST ORDER ID
     *
     * @param fcr
     * @param s
     * @return
     */
    public String buildHostOrderId(FeeCalculationRequest fcr, String s) {
        return s + fcr.getAccountId() + "-" + fcr.getTicketId();
    }

    /**
     * Decide on billable flags
     *
     * @param fcr
     * @param account
     */
    public Billable handleBillableFlags(FeeCalculationRequest fcr, Account account) {
        Billable billable = new Billable();

        if (account.getAccountSource() != null && account.getAccountSource().getAssetType().equals(fcr.getAssetType())) {
            billable.setChargedPerOwner(true);
        }
        if (fcr.getTradeSpecType() != null) {
            if (fcr.getTradeSpecType().contains(TradeSpecType.DONE_AWAY.name())) {
                switch (fcr.getIsBillableFlag()) {
                    case "YES":
                        billable = defineBillableCharges(true, true, false);
                        break;
                    case "SPECIFIED":
                        billable = defineBillableCharges(true, false, true);
                        break;
                    case "NO":
                        billable = defineBillableCharges(true, false, false);
                        break;
                }
                fcr.setExchangeMIC(fcr.getShortExecutingBrokerName() + "." + fcr.getMarketMIC());
            } else {
                switch (fcr.getIsBillableFlag()) {
                    case "YES":
                        billable = defineBillableCharges(true, true, false);
                        break;
                    case "SPECIFIED":
                        billable = defineBillableCharges(true, false, false);
                        break;
                    case "NO":
                        billable = defineBillableCharges(false, false, false);
                        break;
                }

                fcr.setExchangeMIC(fcr.getShortExecutingBrokerName() + "." + fcr.getExchangeMIC());
            }

        }
        return billable;
    }

    private Billable defineBillableCharges(Boolean baseFeeCharge, Boolean commFeeCharge, Boolean commOutsideFeeCharge) {
        Billable billable = new Billable();

        billable.setBaseFeeCharge(baseFeeCharge);
        billable.setCommFeeCharge(commFeeCharge);
        billable.setCommOutsideFeeCharge(commOutsideFeeCharge);

        return billable;
    }
}
