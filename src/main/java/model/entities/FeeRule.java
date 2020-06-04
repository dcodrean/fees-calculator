package model.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class FeeRule {
    private Long ruleId;

    private String exchangeMIC;
    private String executingBrokerName;
    private String executingBrokerAccountName;
    private String executionBrokerCode;
    private String assetType;
    private String brokerCode;
    private String currencyName;
    private String feeCurrencyName;
    private String executionType;
    private String feeCategory;
    private String feeSubCategory;
    private String feeLevel;
    private String description;
    private String marketMIC;
    private String destination;
    private String instrument;
    private String underlyingType;
    private String tradeFlags;

    private Double priceStart;
    private Double priceEnd;

    private Double minPrincipal;
    private Double maxPrincipal;

    private Integer minQuantity;
    private Integer maxQuantity;

    private Double flatFee;
    private Double basisPoints;
    private Double basisPointsFeeMin;
    private Double basisPointsFeeMax;
    private Double feePerContract;
    private Double feePerContractMin;
    private Double feePerContractMaxBP;

    private Boolean isRoundedUp;
    private Boolean isActive;
    private Boolean isAggressor;
    private Boolean isOddLot;
    private Boolean isSaleOrBuy;
    private Boolean isAppliedPerExecution;
    private Boolean isAppliedPerTicket;
    private Boolean isPerExecutingBrokerAccountName;
    private Boolean isPerExecutionBrokerCode;
    private Boolean isCashDesk;
    private Boolean isExternal;

    // ownerlist
    private List<String> ownersList;
}
