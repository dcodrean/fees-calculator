package providers;

import model.entities.Account;

public interface IAccountProvider {
    Account get(String accountId);
}
