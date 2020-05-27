package compute.engine.impl;

import compute.filters.IFilters;
import model.entities.FeeCalculationRequest;
import model.entities.FeeRule;
import model.entities.FeeRuleBase;
import model.entities.FeeRuleComm;
import org.junit.Before;
import org.junit.Test;
import providers.IFeeRulesProvider;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FeeRulesRetrieverTest {

    private FeeRulesRetriever feeRulesRetrieverUnderTest;

    @Before
    public void setUp() {
        feeRulesRetrieverUnderTest = new FeeRulesRetriever();
        feeRulesRetrieverUnderTest.filters = mock(IFilters.class);
    }

    @Test
    public void testRetrieveCommissionRules() {
        // Setup
        final IFeeRulesProvider feeRulesProvider = null;
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

        when(feeRulesRetrieverUnderTest.filters.filterOnCommAccountId(any(FeeRuleComm.class), eq("accountId"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnCommAllInExchangeMIC(any(FeeRuleComm.class), eq("allInExchangeMIC"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnCommTradeTime(any(FeeRuleComm.class), eq(new GregorianCalendar(2019, Calendar.JANUARY, 1).getTime()))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnAssetName(any(FeeRule.class), eq("assetName"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnIsActive(any(FeeRule.class))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnExecutionType(any(FeeRule.class), eq("executionType"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnPrice(any(FeeRule.class), eq(0.0))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnIsSaleOrBuy(any(FeeRule.class), eq(0))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnTradeFlags(any(FeeRule.class), eq("tradeFlags"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnIsCashDesk(any(FeeRule.class), eq(false), eq("destination"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnIsFeePerExecutionBrokerCode(any(FeeRule.class), eq(false), eq("brokerCode"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnIsPerExecutingBrokerAccountName(any(FeeRule.class), eq("executingBrokerAccountName"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnPrincipal(any(FeeRule.class), eq(0.0))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnExchangeMIC(any(FeeRule.class), eq("defaultExchangeMIC"), eq("exchangeMIC"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnCCYName(any(FeeRule.class), eq("ccyName"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnMarketMIC(any(FeeRule.class), eq("marketMIC"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnInstrumentAndExchangeMatch(any(FeeRule.class), eq("ticker"), eq("exchange"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnInstrumentIsNull(any(FeeRule.class))).thenReturn(false);

        // Run the test
        final List<FeeRule> result = feeRulesRetrieverUnderTest.retrieveCommissionRules(feeRulesProvider, fcr, false, "defaultFeeExchange", 0.0, "tickerSymbol", "tickerExch");

        // Verify the results
    }

    @Test
    public void testRetrieveNonExchangeBaseRules() {
        // Setup
        final IFeeRulesProvider feeRulesProvider = null;
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

        when(feeRulesRetrieverUnderTest.filters.filterOnExchangeMIC(any(FeeRule.class), eq("defaultExchangeMIC"), eq("exchangeMIC"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnCCYName(any(FeeRule.class), eq("ccyName"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnFeeRulesBaseDate(any(FeeRuleBase.class), eq(new GregorianCalendar(2019, Calendar.JANUARY, 1).getTime()))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnIsActive(any(FeeRule.class))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnMarketMIC(any(FeeRule.class), eq("marketMIC"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnAssetName(any(FeeRule.class), eq("assetName"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnExecutionType(any(FeeRule.class), eq("executionType"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnPrice(any(FeeRule.class), eq(0.0))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnIsSaleOrBuy(any(FeeRule.class), eq(0))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnTradeFlags(any(FeeRule.class), eq("tradeFlags"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnIsCashDesk(any(FeeRule.class), eq(false), eq("destination"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnIsFeePerExecutionBrokerCode(any(FeeRule.class), eq(false), eq("brokerCode"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnQuantity(any(FeeRule.class), eq(0))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnPrincipal(any(FeeRule.class), eq(0.0))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnIsPerExecutingBrokerAccountName(any(FeeRule.class), eq("executingBrokerAccountName"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnFeeCategory(any(FeeRule.class), eq("feeCategory"), eq(false))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnExecutingBrokerName(any(FeeRule.class), eq("executingBrokerName"), eq("tickerSymbol"), eq("tickerExch"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnSkipSEC(any(FeeRule.class), eq("underlyingType"), eq("assetType"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnUnderlyingType(any(FeeRule.class), eq("underlyingType"))).thenReturn(false);

        // Run the test
        final List<FeeRule> result = feeRulesRetrieverUnderTest.retrieveNonExchangeBaseRules(feeRulesProvider, fcr, 0.0, false, "defaultFeeExchange", "tickerSymbol", "tickerRoot", "tickerExch");

        // Verify the results
    }

    @Test
    public void testRetrieveExchangeBaseRules() {
        // Setup
        final IFeeRulesProvider feeRulesProvider = null;
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

        when(feeRulesRetrieverUnderTest.filters.filterOnExchangeMIC(any(FeeRule.class), eq("defaultExchangeMIC"), eq("exchangeMIC"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnCCYName(any(FeeRule.class), eq("ccyName"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnFeeRulesBaseDate(any(FeeRuleBase.class), eq(new GregorianCalendar(2019, Calendar.JANUARY, 1).getTime()))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnIsActive(any(FeeRule.class))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnMarketMIC(any(FeeRule.class), eq("marketMIC"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnAssetName(any(FeeRule.class), eq("assetName"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnExecutionType(any(FeeRule.class), eq("executionType"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnPrice(any(FeeRule.class), eq(0.0))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnIsSaleOrBuy(any(FeeRule.class), eq(0))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnTradeFlags(any(FeeRule.class), eq("tradeFlags"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnIsCashDesk(any(FeeRule.class), eq(false), eq("destination"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnIsFeePerExecutionBrokerCode(any(FeeRule.class), eq(false), eq("brokerCode"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnQuantity(any(FeeRule.class), eq(0))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnPrincipal(any(FeeRule.class), eq(0.0))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnIsPerExecutingBrokerAccountName(any(FeeRule.class), eq("executingBrokerAccountName"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnFeeCategory(any(FeeRule.class), eq("feeCategory"), eq(false))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnExecutingBrokerName(any(FeeRule.class), eq("executingBrokerName"), eq("tickerSymbol"), eq("tickerExch"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnSkipSEC(any(FeeRule.class), eq("underlyingType"), eq("assetType"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnUnderlyingType(any(FeeRule.class), eq("underlyingType"))).thenReturn(false);
        when(feeRulesRetrieverUnderTest.filters.filterOnDefaultExchangeMIC(any(FeeRule.class), eq("defaultExchangeMIC"))).thenReturn(false);

        // Run the test
        final List<FeeRule> result = feeRulesRetrieverUnderTest.retrieveExchangeBaseRules(feeRulesProvider, fcr, 0.0, false, "defaultFeeExchange", "tickerSymbol", "tickerRoot", "tickerExch");

        // Verify the results
    }
}
