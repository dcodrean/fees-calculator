package model.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ExternalTemp {
    String hostOrderId;
    Date tradeTime;
    String accountId;

}
