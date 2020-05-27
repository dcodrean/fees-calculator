package compute.engine.impl;

import model.entities.Account;
import model.entities.AccountSourceMappings;
import model.entities.Billable;
import model.entities.FeeCalculationRequest;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class FeeCalculatorHelperTest {

    private FeeCalculatorHelper feeCalculatorHelperUnderTest;

    @Before
    public void setUp() {
        feeCalculatorHelperUnderTest = new FeeCalculatorHelper();
    }

    @Test
    public void testBuildHostOrderId() {
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
        final String result = feeCalculatorHelperUnderTest.buildHostOrderId(fcr, "s");

        // Verify the results
        assertEquals("result", result);
    }

    @Test
    public void testHandleBillableFlags() {
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

        // Run the test
        final Billable result = feeCalculatorHelperUnderTest.handleBillableFlags(fcr, account);

        // Verify the results
    }
}
