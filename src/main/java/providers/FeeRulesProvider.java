package providers;

import model.FeeRuleType;
import model.entities.FeeRule;
import model.entities.FeeRuleBase;
import model.entities.FeeRuleComm;

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

    @Override
    public List<FeeRuleBase> getFeeRuleBase(FeeRule feeRule) {
        return null;
    }

    @Override
    public List<FeeRuleComm> getFeeRuleComm(String name) {
        return null;
    }


}
