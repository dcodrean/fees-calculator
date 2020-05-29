package compute;

import model.entities.*;
import model.types.FeeRuleType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import providers.IAccountProvider;
import providers.IExternalTempProvider;
import providers.IFeeRulesProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FeeCalculatorTest {

    @Mock
    private IAccountProvider mockAccountProvider;
    @Mock
    private IFeeRulesProvider mockFeeRulesProvider;
    @Mock
    private IExternalTempProvider mockExternalTempProvider;

    private FeeCalculator feeCalculatorUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        feeCalculatorUnderTest = new FeeCalculator(mockAccountProvider, mockFeeRulesProvider, mockExternalTempProvider);
    }

    @Test
    public void testGetFeePerTrade() {
        // Setup
        final FeeCalculationRequest fcr = new FeeCalculationRequest();
        fcr.setAccountId("TEST-ACCOUNT");
        fcr.setAssetType("O");
        fcr.setTicker("EW1160205C1880.0.CMEO");
        fcr.setMarketMIC("XNAS");
        fcr.setSymbolCurrency("USD");
        fcr.setOrderExecutionId("23232");
        fcr.setExecutingBrokerAccountName("Bayou Executing Broker");
        fcr.setShortExecutingBrokerName("BY");
        fcr.setExchangeMIC("XIMM");
        fcr.setPrice(16.0);
        fcr.setQuantity(30);
        fcr.setTradeTime(new Date());

        when(mockAccountProvider.get("TEST-ACCOUNT")).thenReturn(new AccountProvider().get("TEST-ACCOUNT"));

        when(mockFeeRulesProvider.getAll()).thenReturn(new FeeRulesProvider().getAll());
        // Run the test
        final List<FeeCalculationResponse> result = feeCalculatorUnderTest.getFeePerTrade(fcr);


    }


    class AccountProvider implements IAccountProvider {

        @Override
        public Account get(String accountId) {
            final Account account = new Account();
            account.setAccountId(accountId);
            account.setSource("TEST-GROUP");
            final AccountSourceMappings accountSourceMappings = new AccountSourceMappings();
            accountSourceMappings.setAssetType("O");
            account.setAccountSourceMappings(Arrays.asList(accountSourceMappings));

            return account;
        }
    }

    class ExternalTempProvider implements IExternalTempProvider {

        @Override
        public ExternalTemp get(String hostOrderId, String accountId, Date tradeTime) {
            return null;
        }

        @Override
        public void add(String hostOrderId, String accountId, Date tradeTime) {

        }
    }

    class FeeRulesProvider implements IFeeRulesProvider {


        @Override
        public List<FeeRule> getByAccountIdAndRuleType(Long accountId, FeeRuleType feeRuleType) {
            return null;
        }

        @Override
        public List<FeeRule> getAll() {
            List<String> ownerList = new ArrayList<>();
            ownerList.add("TEST-GROUP");
            List<FeeRule> listRules = new ArrayList<>();

            FeeRule feeRule = new FeeRule();
            feeRule.setExchangeMIC("BY.NO_EXCH");
            feeRule.setExecutingBrokerName("BY");
            feeRule.setAssetType("O");
            feeRule.setCurrencyName("USD");
            feeRule.setFeeCurrencyName("USD");
            feeRule.setExecutionType("Trade");
            feeRule.setFeeCategory("Regulatory");
            feeRule.setDescription("OCC FEE");
            feeRule.setFeePerContract(0.05);
            feeRule.setIsActive(true);
            feeRule.setOwnersList(ownerList);
            feeRule.setRuleId(1L);

            FeeRuleBase feeRuleBase = new FeeRuleBase();
            feeRuleBase.setDateFrom(new Date());
            feeRuleBase.setDateTo(new Date());
            feeRuleBase.setRuleId(feeRule.getRuleId());

            listRules.add(feeRule);

            return listRules;
        }

        @Override
        public FeeRuleBase getByFeeRule(FeeRule feeRule) {
            return null;
        }

        @Override
        public FeeRuleComm getByRuleIdAndAccount(Long ruleId, String account) {
            return null;
        }

        @Override
        public List<FeeRuleComm> getByAccount(String account) {
            return null;
        }
    }
}
