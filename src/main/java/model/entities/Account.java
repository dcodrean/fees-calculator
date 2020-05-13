package model.entities;

public class Account {
    public AccountSource getAccountSource() {
        return accountSource;
    }

    public void setAccountSource(AccountSource accountSource) {
        this.accountSource = accountSource;
    }

    private AccountSource accountSource;
}
