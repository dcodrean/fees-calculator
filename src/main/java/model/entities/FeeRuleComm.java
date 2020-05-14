package model.entities;

import java.util.Date;

public class FeeRuleComm {
    private Long ruleId;
    private Account accountId;

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public Account getAccountId() {
        return accountId;
    }

    public void setAccountId(Account accountId) {
        this.accountId = accountId;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAllInExchangeMIC() {
        return allInExchangeMIC;
    }

    public void setAllInExchangeMIC(String allInExchangeMIC) {
        this.allInExchangeMIC = allInExchangeMIC;
    }

    private Date dateFrom;
    private Date dateTo;
    private String description;
    private String allInExchangeMIC;
}
