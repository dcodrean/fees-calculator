package compute.model;

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


    // LONG NAME (e.g. FIX BAYOU BROKER
    private String fullExecutingBrokerName;

    // NEW ADDED (e.g. BY
    private String shortExecutingBrokerName;

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


    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getMarketMIC() {
        return marketMIC;
    }

    public void setMarketMIC(String marketMIC) {
        this.marketMIC = marketMIC;
    }

    public String getSymbolCurrency() {
        return symbolCurrency;
    }

    public void setSymbolCurrency(String symbolCurrency) {
        this.symbolCurrency = symbolCurrency;
    }

    public String getUnderlyingType() {
        return underlyingType;
    }

    public void setUnderlyingType(String underlyingType) {
        this.underlyingType = underlyingType;
    }

    public String getUnderlyingTicker() {
        return underlyingTicker;
    }

    public void setUnderlyingTicker(String underlyingTicker) {
        this.underlyingTicker = underlyingTicker;
    }

    public String getOrderExecutionId() {
        return orderExecutionId;
    }

    public void setOrderExecutionId(String orderExecutionId) {
        this.orderExecutionId = orderExecutionId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getExchangeMIC() {
        return exchangeMIC;
    }

    public void setExchangeMIC(String exchangeMIC) {
        this.exchangeMIC = exchangeMIC;
    }


    public String getExecutingBrokerAccountName() {
        return executingBrokerAccountName;
    }

    public void setExecutingBrokerAccountName(String executingBrokerAccountName) {
        this.executingBrokerAccountName = executingBrokerAccountName;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getTradeFlags() {
        return tradeFlags;
    }

    public void setTradeFlags(String tradeFlags) {
        this.tradeFlags = tradeFlags;
    }

    public String getTradeSpecType() {
        return tradeSpecType;
    }

    public void setTradeSpecType(String tradeSpecType) {
        this.tradeSpecType = tradeSpecType;
    }

    public String getAllocationType() {
        return allocationType;
    }

    public void setAllocationType(String allocationType) {
        this.allocationType = allocationType;
    }

    public String getIsCashDesk() {
        return isCashDesk;
    }

    public void setIsCashDesk(String isCashDesk) {
        this.isCashDesk = isCashDesk;
    }

    public Integer getContractMultiplier() {
        return contractMultiplier;
    }

    public void setContractMultiplier(Integer contractMultiplier) {
        this.contractMultiplier = contractMultiplier;
    }

    public Double getCcyMultiplier() {
        return ccyMultiplier;
    }

    public void setCcyMultiplier(Double ccyMultiplier) {
        this.ccyMultiplier = ccyMultiplier;
    }

    public String getIsDropCopy() {
        return isDropCopy;
    }

    public void setIsDropCopy(String isDropCopy) {
        this.isDropCopy = isDropCopy;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getBrokerCode() {
        return brokerCode;
    }

    public void setBrokerCode(String brokerCode) {
        this.brokerCode = brokerCode;
    }

    public String getIsFeePerExecutionBrokerCode() {
        return isFeePerExecutionBrokerCode;
    }

    public void setIsFeePerExecutionBrokerCode(String isFeePerExecutionBrokerCode) {
        this.isFeePerExecutionBrokerCode = isFeePerExecutionBrokerCode;
    }

    public String getIsBillableFlag() {
        return isBillableFlag;
    }

    public void setIsBillableFlag(String isBillableFlag) {
        this.isBillableFlag = isBillableFlag;
    }

    public Double getExternalCommRate() {
        return externalCommRate;
    }

    public void setExternalCommRate(Double externalCommRate) {
        this.externalCommRate = externalCommRate;
    }

    public String getExternalCommType() {
        return externalCommType;
    }

    public void setExternalCommType(String externalCommType) {
        this.externalCommType = externalCommType;
    }

    public String getFullExecutingBrokerName() {
        return fullExecutingBrokerName;
    }

    public void setFullExecutingBrokerName(String fullExecutingBrokerName) {
        this.fullExecutingBrokerName = fullExecutingBrokerName;
    }

    public String getShortExecutingBrokerName() {
        return shortExecutingBrokerName;
    }

    public void setShortExecutingBrokerName(String shortExecutingBrokerName) {
        this.shortExecutingBrokerName = shortExecutingBrokerName;
    }

}
