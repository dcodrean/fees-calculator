package providers;

import model.entities.Account;

public class AccountProvider implements IAccountProvider {
    @Override
    public Account get(String name) {
        return new Account();
    }
}
