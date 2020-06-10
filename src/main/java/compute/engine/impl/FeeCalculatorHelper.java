package compute.engine.impl;

import model.entities.Account;
import model.entities.AccountSourceMappings;
import model.entities.Billable;
import model.entities.FeeCalculationRequest;

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

        // default charged per owner
        billable.setBaseFeeCharge(true);
        billable.setCommFeeCharge(true);
        billable.setCommOutsideFeeCharge(false);
        billable.setIsChargedPerOwner(false);

        // get source-asset mappings if any
        List<AccountSourceMappings> accountSourceMappingsList = account.getAccountSourceMappings();

        if (account.getAccountSourceMappings() != null) {
            for (AccountSourceMappings accountSourceMappings : accountSourceMappingsList) {
                if (accountSourceMappings.getAssetType().equals(fcr.getAssetType())) {
                    billable.setIsChargedPerOwner(true);
                }
            }
        }

        if (fcr.getIsDoneAway()) {
            switch (fcr.getBillableState()) {
                case "YES":
                    billable.setBaseFeeCharge(true);
                    billable.setCommFeeCharge(true);
                    billable.setCommOutsideFeeCharge(false);
                    break;
                case "SPECIFIED":
                    billable.setBaseFeeCharge(true);
                    billable.setCommFeeCharge(false);
                    billable.setCommOutsideFeeCharge(true);
                    break;
                case "NO":
                    billable.setBaseFeeCharge(true);
                    billable.setCommFeeCharge(false);
                    billable.setCommOutsideFeeCharge(false);
                    break;
                default:
                    // default
                    billable.setBaseFeeCharge(true);
                    billable.setCommFeeCharge(true);
                    billable.setCommOutsideFeeCharge(false);
                    break;
            }
            fcr.setExchangeMIC(fcr.getShortExecutingBrokerName() + "." + fcr.getMarketMIC());
        } else {
            switch (fcr.getBillableState()) {
                case "YES":
                    billable.setBaseFeeCharge(true);
                    billable.setCommFeeCharge(true);
                    billable.setCommOutsideFeeCharge(false);
                    break;
                case "SPECIFIED":
                    billable.setBaseFeeCharge(true);
                    billable.setCommFeeCharge(false);
                    billable.setCommOutsideFeeCharge(true);
                    break;
                case "NO":
                    billable.setBaseFeeCharge(false);
                    billable.setCommFeeCharge(false);
                    billable.setCommOutsideFeeCharge(false);
                    break;
                default:
                    // default
                    billable.setBaseFeeCharge(true);
                    billable.setCommFeeCharge(true);
                    billable.setCommOutsideFeeCharge(false);
                    break;
            }

            fcr.setExchangeMIC(fcr.getShortExecutingBrokerName() + "." + fcr.getExchangeMIC());
        }

        return billable;
    }

}
