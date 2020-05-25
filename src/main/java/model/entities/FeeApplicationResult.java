package model.entities;

public class FeeApplicationResult {
    String orderExecutionId;
    String feeLevel;
    String feeType;
    String feeCategory;
    String currency;
    Double amount;
    Double commRate;

    public String getOrderExecutionId() {
        return orderExecutionId;
    }

    public void setOrderExecutionId(String orderExecutionId) {
        this.orderExecutionId = orderExecutionId;
    }

    public String getFeeLevel() {
        return feeLevel;
    }

    public void setFeeLevel(String feeLevel) {
        this.feeLevel = feeLevel;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getFeeCategory() {
        return feeCategory;
    }

    public void setFeeCategory(String feeCategory) {
        this.feeCategory = feeCategory;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getCommRate() {
        return commRate;
    }

    public void setCommRate(Double commRate) {
        this.commRate = commRate;
    }

}
