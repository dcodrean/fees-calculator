package model.entities;

import java.util.List;

public class FeeRule {
    private Long ruleId;
    private String exchangeMIC;
    private String executingBrokerName;
    private String executingBrokerAccountName;
    private String executionBrokerCode;
    private String assetName;
    private String brokerCode;
    private String currencyName;
    private String feeCurrencyName;
    private String executionType;
    private String feeCategory;
    private String feeSubCategory;
    private String description;
    private String marketMIC;
    private String destination;
    private String instrument;
    private String underlyingType;
    private String isAggressor;
    private String isOddLot;
    private String isSaleOrBuy;
    private String isAppliedPerExecution;
    private String isAppliedPerTicket;
    private String isPerExecutingBrokerAccountName;
    private String isPerExecutionBrokerCode;
    private String isCashDesk;
    private String isExternal;
    private String tradeFlags;
    private String priceStart;
    private String priceEnd;
    private String minPrincipal;
    private String maxPrincipal;
    private String minQuantity;
    private String maxQuantity;
    private String flatFee;
    private String basisPoints;
    private String basisPointsFeeMin;
    private String basisPointsFeeMax;
    private String feePerContract;
    private String feePerContractMin;
    private String feePerContractMaxBP;
    private String isRoundedUp;
    private String isActive;
    // ownerlist
    private List<AccountSource> ownersList;
}
