package providers;

import model.entities.ExternalTemp;

import java.util.Date;

public interface IExternalTempProvider {
    ExternalTemp get(String hostOrderId, String accountId, Date tradeTime);
}
