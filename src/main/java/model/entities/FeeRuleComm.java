package model.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class FeeRuleComm {
    private Long ruleId;
    private String accountId;
    private Date dateFrom;
    private Date dateTo;
    private String description;
    private String allInExchangeMIC;
}
