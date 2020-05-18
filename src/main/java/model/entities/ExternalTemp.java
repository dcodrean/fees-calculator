package model.entities;

import java.util.Date;

public class ExternalTemp {
    String hostOrderId;
    Date tradeTime;
    String accountId;

    public String getHostOrderId() {
        return hostOrderId;
    }

    public void setHostOrderId(String hostOrderId) {
        this.hostOrderId = hostOrderId;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
