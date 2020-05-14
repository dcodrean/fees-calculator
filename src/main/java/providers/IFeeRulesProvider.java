package providers;

import model.FeeRuleType;
import model.entities.Account;
import model.entities.FeeRule;
import model.entities.FeeRuleBase;
import model.entities.FeeRuleComm;

import java.util.List;

public interface IFeeRulesProvider {
    List<FeeRule> get(Long accountId, FeeRuleType feeRuleType);

    List<FeeRule> getAll();

    List<FeeRuleBase> getFeeRuleBase(FeeRule feeRule);

    List<FeeRuleComm> getFeeRuleComm(FeeRule feeRule, Account account);
}
