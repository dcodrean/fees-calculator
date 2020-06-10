package model.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Input data
 */
@Getter
@Setter
public class FeeCalculationRequest {
    // account info
    private String accountId;

    // asset type
    private String assetType;

    // symbol details
    private String ticker;
    private String marketMIC;
    private String symbolCurrency;
    private String underlyingType;
    private String underlyingTicker;

    // execution details
    private String orderExecutionId;
    private String ticketId;
    private Integer quantity;
    private Double price;
    private String exchangeMIC;

    // LONG NAME (e.g. FIX BAYOU BROKER)
    private String fullExecutingBrokerName;

    // NEW ADDED (e.g. BY)
    private String shortExecutingBrokerName;

    private String executingBrokerAccountName;
    private Date tradeTime;
    private String tradeFlags;
    private String allocationType;
    private Integer contractMultiplier;
    private Double ccyMultiplier;
    private String destination;
    private String brokerCode;

    private Boolean isFeePerExecutionBrokerCode;
    private Boolean isDropCopy;
    private Boolean isDoneAway;
    private Boolean isCashDesk;

    // comm data
    // YES , NO, SPECIFIED - billable flag states
    private String billableState;
    private Double externalCommRate;
    private String externalCommType;
}
