package providers;

import model.FeeRuleType;
import model.entities.FeeRule;
import model.entities.FeeRuleBase;
import model.entities.FeeRuleComm;

import java.util.List;

public interface IFeeRulesProvider {
    List<FeeRule> get(Long accountId, FeeRuleType feeRuleType);

    List<FeeRule> getAll();

    FeeRuleBase getFeeRuleBase(FeeRule feeRule);

    List<FeeRuleComm> getFeeRuleComm(String account);
}
