package model.entities;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FeeCalculationResponse {
    String orderExecutionId;
    String feeLevel;
    String feeType;
    String feeCategory;
    String currency;
    Double amount;
    Double commRate;

}
