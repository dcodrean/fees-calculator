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

    public String getIsAggressor() {
        return isAggressor;
    }

    public void setIsAggressor(String isAggressor) {
        this.isAggressor = isAggressor;
    }

    public String getIsOddLot() {
        return isOddLot;
    }

    public void setIsOddLot(String isOddLot) {
        this.isOddLot = isOddLot;
    }

    public String getIsSaleOrBuy() {
        return isSaleOrBuy;
    }

    public void setIsSaleOrBuy(String isSaleOrBuy) {
        this.isSaleOrBuy = isSaleOrBuy;
    }

    public String getIsAppliedPerExecution() {
        return isAppliedPerExecution;
    }

    public void setIsAppliedPerExecution(String isAppliedPerExecution) {
        this.isAppliedPerExecution = isAppliedPerExecution;
    }

    public String getIsAppliedPerTicket() {
        return isAppliedPerTicket;
    }

    public void setIsAppliedPerTicket(String isAppliedPerTicket) {
        this.isAppliedPerTicket = isAppliedPerTicket;
    }

    public String getIsPerExecutingBrokerAccountName() {
        return isPerExecutingBrokerAccountName;
    }

    public void setIsPerExecutingBrokerAccountName(String isPerExecutingBrokerAccountName) {
        this.isPerExecutingBrokerAccountName = isPerExecutingBrokerAccountName;
    }

    public String getIsPerExecutionBrokerCode() {
        return isPerExecutionBrokerCode;
    }

    public void setIsPerExecutionBrokerCode(String isPerExecutionBrokerCode) {
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

    public String getPriceStart() {
        return priceStart;
    }

    public void setPriceStart(String priceStart) {
        this.priceStart = priceStart;
    }

    public String getPriceEnd() {
        return priceEnd;
    }

    public void setPriceEnd(String priceEnd) {
        this.priceEnd = priceEnd;
    }

    public String getMinPrincipal() {
        return minPrincipal;
    }

    public void setMinPrincipal(String minPrincipal) {
        this.minPrincipal = minPrincipal;
    }

    public String getMaxPrincipal() {
        return maxPrincipal;
    }

    public void setMaxPrincipal(String maxPrincipal) {
        this.maxPrincipal = maxPrincipal;
    }

    public String getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(String minQuantity) {
        this.minQuantity = minQuantity;
    }

    public String getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(String maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public String getFlatFee() {
        return flatFee;
    }

    public void setFlatFee(String flatFee) {
        this.flatFee = flatFee;
    }

    public String getBasisPoints() {
        return basisPoints;
    }

    public void setBasisPoints(String basisPoints) {
        this.basisPoints = basisPoints;
    }

    public String getBasisPointsFeeMin() {
        return basisPointsFeeMin;
    }

    public void setBasisPointsFeeMin(String basisPointsFeeMin) {
        this.basisPointsFeeMin = basisPointsFeeMin;
    }

    public String getBasisPointsFeeMax() {
        return basisPointsFeeMax;
    }

    public void setBasisPointsFeeMax(String basisPointsFeeMax) {
        this.basisPointsFeeMax = basisPointsFeeMax;
    }

    public String getFeePerContract() {
        return feePerContract;
    }

    public void setFeePerContract(String feePerContract) {
        this.feePerContract = feePerContract;
    }

    public String getFeePerContractMin() {
        return feePerContractMin;
    }

    public void setFeePerContractMin(String feePerContractMin) {
        this.feePerContractMin = feePerContractMin;
    }

    public String getFeePerContractMaxBP() {
        return feePerContractMaxBP;
    }

    public void setFeePerContractMaxBP(String feePerContractMaxBP) {
        this.feePerContractMaxBP = feePerContractMaxBP;
    }

    public String getIsRoundedUp() {
        return isRoundedUp;
    }

    public void setIsRoundedUp(String isRoundedUp) {
        this.isRoundedUp = isRoundedUp;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public List<AccountSource> getOwnersList() {
        return ownersList;
    }

    public void setOwnersList(List<AccountSource> ownersList) {
        this.ownersList = ownersList;
    }

}
