package compute.engine.impl;

import model.entities.Account;
import model.entities.AccountSourceMappings;
import model.entities.Billable;
import model.entities.FeeCalculationRequest;
import model.types.TradeSpecType;

import java.util.List;

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

        // default
        billable = defineBillableCharges(true, true, false);

        // get source-asset mappings if any
        List<AccountSourceMappings> accountSourceMappingsList = account.getAccountSourceMappings();

        if (account.getAccountSourceMappings() != null) {
            for (AccountSourceMappings accountSourceMappings : accountSourceMappingsList) {
                if (accountSourceMappings.getAssetType().equals(fcr.getAssetType())) {
                    billable.setIsChargedPerOwner(true);
                }
            }
        }

        if (fcr.getTradeSpecType() != null) {
            if (fcr.getTradeSpecType().contains(TradeSpecType.DONE_AWAY.name())) {
                switch (fcr.getBillableState()) {
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
                switch (fcr.getBillableState()) {
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

    /**
     * Define billable charges
     *
     * @param baseFeeCharge
     * @param commFeeCharge
     * @param commOutsideFeeCharge
     * @return
     */
    private Billable defineBillableCharges(Boolean baseFeeCharge,
                                           Boolean commFeeCharge,
                                           Boolean commOutsideFeeCharge) {
        Billable billable = new Billable();

        billable.setBaseFeeCharge(baseFeeCharge);
        billable.setCommFeeCharge(commFeeCharge);
        billable.setCommOutsideFeeCharge(commOutsideFeeCharge);

        return billable;
    }
}
