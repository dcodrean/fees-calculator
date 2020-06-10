package compute;

import model.entities.*;
import model.types.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import providers.IAccountProvider;
import providers.IExternalTempProvider;
import providers.IFeeRulesProvider;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FeeCalculatorTest {

    @Mock
    private IAccountProvider mockAccountProvider;
    @Mock
    private IFeeRulesProvider mockFeeRulesProvider;
    @Mock
    private IExternalTempProvider mockExternalTempProvider;
    @Mock
    private FeeCalculator feeCalculatorUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        feeCalculatorUnderTest = new FeeCalculator(mockAccountProvider, mockFeeRulesProvider, mockExternalTempProvider);
    }

    @Test
    public void testGetFeePerTrade_withExternalCommission_PER_TICKET() {

        final FeeCalculationRequest fcr = new FeeCalculationRequest();
        String account = "TEST-ACCOUNT";
        fcr.setAccountId(account);
        fcr.setExternalCommType(ExternalCommType.PER_TICKET.name());
        fcr.setSymbolCurrency(CurrencyType.USD.name());
        fcr.setExternalCommRate(2.5);
        fcr.setQuantity(10);
        fcr.setPrice(56.8);
        fcr.setAssetType(AssetType.F.name());//S F sau O
        fcr.setFullExecutingBrokerName("FIX Bayou Broker");
        fcr.setBillableState("SPECIFIED");
        fcr.setTicker("RDP/13M.CME");
        fcr.setOrderExecutionId("10000");

        when(mockAccountProvider.get(account)).thenReturn(new AccountProvider().get(account));


        final List<FeeCalculationResponse> result = feeCalculatorUnderTest.getFeePerTrade(fcr);

        printDetailsResponse(result);
    }

    @Test
    public void testGetFeePerTrade_withExternalCommission_PER_UNIT() {

        final FeeCalculationRequest fcr = new FeeCalculationRequest();
        String account = "TEST-ACCOUNT";
        fcr.setAccountId(account);
        fcr.setExternalCommType(ExternalCommType.PER_UNIT.name());
        fcr.setSymbolCurrency(CurrencyType.USD.name());
        fcr.setExternalCommRate(1.5);
        fcr.setQuantity(10);
        fcr.setPrice(56.8);
        fcr.setAssetType(AssetType.F.name());//S F sau O
        fcr.setFullExecutingBrokerName("FIX Bayou Broker");
        fcr.setBillableState("SPECIFIED");
        fcr.setTicker("RDP/13M.CME");
        fcr.setOrderExecutionId("20000");
        fcr.setIsDoneAway(true);
        fcr.setShortExecutingBrokerName("BY");
        fcr.setIsDropCopy(true);

        when(mockAccountProvider.get(account)).thenReturn(new AccountProvider().get(account));


        final List<FeeCalculationResponse> result = feeCalculatorUnderTest.getFeePerTrade(fcr);

        printDetailsResponse(result);
    }

    @Test
    public void testGetFeePerTrade_withExternalCommission_BPS() {

        final FeeCalculationRequest fcr = new FeeCalculationRequest();
        String account = "TEST-ACCOUNT";
        fcr.setAccountId(account);
        fcr.setExternalCommType(ExternalCommType.BPS.name());
        fcr.setSymbolCurrency(CurrencyType.USD.name());
        fcr.setExternalCommRate(1.5);
        fcr.setQuantity(10);
        fcr.setPrice(56.8);
        fcr.setAssetType(AssetType.F.name());//S F sau O
        fcr.setFullExecutingBrokerName("FIX Bayou Broker");
        fcr.setBillableState("SPECIFIED");
        fcr.setTicker("RDP/13M.CME");
        fcr.setOrderExecutionId("30000");
        fcr.setIsDoneAway(true);
        fcr.setIsDropCopy(true);


        when(mockAccountProvider.get(account)).thenReturn(new AccountProvider().get(account));


        final List<FeeCalculationResponse> result = feeCalculatorUnderTest.getFeePerTrade(fcr);

        printDetailsResponse(result);
    }

    private void printDetailsResponse(List<FeeCalculationResponse> result) {
        for (FeeCalculationResponse feeCalculationResponse : result) {
            System.out.println(
                    " ORDER execution id: " + feeCalculationResponse.getOrderExecutionId()
                            + " Fee Level: " + feeCalculationResponse.getFeeLevel()
                            + " Fee Type: " + feeCalculationResponse.getFeeType()
                            + " Fee Category: " + feeCalculationResponse.getFeeCategory()
                            + " Currency: " + feeCalculationResponse.getCurrency()
                            + " Amount: " + feeCalculationResponse.getAmount()
                            + " Comm Rate: " + feeCalculationResponse.getCommRate()
            );
        }
    }


    @Test
    public void testGetFeePerTrade() {
        // Setup
        final FeeCalculationRequest fcr = new FeeCalculationRequest();
        String account = "TEST-ACCOUNT";
        fcr.setAccountId(account);
        fcr.setAssetType("O");
        fcr.setTicker("EW1160205C1880.0.CMEO");
        fcr.setMarketMIC("XNAS");
        fcr.setSymbolCurrency("USD");
        fcr.setOrderExecutionId("23232");
        fcr.setExecutingBrokerAccountName("Bayou Executing Broker");
        fcr.setShortExecutingBrokerName("BY");
        fcr.setFullExecutingBrokerName("Bayou Executing Broker");
        fcr.setExchangeMIC("XIMM");
        fcr.setBillableState("YES");
        fcr.setPrice(16.0);
        fcr.setQuantity(30);
        Calendar tradeTimeCal = Calendar.getInstance();
        tradeTimeCal.set(Calendar.YEAR, 2020);
        tradeTimeCal.set(Calendar.MONTH, 4);
        tradeTimeCal.set(Calendar.DAY_OF_MONTH, 25);
        Date tradeTime = tradeTimeCal.getTime();
        fcr.setTradeTime(tradeTime);

        when(mockAccountProvider.get(account)).thenReturn(new AccountProvider().get(account));

        when(mockFeeRulesProvider.getByType(FeeLevelType.Firm.name())).thenReturn(new FeeRulesProvider().getByType(FeeLevelType.Firm.name()));
        when(mockFeeRulesProvider.getByType(FeeLevelType.Base.name())).thenReturn(new FeeRulesProvider().getByType(FeeLevelType.Base.name()));

        when(mockFeeRulesProvider.getByFeeRule(1L)).thenReturn(new FeeRulesProvider().getByFeeRule(1L));
        when(mockFeeRulesProvider.getByFeeRule(2L)).thenReturn(new FeeRulesProvider().getByFeeRule(2L));

        when(mockFeeRulesProvider.getByRuleIdAndAccount(any(), any())).thenReturn(new FeeRulesProvider().getByRuleIdAndAccount(2L, account));
        // Run the test
        final List<FeeCalculationResponse> result = feeCalculatorUnderTest.getFeePerTrade(fcr);

        printDetailsResponse(result);

    }


    static class AccountProvider implements IAccountProvider {

        @Override
        public Account get(String accountId) {
            final Account account = new Account();
            account.setAccountId(accountId);
            account.setSource("TEST-GROUP");
            final AccountSourceMappings accountSourceMappings = new AccountSourceMappings();
            accountSourceMappings.setAssetType("O");
            account.setAccountSourceMappings(Collections.singletonList(accountSourceMappings));

            return account;
        }
    }

    static class ExternalTempProvider implements IExternalTempProvider {

        @Override
        public ExternalTemp get(String hostOrderId, String accountId, Date tradeTime) {
            return null;
        }

        @Override
        public void add(String hostOrderId, String accountId, Date tradeTime) {

        }
    }

    static class FeeRulesProvider implements IFeeRulesProvider {


        @Override
        public List<FeeRule> getByAccountIdAndRuleType(Long accountId, FeeRuleType feeRuleType) {
            return null;
        }

        @Override
        public List<FeeRule> getAll() {
            List<String> ownerList = new ArrayList<>();
            ownerList.add("TEST-GROUP");
            List<FeeRule> listRules = new ArrayList<>();
            //BASE
            FeeRule feeRule = new FeeRule();
            feeRule.setExchangeMIC("BY.NO_EXCH");
            feeRule.setExecutingBrokerName("BY");
            feeRule.setAssetType("O");
            feeRule.setCurrencyName("USD");
            feeRule.setFeeCurrencyName("USD");
            feeRule.setExecutionType("Trade");
            feeRule.setFeeCategory(FeeCategoryType.Regulatory.name());
            feeRule.setDescription("OCC FEE");
            feeRule.setFeePerContract(0.05);
            feeRule.setIsActive(true);
            feeRule.setOwnersList(ownerList);
            feeRule.setFeeLevel(FeeLevelType.Base.name());
            feeRule.setRuleId(1L);
            listRules.add(feeRule);
            //COMM
            feeRule = new FeeRule();
            feeRule.setExchangeMIC("BY.NO_EXCH");
            feeRule.setExecutingBrokerName("BY");
            feeRule.setAssetType("O");
            feeRule.setCurrencyName("USD");
            feeRule.setFeeCurrencyName("USD");
            feeRule.setExecutionType("Trade");
            feeRule.setFeeCategory(FeeCategoryType.Commission.name());
            feeRule.setDescription("Options - Commission rate");
            feeRule.setFeePerContract(6.0);
            feeRule.setIsActive(true);
            feeRule.setFeeLevel(FeeLevelType.Firm.name());
            feeRule.setOwnersList(ownerList);
            feeRule.setRuleId(2L);
            listRules.add(feeRule);


            return listRules;
        }

        @Override
        public List<FeeRule> getByType(String feeLevel) {
            List<FeeRule> allData = getAll();

            List<FeeRule> filtered = new ArrayList<>();

            for (FeeRule feeRule : allData) {
                if (feeRule.getFeeLevel().equals(feeLevel)) {
                    filtered.add(feeRule);
                }
            }

            return filtered;
        }

        @Override
        public FeeRuleBase getByFeeRule(Long ruleId) {
            FeeRuleBase feeRuleBase = new FeeRuleBase();

            if (ruleId == 1) {
                feeRuleBase.setRuleId(1L);
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.YEAR, 2020);
                myCalendar.set(Calendar.MONTH, 4);
                myCalendar.set(Calendar.DAY_OF_MONTH, 23);
                Date theDateBefore = myCalendar.getTime();
                Calendar myCalendarAfter = Calendar.getInstance();
                myCalendarAfter.set(Calendar.YEAR, 3000);
                myCalendarAfter.set(Calendar.MONTH, 4);
                myCalendarAfter.set(Calendar.DAY_OF_MONTH, 23);
                Date theDateAfter = myCalendarAfter.getTime();
                feeRuleBase.setDateFrom(theDateBefore);
                feeRuleBase.setDateTo(theDateAfter);
            }
            return feeRuleBase;
        }

        @Override
        public FeeRuleComm getByRuleIdAndAccount(Long ruleId, String account) {
            FeeRuleComm feeRuleComm = new FeeRuleComm();

            if (ruleId == 2 && account.equals("TEST-ACCOUNT")) {
                feeRuleComm.setRuleId(2L);
                feeRuleComm.setAccountId("TEST-ACCOUNT");

                Calendar myCalendarFrom = Calendar.getInstance();
                myCalendarFrom.set(Calendar.YEAR, 2020);
                myCalendarFrom.set(Calendar.MONTH, 1);
                myCalendarFrom.set(Calendar.DAY_OF_MONTH, 1);
                Date theDateFrom = myCalendarFrom.getTime();
                feeRuleComm.setDateFrom(theDateFrom);

                Calendar myCalendarTo = Calendar.getInstance();
                myCalendarTo.set(Calendar.YEAR, 3000);
                myCalendarTo.set(Calendar.MONTH, 4);
                myCalendarTo.set(Calendar.DAY_OF_MONTH, 23);
                Date theDateTo = myCalendarTo.getTime();
                feeRuleComm.setDateTo(theDateTo);

                feeRuleComm.setDescription("USD");
                feeRuleComm.setAllInExchangeMIC("Trade");
            }

            return feeRuleComm;
        }

        @Override
        public List<FeeRuleComm> getByAccount(String account) {
            List<FeeRuleComm> list = new ArrayList<>();
            FeeRuleComm feeRuleComm = new FeeRuleComm();

            if (account.equals("TEST-ACCOUNT")) {
                feeRuleComm.setRuleId(2L);
                feeRuleComm.setAccountId("TEST-ACCOUNT");

                Calendar myCalendarFrom = Calendar.getInstance();
                myCalendarFrom.set(Calendar.YEAR, 2020);
                myCalendarFrom.set(Calendar.MONTH, 1);
                myCalendarFrom.set(Calendar.DAY_OF_MONTH, 1);
                Date theDateFrom = myCalendarFrom.getTime();
                feeRuleComm.setDateFrom(theDateFrom);

                Calendar myCalendarTo = Calendar.getInstance();
                myCalendarTo.set(Calendar.YEAR, 3000);
                myCalendarTo.set(Calendar.MONTH, 4);
                myCalendarTo.set(Calendar.DAY_OF_MONTH, 23);
                Date theDateTo = myCalendarTo.getTime();
                feeRuleComm.setDateTo(theDateTo);

                feeRuleComm.setDescription("USD");
                feeRuleComm.setAllInExchangeMIC("Trade");
            }
            list.add(feeRuleComm);
            return list;
        }

    }
}
