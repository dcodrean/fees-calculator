package providers;

import model.FeeRuleType;
import model.entities.FeeRule;

import java.util.List;

public interface IFeeRulesProvider {
    List<FeeRule> get(Long accountId, FeeRuleType feeRuleType);
}
