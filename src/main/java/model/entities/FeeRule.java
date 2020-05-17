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
    private Integer isAggressor;
    private String isOddLot;

    private Integer isSaleOrBuy;
    private Integer isAppliedPerExecution;
    private Integer isAppliedPerTicket;
    private Integer isPerExecutingBrokerAccountName;
    private Integer isPerExecutionBrokerCode;
    private String isCashDesk;
    private String isExternal;
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
    private String isRoundedUp;
    private Integer isActive;
    // ownerlist
    private List<AccountSource> ownersList;

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public String getExchangeMIC() {
        return exchangeMIC;
    }

    public void setExchangeMIC(String exchangeMIC) {
        this.exchangeMIC = exchangeMIC;
    }

    public String getExecutingBrokerName() {
        return executingBrokerName;
    }

    public void setExecutingBrokerName(String executingBrokerName) {
        this.executingBrokerName = executingBrokerName;
    }

    public String getExecutingBrokerAccountName() {
        return executingBrokerAccountName;
    }

    public void setExecutingBrokerAccountName(String executingBrokerAccountName) {
        this.executingBrokerAccountName = executingBrokerAccountName;
    }

    public String getExecutionBrokerCode() {
        return executionBrokerCode;
    }

    public void setExecutionBrokerCode(String executionBrokerCode) {
        this.executionBrokerCode = executionBrokerCode;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getBrokerCode() {
        return brokerCode;
    }

    public void setBrokerCode(String brokerCode) {
        this.brokerCode = brokerCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getFeeCurrencyName() {
        return feeCurrencyName;
    }

    public void setFeeCurrencyName(String feeCurrencyName) {
        this.feeCurrencyName = feeCurrencyName;
    }

    public String getExecutionType() {
        return executionType;
    }

    public void setExecutionType(String executionType) {
        this.executionType = executionType;
    }

    public String getFeeCategory() {
        return feeCategory;
    }

    public void setFeeCategory(String feeCategory) {
        this.feeCategory = feeCategory;
    }

    public String getFeeSubCategory() {
        return feeSubCategory;
    }

    public void setFeeSubCategory(String feeSubCategory) {
        this.feeSubCategory = feeSubCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMarketMIC() {
        return marketMIC;
    }

    public void setMarketMIC(String marketMIC) {
        this.marketMIC = marketMIC;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getUnderlyingType() {
        return underlyingType;
    }

    public void setUnderlyingType(String underlyingType) {
        this.underlyingType = underlyingType;
    }

    public Integer getIsAggressor() {
        return isAggressor;
    }

    public void setIsAggressor(Integer isAggressor) {
        this.isAggressor = isAggressor;
    }

    public String getIsOddLot() {
        return isOddLot;
    }

    public void setIsOddLot(String isOddLot) {
        this.isOddLot = isOddLot;
    }

    public Integer getIsSaleOrBuy() {
        return isSaleOrBuy;
    }

    public void setIsSaleOrBuy(Integer isSaleOrBuy) {
        this.isSaleOrBuy = isSaleOrBuy;
    }

    public Integer getIsAppliedPerExecution() {
        return isAppliedPerExecution;
    }

    public void setIsAppliedPerExecution(Integer isAppliedPerExecution) {
        this.isAppliedPerExecution = isAppliedPerExecution;
    }

    public Integer getIsAppliedPerTicket() {
        return isAppliedPerTicket;
    }

    public void setIsAppliedPerTicket(Integer isAppliedPerTicket) {
        this.isAppliedPerTicket = isAppliedPerTicket;
    }

    public Integer getIsPerExecutingBrokerAccountName() {
        return isPerExecutingBrokerAccountName;
    }

    public void setIsPerExecutingBrokerAccountName(Integer isPerExecutingBrokerAccountName) {
        this.isPerExecutingBrokerAccountName = isPerExecutingBrokerAccountName;
    }

    public Integer getIsPerExecutionBrokerCode() {
        return isPerExecutionBrokerCode;
    }

    public void setIsPerExecutionBrokerCode(Integer isPerExecutionBrokerCode) {
        this.isPerExecutionBrokerCode = isPerExecutionBrokerCode;
    }

    public String getIsCashDesk() {
        return isCashDesk;
    }

    public void setIsCashDesk(String isCashDesk) {
        this.isCashDesk = isCashDesk;
    }

    public String getIsExternal() {
        return isExternal;
    }

    public void setIsExternal(String isExternal) {
        this.isExternal = isExternal;
    }

    public String getTradeFlags() {
        return tradeFlags;
    }

    public void setTradeFlags(String tradeFlags) {
        this.tradeFlags = tradeFlags;
    }

    public Double getPriceStart() {
        return priceStart;
    }

    public void setPriceStart(Double priceStart) {
        this.priceStart = priceStart;
    }

    public Double getPriceEnd() {
        return priceEnd;
    }

    public void setPriceEnd(Double priceEnd) {
        this.priceEnd = priceEnd;
    }

    public Double getMinPrincipal() {
        return minPrincipal;
    }

    public void setMinPrincipal(Double minPrincipal) {
        this.minPrincipal = minPrincipal;
    }

    public Double getMaxPrincipal() {
        return maxPrincipal;
    }

    public void setMaxPrincipal(Double maxPrincipal) {
        this.maxPrincipal = maxPrincipal;
    }

    public Integer getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(Integer minQuantity) {
        this.minQuantity = minQuantity;
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public Double getFlatFee() {
        return flatFee;
    }

    public void setFlatFee(Double flatFee) {
        this.flatFee = flatFee;
    }

    public Double getBasisPoints() {
        return basisPoints;
    }

    public void setBasisPoints(Double basisPoints) {
        this.basisPoints = basisPoints;
    }

    public Double getBasisPointsFeeMin() {
        return basisPointsFeeMin;
    }

    public void setBasisPointsFeeMin(Double basisPointsFeeMin) {
        this.basisPointsFeeMin = basisPointsFeeMin;
    }

    public Double getBasisPointsFeeMax() {
        return basisPointsFeeMax;
    }

    public void setBasisPointsFeeMax(Double basisPointsFeeMax) {
        this.basisPointsFeeMax = basisPointsFeeMax;
    }

    public Double getFeePerContract() {
        return feePerContract;
    }

    public void setFeePerContract(Double feePerContract) {
        this.feePerContract = feePerContract;
    }

    public Double getFeePerContractMin() {
        return feePerContractMin;
    }

    public void setFeePerContractMin(Double feePerContractMin) {
        this.feePerContractMin = feePerContractMin;
    }

    public Double getFeePerContractMaxBP() {
        return feePerContractMaxBP;
    }

    public void setFeePerContractMaxBP(Double feePerContractMaxBP) {
        this.feePerContractMaxBP = feePerContractMaxBP;
    }

    public String getIsRoundedUp() {
        return isRoundedUp;
    }

    public void setIsRoundedUp(String isRoundedUp) {
        this.isRoundedUp = isRoundedUp;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public List<AccountSource> getOwnersList() {
        return ownersList;
    }

    public void setOwnersList(List<AccountSource> ownersList) {
        this.ownersList = ownersList;
    }
}
