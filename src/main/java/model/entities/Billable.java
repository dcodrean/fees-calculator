package model.entities;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Billable {
    Boolean baseFeeCharge;
    Boolean commFeeCharge;
    Boolean commOutsideFeeCharge;
    Boolean isChargedPerOwner;

}
