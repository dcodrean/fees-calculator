package model.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class FeeRuleBase {
    private Long ruleId;
    private Date dateFrom;
    private Date dateTo;
}
