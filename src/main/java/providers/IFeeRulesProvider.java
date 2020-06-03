package providers;

import model.entities.FeeRule;
import model.entities.FeeRuleBase;
import model.entities.FeeRuleComm;
import model.types.FeeRuleType;

import java.util.List;

public interface IFeeRulesProvider {
    List<FeeRule> getByAccountIdAndRuleType(Long accountId, FeeRuleType feeRuleType);

    List<FeeRule> getAll();

    FeeRuleBase getByFeeRule(Long ruleId);

    FeeRuleComm getByRuleIdAndAccount(Long ruleId, String account);

    List<FeeRuleComm> getByAccount(String account);
}
