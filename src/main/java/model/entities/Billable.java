package model.entities;

public class Billable {
    Boolean baseFeeCharge;
    Boolean commFeeCharge;
    Boolean commOutsideFeeCharge;
    Boolean isChargedPerOwner;


    public Boolean getBaseFeeCharge() {
        return baseFeeCharge;
    }

    public void setBaseFeeCharge(Boolean baseFeeCharge) {
        this.baseFeeCharge = baseFeeCharge;
    }

    public Boolean getCommFeeCharge() {
        return commFeeCharge;
    }

    public void setCommFeeCharge(Boolean commFeeCharge) {
        this.commFeeCharge = commFeeCharge;
    }

    public Boolean getCommOutsideFeeCharge() {
        return commOutsideFeeCharge;
    }

    public void setCommOutsideFeeCharge(Boolean commOutsideFeeCharge) {
        this.commOutsideFeeCharge = commOutsideFeeCharge;
    }

    public Boolean getChargedPerOwner() {
        return isChargedPerOwner;
    }

    public void setChargedPerOwner(Boolean chargedPerOwner) {
        isChargedPerOwner = chargedPerOwner;
    }

}
