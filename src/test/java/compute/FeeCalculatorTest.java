package compute;

import model.entities.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import providers.IAccountProvider;
import providers.IExternalTempProvider;
import providers.IFeeRulesProvider;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.mockito.Mockito.verify;
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
        fcr.setAccountId("accountId");
        fcr.setAssetType("assetType");
        fcr.setTicker("ticker");
        fcr.setMarketMIC("marketMIC");
        fcr.setSymbolCurrency("symbolCurrency");
        fcr.setUnderlyingType("underlyingType");
        fcr.setUnderlyingTicker("underlyingTicker");
        fcr.setOrderExecutionId("orderExecutionId");
        fcr.setTicketId("ticketId");
        fcr.setQuantity(0);

        // Configure IAccountProvider.get(...).
        final Account account = new Account();
        account.setAccountId("accountId");
        account.setSource("source");
        final AccountSourceMappings accountSourceMappings = new AccountSourceMappings();
        accountSourceMappings.setAssetType("assetType");
        account.setAccountSourceMappings(Arrays.asList(accountSourceMappings));
        when(mockAccountProvider.get("accountId")).thenReturn(account);

        // Configure IFeeRulesProvider.getByAccount(...).
        final FeeRuleComm feeRuleComm = new FeeRuleComm();
        feeRuleComm.setRuleId(0L);
        feeRuleComm.setAccountId("accountId");
        feeRuleComm.setDateFrom(new GregorianCalendar(2019, Calendar.JANUARY, 1).getTime());
        feeRuleComm.setDateTo(new GregorianCalendar(2019, Calendar.JANUARY, 1).getTime());
        feeRuleComm.setDescription("description");
        feeRuleComm.setAllInExchangeMIC("allInExchangeMIC");
        final List<FeeRuleComm> feeRuleComms = Arrays.asList(feeRuleComm);
        when(mockFeeRulesProvider.getByAccount("account")).thenReturn(feeRuleComms);

        // Configure IExternalTempProvider.get(...).
        final ExternalTemp externalTemp = new ExternalTemp();
        externalTemp.setHostOrderId("hostOrderId");
        externalTemp.setTradeTime(new GregorianCalendar(2019, Calendar.JANUARY, 1).getTime());
        externalTemp.setAccountId("accountId");
        when(mockExternalTempProvider.get("hostOrderId", "accountId", new GregorianCalendar(2019, Calendar.JANUARY, 1).getTime())).thenReturn(externalTemp);

        // Run the test
        final List<FeeCalculationResponse> result = feeCalculatorUnderTest.getFeePerTrade(fcr);

        // Verify the results
        verify(mockExternalTempProvider).add("hostOrderId", "accountId", new GregorianCalendar(2019, Calendar.JANUARY, 1).getTime());
    }
}
