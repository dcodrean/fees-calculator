package compute.engine.impl;

import compute.engine.IFeeRulesRetriever;
import model.entities.*;
import org.junit.Before;
import org.junit.Test;
import providers.IFeeRulesProvider;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FeeComputationTest {

    private FeeComputation feeComputationUnderTest;

    @Before
    public void setUp() {
        feeComputationUnderTest = new FeeComputation();
        feeComputationUnderTest.fr = mock(IFeeRulesRetriever.class);
    }

    @Test
    public void testComputeFeeCommissionCharge() {
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

        final Account account = new Account();
        account.setAccountId("accountId");
        account.setSource("source");
        final AccountSourceMappings accountSourceMappings = new AccountSourceMappings();
        accountSourceMappings.setAssetType("assetType");
        account.setAccountSourceMappings(Arrays.asList(accountSourceMappings));

        // Configure IFeeRulesRetriever.retrieveCommissionRules(...).
        final FeeRule feeRule = new FeeRule();
        feeRule.setRuleId(0L);
        feeRule.setExchangeMIC("exchangeMIC");
        feeRule.setExecutingBrokerName("executingBrokerName");
        feeRule.setExecutingBrokerAccountName("executingBrokerAccountName");
        feeRule.setExecutionBrokerCode("executionBrokerCode");
        feeRule.setAssetType("assetType");
        feeRule.setBrokerCode("brokerCode");
        feeRule.setCurrencyName("currencyName");
        feeRule.setFeeCurrencyName("feeCurrencyName");
        feeRule.setExecutionType("executionType");
        final List<FeeRule> feeRules = Arrays.asList(feeRule);
        when(feeComputationUnderTest.fr.retrieveCommissionRules(any(IFeeRulesProvider.class), any(FeeCalculationRequest.class), eq(false), eq("defaultFeeExchange"), eq(0.0), eq("tickerSymbol"), eq("tickerExch"))).thenReturn(feeRules);

        // Run the test
        final List<FeeCalculationResponse> result = feeComputationUnderTest.computeFeeCommissionCharge(feeRulesProvider, fcr, account, false, "defaultFeeExchange", 0.0, "tickerSymbol", "tickerExch", "oldHostOrderId");

        // Verify the results
    }

    @Test
    public void testComputeFeeBaseCharge() {
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

        final Account account = new Account();
        account.setAccountId("accountId");
        account.setSource("source");
        final AccountSourceMappings accountSourceMappings = new AccountSourceMappings();
        accountSourceMappings.setAssetType("assetType");
        account.setAccountSourceMappings(Arrays.asList(accountSourceMappings));

        // Configure IFeeRulesRetriever.retrieveNonExchangeBaseRules(...).
        final FeeRule feeRule = new FeeRule();
        feeRule.setRuleId(0L);
        feeRule.setExchangeMIC("exchangeMIC");
        feeRule.setExecutingBrokerName("executingBrokerName");
        feeRule.setExecutingBrokerAccountName("executingBrokerAccountName");
        feeRule.setExecutionBrokerCode("executionBrokerCode");
        feeRule.setAssetType("assetType");
        feeRule.setBrokerCode("brokerCode");
        feeRule.setCurrencyName("currencyName");
        feeRule.setFeeCurrencyName("feeCurrencyName");
        feeRule.setExecutionType("executionType");
        final List<FeeRule> feeRules = Arrays.asList(feeRule);
        when(feeComputationUnderTest.fr.retrieveNonExchangeBaseRules(any(IFeeRulesProvider.class), any(FeeCalculationRequest.class), eq(0.0), eq(false), eq("defaultFeeExchange"), eq("tickerSymbol"), eq("tickerRoot"), eq("tickerExch"))).thenReturn(feeRules);

        // Configure IFeeRulesRetriever.retrieveExchangeBaseRules(...).
        final FeeRule feeRule1 = new FeeRule();
        feeRule1.setRuleId(0L);
        feeRule1.setExchangeMIC("exchangeMIC");
        feeRule1.setExecutingBrokerName("executingBrokerName");
        feeRule1.setExecutingBrokerAccountName("executingBrokerAccountName");
        feeRule1.setExecutionBrokerCode("executionBrokerCode");
        feeRule1.setAssetType("assetType");
        feeRule1.setBrokerCode("brokerCode");
        feeRule1.setCurrencyName("currencyName");
        feeRule1.setFeeCurrencyName("feeCurrencyName");
        feeRule1.setExecutionType("executionType");
        final List<FeeRule> feeRules1 = Arrays.asList(feeRule1);
        when(feeComputationUnderTest.fr.retrieveExchangeBaseRules(any(IFeeRulesProvider.class), any(FeeCalculationRequest.class), eq(0.0), eq(false), eq("defaultFeeExchange"), eq("tickerSymbol"), eq("tickerRoot"), eq("tickerExch"))).thenReturn(feeRules1);

        // Run the test
        final List<FeeCalculationResponse> result = feeComputationUnderTest.computeFeeBaseCharge(feeRulesProvider, fcr, account, "feeLevel", 0.0, false, false, "defaultFeeExchange", "tickerSymbol", "tickerRoot", "tickerExch", "oldHostOrderId");

        // Verify the results
    }

    @Test
    public void testComputeFeeOutsideCommCharge() {
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

        // Run the test
        final List<FeeCalculationResponse> result = feeComputationUnderTest.computeFeeOutsideCommCharge(fcr, 0.0);

        // Verify the results
    }

    @Test
    public void testComputeFees() {
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

        final Account account = new Account();
        account.setAccountId("accountId");
        account.setSource("source");
        final AccountSourceMappings accountSourceMappings = new AccountSourceMappings();
        accountSourceMappings.setAssetType("assetType");
        account.setAccountSourceMappings(Arrays.asList(accountSourceMappings));

        final FeeRule feeRule = new FeeRule();
        feeRule.setRuleId(0L);
        feeRule.setExchangeMIC("exchangeMIC");
        feeRule.setExecutingBrokerName("executingBrokerName");
        feeRule.setExecutingBrokerAccountName("executingBrokerAccountName");
        feeRule.setExecutionBrokerCode("executionBrokerCode");
        feeRule.setAssetType("assetType");
        feeRule.setBrokerCode("brokerCode");
        feeRule.setCurrencyName("currencyName");
        feeRule.setFeeCurrencyName("feeCurrencyName");
        feeRule.setExecutionType("executionType");
        final List<FeeRule> feeRules = Arrays.asList(feeRule);

        // Run the test
        final List<FeeCalculationResponse> result = feeComputationUnderTest.computeFees(fcr, account, feeRules, "feeLevel", 0.0, false, false, "oldHostOrderId");

        // Verify the results
    }
}
