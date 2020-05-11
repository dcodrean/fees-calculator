package compute;

import java.util.Date;

/**
 * Input data
 */
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
    private Double quantity;
    private Double price;
    private String exchangeMIC;
    private String executingBrokerName;
    private String executingBrokerAccountName;
    private Date tradeTime;
    private String tradeFlags;
    private String tradeSpecType;
    private String allocationType;
    private String isCashDesk;
    private Integer contractMultiplier;
    private Double ccyMultiplier;
    private String isDropCopy;
    private String destination;
    private String brokerCode;
    private String isFeePerExecutionBrokerCode;

    // comm data
    private String isBillableFlag;
    private Double externalCommRate;
    private String externalCommType;
}
