package providers;

import model.FeeRuleType;
import model.entities.FeeRule;

import java.util.List;

public class FeeRulesProvider implements IFeeRulesProvider {
    @Override
    public List<FeeRule> get(Long accountId, FeeRuleType feeRuleType) {
        return null;
    }

    @Override
    public List<FeeRule> getAll() {
        return null;
    }
}
