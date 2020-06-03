package compute.filters;


import model.entities.FeeCalculationRequest;
import model.entities.FeeRule;
import model.entities.FeeRuleBase;
import model.entities.FeeRuleComm;
import model.types.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class FiltersTest {
    IFilters filters;
    FeeRuleComm frc;
    FeeRule fr;
    FeeRuleBase frb;
    FeeCalculationRequest fcr;
    List<FeeRuleComm> l;

    @Before
    public void setUp() {
        frc = Mockito.mock(FeeRuleComm.class);
        filters = new Filters();
        fr = Mockito.mock(FeeRule.class);
        frb = Mockito.mock(FeeRuleBase.class);
        fcr = Mockito.mock(FeeCalculationRequest.class);
        l = new ArrayList<>();

    }

    @Test
    public void filterOnCommAccountIdTrue_accountIdEqual() {

        when(frc.getAccountId()).thenReturn("ACC023");
        boolean test = filters.filterOnCommAccountId(frc, "ACC023");

        assertTrue(test);

    }

    @Test
    public void filterOnCommAccountIdFalse_accountIdNotEqual() {

        when(frc.getAccountId()).thenReturn("ACC023");
        boolean test = filters.filterOnCommAccountId(frc, "ACC987");

        assertFalse(test);

    }

    @Test
    public void filterOnCommAllInExchangeMICTrue_allInExchangeMICEqual() {

        when(frc.getAllInExchangeMIC()).thenReturn("BY.XNAS");
        boolean test = filters.filterOnCommAllInExchangeMIC(frc, "BY.XNAS");

        assertTrue(test);

    }

    @Test
    public void filterOnCommAllInExchangeMICFalse_allInExchangeMICNotEqual() {

        when(frc.getAllInExchangeMIC()).thenReturn("BY.XNAS");
        boolean test = filters.filterOnCommAllInExchangeMIC(frc, "BY.XLON");

        assertFalse(test);

    }

    @Test
    public void filterOnCommTradeTimeTrue() {

        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.YEAR, 2020);
        myCalendar.set(Calendar.MONTH, 4);
        myCalendar.set(Calendar.DAY_OF_MONTH, 23);
        Date theDateBefore = myCalendar.getTime();

        Calendar myCalendar2 = Calendar.getInstance();
        myCalendar2.set(Calendar.YEAR, 2020);
        myCalendar2.set(Calendar.MONTH, 6);
        myCalendar2.set(Calendar.DAY_OF_MONTH, 23);
        Date tradeDateAfter = myCalendar2.getTime();

        Calendar myCalendar3 = Calendar.getInstance();
        myCalendar3.set(Calendar.YEAR, 2020);
        myCalendar3.set(Calendar.MONTH, 5);
        myCalendar3.set(Calendar.DAY_OF_MONTH, 23);
        Date tradeDate = myCalendar3.getTime();

        //theDateBefore: 2020-04-23
        when(frc.getDateFrom()).thenReturn(theDateBefore);
        //tradeDateAfter: 2020-06-23
        when(frc.getDateTo()).thenReturn(tradeDateAfter);
        //tradeDate: 2020-05-23
        boolean test = filters.filterOnCommTradeTime(frc, tradeDate);

        assertTrue(test);

    }

    @Test
    public void filterOnCommTradeTimeFalse_theDateBeforeIsAfterTradeTime() {

        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.YEAR, 2020);
        myCalendar.set(Calendar.MONTH, 6);
        myCalendar.set(Calendar.DAY_OF_MONTH, 1);
        Date theDateBefore = myCalendar.getTime();

        Calendar myCalendar2 = Calendar.getInstance();
        myCalendar2.set(Calendar.YEAR, 2020);
        myCalendar2.set(Calendar.MONTH, 3);
        myCalendar2.set(Calendar.DAY_OF_MONTH, 23);
        Date tradeDateAfter = myCalendar2.getTime();

        Calendar myCalendar3 = Calendar.getInstance();
        myCalendar3.set(Calendar.YEAR, 2020);
        myCalendar3.set(Calendar.MONTH, 5);
        myCalendar3.set(Calendar.DAY_OF_MONTH, 23);
        Date tradeDate = myCalendar3.getTime();
        //theDateBefore: 2020-06-01
        when(frc.getDateFrom()).thenReturn(theDateBefore);
        //tradeDateAfter: 2020-03-23
        when(frc.getDateTo()).thenReturn(tradeDateAfter);
        //tradeDate: 2020-05-23
        boolean test = filters.filterOnCommTradeTime(frc, tradeDate);

        assertFalse(test);

    }

    @Test
    public void filterOnCommTradeTimeFalse_theDateAfterIsBeforeTradeTime() {

        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.YEAR, 2020);
        myCalendar.set(Calendar.MONTH, 5);
        myCalendar.set(Calendar.DAY_OF_MONTH, 1);
        Date theDateBefore = myCalendar.getTime();

        Calendar myCalendar2 = Calendar.getInstance();
        myCalendar2.set(Calendar.YEAR, 2020);
        myCalendar2.set(Calendar.MONTH, 3);
        myCalendar2.set(Calendar.DAY_OF_MONTH, 23);
        Date tradeDateAfter = myCalendar2.getTime();

        Calendar myCalendar3 = Calendar.getInstance();
        myCalendar3.set(Calendar.YEAR, 2020);
        myCalendar3.set(Calendar.MONTH, 5);
        myCalendar3.set(Calendar.DAY_OF_MONTH, 23);
        Date tradeDate = myCalendar3.getTime();
        //theDateBefore: 2020-05-01
        when(frc.getDateFrom()).thenReturn(theDateBefore);
        //tradeDateAfter: 2020-03-23
        when(frc.getDateTo()).thenReturn(tradeDateAfter);
        //tradeDate: 2020-05-23
        boolean test = filters.filterOnCommTradeTime(frc, tradeDate);

        assertFalse(test);

    }


    @Test
    public void filterOnDefaultExchangeMICTrue_exchangeMICEqual() {

        when(fr.getExchangeMIC()).thenReturn("BY.XNAS");
        boolean test = filters.filterOnDefaultExchangeMIC(fr, "BY.XNAS");

        assertTrue(test);

    }

    @Test
    public void filterOnDefaultExchangeMICFalse_notEqual_exchangeMICNotEqual() {

        when(fr.getExchangeMIC()).thenReturn("BY.XNAS");
        boolean test = filters.filterOnDefaultExchangeMIC(fr, "BY.XLON");

        assertFalse(test);

    }

    @Test
    public void filterOnExchangeMICTrue_allEqual() {

        when(fr.getExchangeMIC()).thenReturn("BY.XNAS");
        boolean test = filters.filterOnExchangeMIC(fr, "BY.XNAS", "BY.XNAS");

        assertTrue(test);

    }

    @Test
    public void filterOnExchangeMICTrue_defaultExchangeMICEqual() {

        when(fr.getExchangeMIC()).thenReturn("BY.XNAS");
        boolean test = filters.filterOnExchangeMIC(fr, "BY.XNAS", "BY.XLON");

        assertTrue(test);

    }

    @Test
    public void filterOnExchangeMICTrue_equal_exchangeMICEquals() {

        when(fr.getExchangeMIC()).thenReturn("BY.XLON");
        boolean test = filters.filterOnExchangeMIC(fr, "BY.XNAS", "BY.XLON");

        assertTrue(test);

    }


    @Test
    public void filterOnExchangeMICFalse_allNotEqual() {

        when(fr.getExchangeMIC()).thenReturn("BY.XLON");
        boolean test = filters.filterOnExchangeMIC(fr, "BY.XNAS", "BY.XNYS");

        assertFalse(test);

    }

    @Test
    public void filterOnMarketMICTrue_marketMICEqual() {

        when(fr.getMarketMIC()).thenReturn("XNAS");
        boolean test = filters.filterOnMarketMIC(fr, "XNAS");

        assertTrue(test);
    }

    @Test
    public void filterOnMarketMICTrue_marketMICIsNull() {

        when(fr.getMarketMIC()).thenReturn(null);
        boolean test = filters.filterOnMarketMIC(fr, "XNAS");

        assertTrue(test);
    }

    @Test
    public void filterOnMarketMICFalse_marketMICNotEqual() {

        when(fr.getMarketMIC()).thenReturn("XNAS");
        boolean test = filters.filterOnMarketMIC(fr, "XLON");

        assertFalse(test);

    }

    @Test
    public void filterOnAssetTypeTrue_assetTypeEqualSTOCK() {

        when(fr.getAssetType()).thenReturn(AssetType.S.name());
        boolean test = filters.filterOnAssetType(fr, AssetType.S.name());

        assertTrue(test);

    }

    @Test
    public void filterOnAssetTypeTrue_assetTypeEqualOPTIONS() {

        when(fr.getAssetType()).thenReturn(AssetType.O.name());
        boolean test = filters.filterOnAssetType(fr, AssetType.O.name());

        assertTrue(test);

    }

    @Test
    public void filterOnAssetTypeTrue_assetTypeEqualFUTURES() {

        when(fr.getAssetType()).thenReturn(AssetType.F.name());
        boolean test = filters.filterOnAssetType(fr, AssetType.F.name());

        assertTrue(test);

    }


    @Test
    public void filterOnAssetTypeFalse_assetTypeNotEqual() {

        when(fr.getAssetType()).thenReturn(AssetType.F.name());
        boolean test = filters.filterOnAssetType(fr, AssetType.S.name());

        assertFalse(test);

    }

    @Test
    public void filterOnExecutionTypeTrue_executionTypeEqual() {

        when(fr.getExecutionType()).thenReturn(ExecutionType.Trade.name());
        boolean test = filters.filterOnExecutionType(fr, ExecutionType.Trade.name());

        assertTrue(test);
    }

    @Test
    public void filterOnExecutionTypeFalse_executionTypeNotEqual() {

        when(fr.getExecutionType()).thenReturn(ExecutionType.Trade.name());
        boolean test = filters.filterOnExecutionType(fr, "Ticket");

        assertFalse(test);
    }

    @Test
    public void filterOnPriceTrue_priceStartLessThanPrice_priceEndMore() {

        when(fr.getPriceStart()).thenReturn(10.9);
        when(fr.getPriceEnd()).thenReturn(100.7);
        boolean test = filters.filterOnPrice(fr, 50.6);

        assertTrue(test);
    }

    @Test
    public void filterOnPriceTrue_priceStart_priceEnd_isNull() {

        when(fr.getPriceStart()).thenReturn(null);
        when(fr.getPriceEnd()).thenReturn(null);
        boolean test = filters.filterOnPrice(fr, 50.6);

        assertTrue(test);
    }

    @Test
    public void filterOnPriceTrue_priceEndIsNull() {

        when(fr.getPriceStart()).thenReturn(20.7);
        when(fr.getPriceEnd()).thenReturn(null);
        boolean test = filters.filterOnPrice(fr, 50.6);

        assertTrue(test);
    }

    @Test
    public void filterOnPriceTrue_priceStartIsNull() {

        when(fr.getPriceStart()).thenReturn(null);
        when(fr.getPriceEnd()).thenReturn(100.5);
        boolean test = filters.filterOnPrice(fr, 50.6);

        assertTrue(test);
    }

    @Test
    public void filterOnPriceFalse_priceStartMoreThanPrice_priceEndLess() {

        when(fr.getPriceStart()).thenReturn(100.9);
        when(fr.getPriceEnd()).thenReturn(10.7);
        boolean test = filters.filterOnPrice(fr, 50.6);

        assertFalse(test);
    }


    @Test
    public void filterOnCCYNameTrue_currencyNameIsNull() {

        when(fr.getCurrencyName()).thenReturn(null);
        boolean test = filters.filterOnCCYName(fr, CurrencyType.CHF.name());

        assertTrue(test);
    }

    @Test
    public void filterOnCCYNameTrue_currencyNameEqual() {

        when(fr.getCurrencyName()).thenReturn(CurrencyType.CHF.name());
        boolean test = filters.filterOnCCYName(fr, CurrencyType.CHF.name());

        assertTrue(test);
    }

    @Test
    public void filterOnCCYNameFalse_currencyNameNotEqual() {

        when(fr.getCurrencyName()).thenReturn(CurrencyType.CHF.name());
        boolean test = filters.filterOnCCYName(fr, CurrencyType.EUR.name());

        assertFalse(test);
    }

    @Test
    public void filterOnCommissionAllInFeeLevelTrue() {

        when(frc.getAccountId()).thenReturn("ACC023");
        when(frc.getAllInExchangeMIC()).thenReturn("BY.XNAS");

        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.YEAR, 2020);
        myCalendar.set(Calendar.MONTH, 4);
        myCalendar.set(Calendar.DAY_OF_MONTH, 23);
        Date theDateBefore = myCalendar.getTime();

        Calendar myCalendar2 = Calendar.getInstance();
        myCalendar2.set(Calendar.YEAR, 2020);
        myCalendar2.set(Calendar.MONTH, 6);
        myCalendar2.set(Calendar.DAY_OF_MONTH, 23);
        Date tradeDateAfter = myCalendar2.getTime();

        Calendar myCalendar3 = Calendar.getInstance();
        myCalendar3.set(Calendar.YEAR, 2020);
        myCalendar3.set(Calendar.MONTH, 5);
        myCalendar3.set(Calendar.DAY_OF_MONTH, 23);
        Date tradeTime = myCalendar3.getTime();
        //2020-04-23
        when(frc.getDateFrom()).thenReturn(theDateBefore);
        //2020-06-23
        when(frc.getDateTo()).thenReturn(tradeDateAfter);
        //2020-05-23
        boolean test = filters.filterOnCommissionAllInFeeLevel(frc, "ACC023", "BY.XNAS", tradeTime);

        assertTrue(test);
    }

    @Test
    public void filterOnCommissionAllInFeeLevelFalse_accountIsNotEqual() {

        when(frc.getAccountId()).thenReturn("ACC123");
        when(frc.getAllInExchangeMIC()).thenReturn("BY.XNAS");

        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.YEAR, 2020);
        myCalendar.set(Calendar.MONTH, 4);
        myCalendar.set(Calendar.DAY_OF_MONTH, 23);
        Date theDateBefore = myCalendar.getTime();

        Calendar myCalendar2 = Calendar.getInstance();
        myCalendar2.set(Calendar.YEAR, 2020);
        myCalendar2.set(Calendar.MONTH, 6);
        myCalendar2.set(Calendar.DAY_OF_MONTH, 23);
        Date tradeDateAfter = myCalendar2.getTime();

        Calendar myCalendar3 = Calendar.getInstance();
        myCalendar3.set(Calendar.YEAR, 2020);
        myCalendar3.set(Calendar.MONTH, 5);
        myCalendar3.set(Calendar.DAY_OF_MONTH, 23);
        Date tradeTime = myCalendar3.getTime();
        //2020-04-23
        when(frc.getDateFrom()).thenReturn(theDateBefore);
        //2020-06-23
        when(frc.getDateTo()).thenReturn(tradeDateAfter);
        //2020-05-23
        boolean test = filters.filterOnCommissionAllInFeeLevel(frc, "ACC987", "BY.XNAS", tradeTime);

        assertFalse(test);
    }

    @Test
    public void filterOnCommissionAllInFeeLevelFalse_exchangeMICIsNotEqual() {

        when(frc.getAccountId()).thenReturn("ACC123");
        when(frc.getAllInExchangeMIC()).thenReturn("BY.XNAS");

        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.YEAR, 2020);
        myCalendar.set(Calendar.MONTH, 4);
        myCalendar.set(Calendar.DAY_OF_MONTH, 23);
        Date theDateBefore = myCalendar.getTime();

        Calendar myCalendar2 = Calendar.getInstance();
        myCalendar2.set(Calendar.YEAR, 2020);
        myCalendar2.set(Calendar.MONTH, 6);
        myCalendar2.set(Calendar.DAY_OF_MONTH, 23);
        Date tradeDateAfter = myCalendar2.getTime();

        Calendar myCalendar3 = Calendar.getInstance();
        myCalendar3.set(Calendar.YEAR, 2020);
        myCalendar3.set(Calendar.MONTH, 5);
        myCalendar3.set(Calendar.DAY_OF_MONTH, 23);
        Date tradeTime = myCalendar3.getTime();
        //2020-04-23
        when(frc.getDateFrom()).thenReturn(theDateBefore);
        //2020-06-23
        when(frc.getDateTo()).thenReturn(tradeDateAfter);
        //2020-05-23
        boolean test = filters.filterOnCommissionAllInFeeLevel(frc, "ACC123", "BY.XLON", tradeTime);

        assertFalse(test);
    }


    @Test
    public void filterOnFeeRulesBaseDateTrue_dateBeforeTradeTime() {

        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.YEAR, 2020);
        myCalendar.set(Calendar.MONTH, 4);
        myCalendar.set(Calendar.DAY_OF_MONTH, 23);
        Date theDateBefore = myCalendar.getTime();

        Calendar myCalendar2 = Calendar.getInstance();
        myCalendar2.set(Calendar.YEAR, 2020);
        myCalendar2.set(Calendar.MONTH, 6);
        myCalendar2.set(Calendar.DAY_OF_MONTH, 23);
        Date tradeDateAfter = myCalendar2.getTime();

        Calendar myCalendar3 = Calendar.getInstance();
        myCalendar3.set(Calendar.YEAR, 2020);
        myCalendar3.set(Calendar.MONTH, 5);
        myCalendar3.set(Calendar.DAY_OF_MONTH, 23);
        Date tradeTime = myCalendar3.getTime();

        //2020-04-23
        when(frb.getDateFrom()).thenReturn(theDateBefore);
        //2020-06-23
        when(frb.getDateTo()).thenReturn(tradeDateAfter);
        //2020-05-23
        boolean test = filters.filterOnFeeRulesBaseDate(frb, tradeTime);

        assertTrue(test);

    }

    @Test
    public void filterOnFeeRulesBaseDateTrue_dateAfterTradeTime() {

        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.YEAR, 2020);
        myCalendar.set(Calendar.MONTH, 8);
        myCalendar.set(Calendar.DAY_OF_MONTH, 23);
        Date theDateBefore = myCalendar.getTime();

        Calendar myCalendar2 = Calendar.getInstance();
        myCalendar2.set(Calendar.YEAR, 2020);
        myCalendar2.set(Calendar.MONTH, 6);
        myCalendar2.set(Calendar.DAY_OF_MONTH, 23);
        Date tradeDateAfter = myCalendar2.getTime();

        Calendar myCalendar3 = Calendar.getInstance();
        myCalendar3.set(Calendar.YEAR, 2020);
        myCalendar3.set(Calendar.MONTH, 5);
        myCalendar3.set(Calendar.DAY_OF_MONTH, 23);
        Date tradeTime = myCalendar3.getTime();

        //2020-08-23
        when(frb.getDateFrom()).thenReturn(theDateBefore);
        //2020-06-23
        when(frb.getDateTo()).thenReturn(tradeDateAfter);
        //2020-05-23
        boolean test = filters.filterOnFeeRulesBaseDate(frb, tradeTime);

        assertTrue(test);

    }


    @Test
    public void filterOnFeeRulesBaseDateFalse() {

        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.YEAR, 2020);
        myCalendar.set(Calendar.MONTH, 6);
        myCalendar.set(Calendar.DAY_OF_MONTH, 23);
        Date theDateBefore = myCalendar.getTime();

        Calendar myCalendar2 = Calendar.getInstance();
        myCalendar2.set(Calendar.YEAR, 2020);
        myCalendar2.set(Calendar.MONTH, 4);
        myCalendar2.set(Calendar.DAY_OF_MONTH, 23);
        Date tradeDateAfter = myCalendar2.getTime();

        Calendar myCalendar3 = Calendar.getInstance();
        myCalendar3.set(Calendar.YEAR, 2020);
        myCalendar3.set(Calendar.MONTH, 5);
        myCalendar3.set(Calendar.DAY_OF_MONTH, 23);
        Date tradeTime = myCalendar3.getTime();

        //2020-06-23
        when(frb.getDateFrom()).thenReturn(theDateBefore);
        //2020-04-23
        when(frb.getDateTo()).thenReturn(tradeDateAfter);
        //2020-05-23
        boolean test = filters.filterOnFeeRulesBaseDate(frb, tradeTime);

        assertFalse(test);

    }

    @Test
    public void filterOnIsActiveTrue_isActive() {

        when(fr.getIsActive()).thenReturn(true);
        boolean test = filters.filterOnIsActive(fr);

        assertTrue(test);
    }

    @Test
    public void filterOnIsActiveFalse_isActive() {

        when(fr.getIsActive()).thenReturn(false);
        boolean test = filters.filterOnIsActive(fr);

        assertFalse(test);
    }

    @Test
    public void filterOnInstrumentIsNullTrue() {

        when(fr.getInstrument()).thenReturn(null);
        boolean test = filters.filterOnInstrumentIsNull(fr);

        assertTrue(test);
    }

    @Test
    public void filterOnInstrumentIsNullFalse_instrumentNotNull() {

        when(fr.getInstrument()).thenReturn("6N.CME");
        boolean test = filters.filterOnInstrumentIsNull(fr);

        assertFalse(test);
    }

    @Test
    public void filterOnIsSaleOrBuyTrue_isSaleOrBuyIsNull() {

        when(fr.getIsSaleOrBuy()).thenReturn(null);
        boolean test = filters.filterOnIsSaleOrBuy(fr, 1);

        assertTrue(test);
    }

    @Test
    public void filterOnIsSaleOrBuyTrue_isSaleOrBuyIsYes_quantityLessThanZero() {

        when(fr.getIsSaleOrBuy()).thenReturn(true);
        boolean test = filters.filterOnIsSaleOrBuy(fr, -1);

        assertTrue(test);
    }

    @Test
    public void filterOnIsSaleOrBuyTrue_isSaleOrBuyIsYes_quantityMoreThanZero() {

        when(fr.getIsSaleOrBuy()).thenReturn(true);
        boolean test = filters.filterOnIsSaleOrBuy(fr, 1);

        assertTrue(test);
    }

    @Test
    public void filterOnIsSaleOrBuyFalse_isSaleOrBuyIsNotYes_quantityLessThanZero() {

        when(fr.getIsSaleOrBuy()).thenReturn(false);
        boolean test = filters.filterOnIsSaleOrBuy(fr, -10);

        assertFalse(test);
    }

    @Test
    public void filterOnIsSaleOrBuyFalse_isSaleOrBuyIsNotYes_quantityMoreThanZero() {

        when(fr.getIsSaleOrBuy()).thenReturn(false);
        boolean test = filters.filterOnIsSaleOrBuy(fr, 10);

        assertFalse(test);
    }

    @Test
    public void filterOnIsSaleOrBuyFalse_isSaleOrBuyIsYes_quantityMoreThanZero() {

        when(fr.getIsSaleOrBuy()).thenReturn(false);
        boolean test = filters.filterOnIsSaleOrBuy(fr, 100);

        assertFalse(test);
    }

    @Test
    public void filterOnIsSaleOrBuyFalse_isSaleOrBuyIsYes_quantityLessThanZero() {

        when(fr.getIsSaleOrBuy()).thenReturn(false);
        boolean test = filters.filterOnIsSaleOrBuy(fr, -56);

        assertFalse(test);
    }

    @Test
    public void filterOnTradeFlagsTrue_tradeFlagsIsNull_isAggressorIsNull() {

        when(fr.getTradeFlags()).thenReturn(null);
        when(fr.getIsAggressor()).thenReturn(null);
        boolean test = filters.filterOnTradeFlags(fr, "T");

        assertTrue(test);
    }

    @Test
    public void filterOnTradeFlagsTrue_tradeFlagsIsNull_isAggressorIsNO() {

        when(fr.getTradeFlags()).thenReturn(null);
        when(fr.getIsAggressor()).thenReturn(false);
        boolean test = filters.filterOnTradeFlags(fr, "T");

        assertTrue(test);
    }

    @Test
    public void filterOnTradeFlagsTrue_tradeFlagsIsNotNull_isAggressorIsNO() {

        when(fr.getTradeFlags()).thenReturn("T");
        when(fr.getIsAggressor()).thenReturn(false);
        boolean test = filters.filterOnTradeFlags(fr, "T");

        assertTrue(test);
    }

    @Test
    public void filterOnTradeFlagsFalse_tradeFlagsIsNull_isAggressorNotIsNO() {

        when(fr.getTradeFlags()).thenReturn(null);
        when(fr.getIsAggressor()).thenReturn(true);
        boolean test = filters.filterOnTradeFlags(fr, "T");

        assertFalse(test);
    }

    @Test
    public void filterOnTradeFlagsFalse_tradeFlagsIsNotNull_isAggressorNotIsNO() {

        when(fr.getTradeFlags()).thenReturn("T");
        when(fr.getIsAggressor()).thenReturn(true);
        boolean test = filters.filterOnTradeFlags(fr, "F6");

        assertFalse(test);
    }

    @Test
    public void filterOnIsCashDeskTrue() {

        when(fr.getIsCashDesk()).thenReturn(true);
        when(fr.getDestination()).thenReturn("XNAS");
        boolean test = filters.filterOnIsCashDesk(fr, true, "XNAS");

        assertTrue(test);
    }

    @Test
    public void filterOnIsCashDeskTrue_IsCashDeskIsNull_getIsCashDeskIsNull() {

        when(fr.getIsCashDesk()).thenReturn(null);
        when(fr.getDestination()).thenReturn("XNAS");
        boolean test = filters.filterOnIsCashDesk(fr, null, "XNAS");

        assertTrue(test);
    }

    @Test
    public void filterOnIsCashDeskTrue_IsCashDeskIsNotYes_getIsCashDeskIsNotYes() {

        when(fr.getIsCashDesk()).thenReturn(false);
        when(fr.getDestination()).thenReturn("XNAS");
        boolean test = filters.filterOnIsCashDesk(fr, false, "XNAS");

        assertTrue(test);
    }

    @Test
    public void filterOnIsCashDeskFalse() {

        when(fr.getIsCashDesk()).thenReturn(false);
        when(fr.getDestination()).thenReturn("XNAS");
        boolean test = filters.filterOnIsCashDesk(fr, true, "XLON");

        assertFalse(test);
    }

    @Test
    public void filterOnIsFeePerExecutionBrokerCodeTrue_allEntriesEqual() {

        when(fr.getIsPerExecutionBrokerCode()).thenReturn(true);
        when(fr.getExecutionBrokerCode()).thenReturn("RBCB");
        boolean test = filters.filterOnIsFeePerExecutionBrokerCode(fr, true, "RBCB");

        assertTrue(test);
    }

    @Test
    public void filterOnIsFeePerExecutionBrokerCodeTrue_isPerExecutionBrokerCodeNull_feeRuleIsPerExecutionBrokerCodeIsNull() {

        when(fr.getIsPerExecutionBrokerCode()).thenReturn(null);
        when(fr.getExecutionBrokerCode()).thenReturn("RBCB");
        boolean test = filters.filterOnIsFeePerExecutionBrokerCode(fr, null, "RBCB");

        assertTrue(test);
    }

    @Test
    public void filterOnIsFeePerExecutionBrokerCodeTrue_isPerExecutionBrokerCodeNotYes_feeRuleIsPerExecutionBrokerCodeIsNull() {

        when(fr.getIsPerExecutionBrokerCode()).thenReturn(null);
        when(fr.getExecutionBrokerCode()).thenReturn("RBCB");
        boolean test = filters.filterOnIsFeePerExecutionBrokerCode(fr, false, "RBCB");

        assertTrue(test);
    }

    @Test
    public void filterOnIsFeePerExecutionBrokerCodeTrue_isPerExecutionBrokerCodeNotYes_feeRuleIsPerExecutionBrokerCodeNotYes() {

        when(fr.getIsPerExecutionBrokerCode()).thenReturn(false);
        when(fr.getExecutionBrokerCode()).thenReturn("RBCB");
        boolean test = filters.filterOnIsFeePerExecutionBrokerCode(fr, false, "RBCB");

        assertTrue(test);
    }

    @Test
    public void filterOnIsFeePerExecutionBrokerCodeFalse_notEqual() {

        when(fr.getIsPerExecutionBrokerCode()).thenReturn(false);
        when(fr.getExecutionBrokerCode()).thenReturn("RBCB");
        boolean test = filters.filterOnIsFeePerExecutionBrokerCode(fr, true, "RBCB");

        assertFalse(test);
    }


    @Test
    public void filterOnQuantityTrue_minQuantityIsNull_maxQuantityIsNull() {

        when(fr.getMinQuantity()).thenReturn(null);
        when(fr.getMaxQuantity()).thenReturn(null);
        boolean test = filters.filterOnQuantity(fr, 10);

        assertTrue(test);
    }

    @Test
    public void filterOnQuantityTrue_maxQuantityIsNull_minQuantityLessThanABSQuantity() {

        when(fr.getMinQuantity()).thenReturn(5);
        when(fr.getMaxQuantity()).thenReturn(null);
        boolean test = filters.filterOnQuantity(fr, -10);

        assertTrue(test);
    }

    @Test
    public void filterOnQuantityTrue_minQuantityIsNull_axQuantityMoreThanABSQuantity() {

        when(fr.getMinQuantity()).thenReturn(null);
        when(fr.getMaxQuantity()).thenReturn(15);
        boolean test = filters.filterOnQuantity(fr, -10);

        assertTrue(test);
    }

    @Test
    public void filterOnQuantityTrue_minQuantityLessThanABSQuantity_maxQuantityMoreThanABSQuantity() {

        when(fr.getMinQuantity()).thenReturn(5);
        when(fr.getMaxQuantity()).thenReturn(15);
        boolean test = filters.filterOnQuantity(fr, -10);

        assertTrue(test);
    }

    @Test
    public void filterOnQuantityFalse_maxQuantityIsNull_minQuantityLessThanABSQuantity() {

        when(fr.getMinQuantity()).thenReturn(15);
        when(fr.getMaxQuantity()).thenReturn(null);
        boolean test = filters.filterOnQuantity(fr, -10);

        assertFalse(test);
    }

    @Test
    public void filterOnQuantityFalse_minQuantityIsNull_maxQuantityMoreThanABSQuantity() {

        when(fr.getMinQuantity()).thenReturn(null);
        when(fr.getMaxQuantity()).thenReturn(5);
        boolean test = filters.filterOnQuantity(fr, -10);

        assertFalse(test);
    }

    @Test
    public void filterOnQuantityFalse_minQuantityLessThanABSQuantity_maxQuantityMoreThanABSQuantity() {

        when(fr.getMinQuantity()).thenReturn(15);
        when(fr.getMaxQuantity()).thenReturn(5);
        boolean test = filters.filterOnQuantity(fr, -10);

        assertFalse(test);
    }

    @Test
    public void filterOnPrincipalTrue_minPrincipalIsNull_maxPrincipalIsNull() {

        when(fr.getMinPrincipal()).thenReturn(null);
        when(fr.getMaxPrincipal()).thenReturn(null);
        boolean test = filters.filterOnPrincipal(fr, 10.0);

        assertTrue(test);
    }

    @Test
    public void filterOnPrincipalTrue_maxPrincipalIsNull_minPrincipalLessThanConsideration() {

        when(fr.getMinPrincipal()).thenReturn(5.0);
        when(fr.getMaxPrincipal()).thenReturn(null);
        boolean test = filters.filterOnPrincipal(fr, 10.1);

        assertTrue(test);
    }

    @Test
    public void filterOnPrincipalTrue_minPrincipalIsNull_maxPrincipalMoreThanConsideration() {

        when(fr.getMinPrincipal()).thenReturn(null);
        when(fr.getMaxPrincipal()).thenReturn(15.0);
        boolean test = filters.filterOnPrincipal(fr, 10.0);

        assertTrue(test);
    }

    @Test
    public void filterOnPrincipalTrue_inPrincipalLessThanConsideration_maxPrincipalMoreThanConsideration() {

        when(fr.getMinPrincipal()).thenReturn(5.0);
        when(fr.getMaxPrincipal()).thenReturn(15.0);
        boolean test = filters.filterOnPrincipal(fr, 10.0);

        assertTrue(test);
    }

    @Test
    public void filterOnPrincipalFalse_maxPrincipalIsNull_minPrincipalIsNotLessThanConsideration() {

        when(fr.getMinPrincipal()).thenReturn(15.0);
        when(fr.getMaxPrincipal()).thenReturn(null);
        boolean test = filters.filterOnPrincipal(fr, -10.1);

        assertFalse(test);
    }

    @Test
    public void filterOnPrincipalFalse_minPrincipalIsNull_maxPrincipalIsNotMoreThanConsideration() {

        when(fr.getMinPrincipal()).thenReturn(null);
        when(fr.getMaxPrincipal()).thenReturn(5.0);
        boolean test = filters.filterOnPrincipal(fr, 10.0);

        assertFalse(test);
    }

    @Test
    public void filterOnPrincipalFalse_minPrincipalLessThanConsideration_maxPrincipalMoreThanConsideration() {

        when(fr.getMinPrincipal()).thenReturn(15.0);
        when(fr.getMaxPrincipal()).thenReturn(5.0);
        boolean test = filters.filterOnPrincipal(fr, -10.0);

        assertFalse(test);
    }

    @Test
    public void filterOnIsPerExecutingBrokerAccountNameTrue_isNull() {

        when(fr.getIsPerExecutingBrokerAccountName()).thenReturn(null);
        boolean test = filters.filterOnIsPerExecutingBrokerAccountName(fr, "XLON");

        assertTrue(test);
    }

    @Test
    public void filterOnIsPerExecutingBrokerAccountNameTrue_isYes() {

        when(fr.getIsPerExecutingBrokerAccountName()).thenReturn(true);
        when(fr.getExecutingBrokerAccountName()).thenReturn("XLON");
        boolean test = filters.filterOnIsPerExecutingBrokerAccountName(fr, "XLON");

        assertTrue(test);
    }

    @Test
    public void filterOnIsPerExecutingBrokerAccountNameTrue_isYes_executingBrokerAccountNameEqual() {

        when(fr.getIsPerExecutingBrokerAccountName()).thenReturn(true);
        when(fr.getExecutingBrokerAccountName()).thenReturn("XLON");
        boolean test = filters.filterOnIsPerExecutingBrokerAccountName(fr, "XLON");

        assertTrue(test);
    }

    @Test
    public void filterOnIsPerExecutingBrokerAccountNameFalse_isNotYes_executingBrokerAccountNameEqual() {

        when(fr.getIsPerExecutingBrokerAccountName()).thenReturn(false);
        when(fr.getExecutingBrokerAccountName()).thenReturn("CDC");
        boolean test = filters.filterOnIsPerExecutingBrokerAccountName(fr, "CDC");

        assertFalse(test);
    }

    @Test
    public void filterOnIsPerExecutingBrokerAccountNameFalse_isNotYes_executingBrokerAccountNameNotEqual() {

        when(fr.getIsPerExecutingBrokerAccountName()).thenReturn(false);
        when(fr.getExecutingBrokerAccountName()).thenReturn("CDC");
        boolean test = filters.filterOnIsPerExecutingBrokerAccountName(fr, "CDC-2");

        assertFalse(test);
    }

    @Test
    public void filterOnFeeCategoryTrue_isExchangeRuleIsTrue_feeCategoryEquals() {

        when(fr.getFeeCategory()).thenReturn(FeeCategoryType.Exchange.name());
        boolean test = filters.filterOnFeeCategory(fr, FeeCategoryType.Exchange.name(), true);

        assertTrue(test);
    }

    @Test
    public void filterOnFeeCategoryTrue_isExchangeRuleIsFalse_feeCategoryNotEquals() {

        when(fr.getFeeCategory()).thenReturn(FeeCategoryType.Exchange.name());
        boolean test = filters.filterOnFeeCategory(fr, "Commission", false);

        assertTrue(test);
    }

    @Test
    public void filterOnFeeCategoryFalse_isExchangeRuleIsFalse_feeCategoryEquals() {

        when(fr.getFeeCategory()).thenReturn(FeeCategoryType.Exchange.name());
        boolean test = filters.filterOnFeeCategory(fr, FeeCategoryType.Exchange.name(), false);

        assertFalse(test);
    }

    @Test
    public void filterOnFeeCategoryFalse_isExchangeRuleIsTrue_feeCategoryNotEquals() {

        when(fr.getFeeCategory()).thenReturn(FeeCategoryType.Exchange.name());
        boolean test = filters.filterOnFeeCategory(fr, "Commission", true);

        assertFalse(test);
    }

    @Test
    public void filterOnExecutingBrokerNameTrue() {

        when(fr.getExecutingBrokerName()).thenReturn("FIX Bayou Broker");
        when(fr.getInstrument()).thenReturn("6N.CME");
        boolean test = filters.filterOnExecutingBrokerName(fr, "FIX Bayou Broker", "6N", "CME");

        assertTrue(test);
    }

    @Test
    public void filterOnExecutingBrokerNameTrue_instrumentNull() {

        when(fr.getExecutingBrokerName()).thenReturn("FIX Bayou Broker");
        when(fr.getInstrument()).thenReturn(null);
        boolean test = filters.filterOnExecutingBrokerName(fr, "FIX Bayou Broker", "6N", "EUX");

        assertTrue(test);
    }

    @Test
    public void filterOnExecutingBrokerNameFalse_tickerSymbolNotEqual() {

        when(fr.getExecutingBrokerName()).thenReturn("FIX Bayou Broker");
        when(fr.getInstrument()).thenReturn("6N.CME");
        boolean test = filters.filterOnExecutingBrokerName(fr, "FIX Bayou Broker", "NKD", "CME");

        assertFalse(test);
    }

    @Test
    public void filterOnExecutingBrokerNameFalse_tickerExchNotEqual() {

        when(fr.getExecutingBrokerName()).thenReturn("FIX Bayou Broker");
        when(fr.getInstrument()).thenReturn("6N.CME");
        boolean test = filters.filterOnExecutingBrokerName(fr, "FIX Bayou Broker", "6N", "EUX");

        assertFalse(test);
    }

    @Test
    public void filterOnExecutingBrokerNameFalse_executingBrokerNameNull() {

        when(fr.getExecutingBrokerName()).thenReturn(null);
        when(fr.getInstrument()).thenReturn("6N.CME");
        boolean test = filters.filterOnExecutingBrokerName(fr, "FIX Bayou Broker", "6N", "EUX");

        assertFalse(test);
    }

    @Test
    public void filterOnSkipSECTrue_assetTypeNotO_underlyingTypeNotF() {

        when(fr.getFeeSubCategory()).thenReturn("SEC");
        when(fr.getInstrument()).thenReturn(null);
        //assetType invalid input: "O"
        //underlyingType invalid input: "F","I"
        //getFeeSubCategory invalid input: "SEC"
        boolean test = filters.filterOnSkipSEC(fr, "A", "A");

        assertTrue(test);
    }

    @Test
    public void filterOnSkipSECTrue_assetTypeNotO_underlyingTypeNull() {

        when(fr.getFeeSubCategory()).thenReturn("SEC");
        when(fr.getInstrument()).thenReturn(null);
        //assetType invalid input: "O"
        //underlyingType invalid input: "F","I"
        //getFeeSubCategory invalid input: "SEC"
        boolean test = filters.filterOnSkipSEC(fr, null, "A");

        assertTrue(test);
    }

    @Test
    public void filterOnSkipSECFalse_assetTypeIsO_underlyingTypeIsF() {

        when(fr.getFeeSubCategory()).thenReturn("SEC");
        when(fr.getInstrument()).thenReturn(null);
        //assetType invalid input: "O"
        //underlyingType invalid input: "F","I"
        //getFeeSubCategory invalid input: "SEC"
        boolean test = filters.filterOnSkipSEC(fr, "F", "O");

        assertFalse(test);
    }

    @Test
    public void filterOnSkipSECFalse_assetTypeIsO_underlyingTypeIsI() {

        when(fr.getFeeSubCategory()).thenReturn("SEC");
        when(fr.getInstrument()).thenReturn(null);
        //assetType invalid input: "O"
        //underlyingType invalid input: "F","I"
        //getFeeSubCategory invalid input: "SEC"
        boolean test = filters.filterOnSkipSEC(fr, "I", "O");

        assertFalse(test);
    }

    @Test
    public void filterOnUnderlyingTypeTrue_underlyingTypeEqual() {

        when(fr.getUnderlyingType()).thenReturn("A");
        boolean test = filters.filterOnUnderlyingType(fr, "A");

        assertTrue(test);
    }

    @Test
    public void filterOnUnderlyingTypeTrue_feeRuleUnderlyingTypeNull() {

        when(fr.getUnderlyingType()).thenReturn(null);
        boolean test = filters.filterOnUnderlyingType(fr, "A");

        assertTrue(test);
    }

    @Test
    public void filterOnUnderlyingTypeTrue_underlyingTypeNotNull() {

        when(fr.getUnderlyingType()).thenReturn("A");
        boolean test = filters.filterOnUnderlyingType(fr, null);

        assertTrue(test);
    }

    @Test
    public void filterOnUnderlyingTypeFalse_underlyingTypeNotEqual() {

        when(fr.getUnderlyingType()).thenReturn("B");
        boolean test = filters.filterOnUnderlyingType(fr, "A");

        assertFalse(test);
    }

    @Test
    public void isCommissionAllInStatusTrue() {

        //filterOnCommissionAllInFeeLevel() inputs

        when(frc.getAccountId()).thenReturn("ACC023");
        when(frc.getAllInExchangeMIC()).thenReturn("BY.XNAS");

        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.YEAR, 2020);
        myCalendar.set(Calendar.MONTH, 4);
        myCalendar.set(Calendar.DAY_OF_MONTH, 23);
        Date theDateBefore = myCalendar.getTime();

        Calendar myCalendar2 = Calendar.getInstance();
        myCalendar2.set(Calendar.YEAR, 2020);
        myCalendar2.set(Calendar.MONTH, 6);
        myCalendar2.set(Calendar.DAY_OF_MONTH, 23);
        Date tradeDateAfter = myCalendar2.getTime();

        Calendar myCalendar3 = Calendar.getInstance();
        myCalendar3.set(Calendar.YEAR, 2020);
        myCalendar3.set(Calendar.MONTH, 5);
        myCalendar3.set(Calendar.DAY_OF_MONTH, 23);
        Date tradeTime = myCalendar3.getTime();
        //2020-04-23
        when(frc.getDateFrom()).thenReturn(theDateBefore);
        //2020-06-23
        when(frc.getDateTo()).thenReturn(tradeDateAfter);
        //2020-05-23
        boolean test = filters.filterOnCommissionAllInFeeLevel(frc, "ACC023", "BY.XNAS", tradeTime);

        //isCommissionAllInStatus() inputs

        Calendar myCalendar4 = Calendar.getInstance();
        myCalendar4.set(Calendar.YEAR, 2020);
        myCalendar4.set(Calendar.MONTH, 5);
        myCalendar4.set(Calendar.DAY_OF_MONTH, 23);
        Date tradeTime2 = myCalendar4.getTime();

        l.add(frc);

        boolean test2 = filters.isCommissionAllInStatus(l, "ACC023", "BY.XNAS", tradeTime2);

        assertTrue(test2);
    }

    @Test
    public void isCommissionAllInStatusFalse_accountIdNotEqual() {

        //filterOnCommissionAllInFeeLevel() inputs

        when(frc.getAccountId()).thenReturn("ACC987");
        when(frc.getAllInExchangeMIC()).thenReturn("BY.XNAS");

        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.YEAR, 2020);
        myCalendar.set(Calendar.MONTH, 4);
        myCalendar.set(Calendar.DAY_OF_MONTH, 23);
        Date theDateBefore = myCalendar.getTime();

        Calendar myCalendar2 = Calendar.getInstance();
        myCalendar2.set(Calendar.YEAR, 2020);
        myCalendar2.set(Calendar.MONTH, 6);
        myCalendar2.set(Calendar.DAY_OF_MONTH, 23);
        Date tradeDateAfter = myCalendar2.getTime();

        Calendar myCalendar3 = Calendar.getInstance();
        myCalendar3.set(Calendar.YEAR, 2020);
        myCalendar3.set(Calendar.MONTH, 5);
        myCalendar3.set(Calendar.DAY_OF_MONTH, 23);
        Date tradeTime = myCalendar3.getTime();
        //2020-04-23
        when(frc.getDateFrom()).thenReturn(theDateBefore);
        //2020-06-23
        when(frc.getDateTo()).thenReturn(tradeDateAfter);
        //2020-05-23
        boolean test = filters.filterOnCommissionAllInFeeLevel(frc, "ACC023", "BY.XNAS", tradeTime);

        //isCommissionAllInStatus() inputs

        Calendar myCalendar4 = Calendar.getInstance();
        myCalendar4.set(Calendar.YEAR, 2020);
        myCalendar4.set(Calendar.MONTH, 5);
        myCalendar4.set(Calendar.DAY_OF_MONTH, 23);
        Date tradeTime2 = myCalendar4.getTime();

        l.add(frc);

        boolean test2 = filters.isCommissionAllInStatus(l, "ACC023", "BY.XNAS", tradeTime2);

        assertFalse(test2);
    }

    @Test
    public void filterOnInstrumentAndExchangeMatchTrue() {

        when(fr.getInstrument()).thenReturn("6N.CME");
        boolean test = filters.filterOnInstrumentAndExchangeMatch(fr, "6N", "CME");

        assertTrue(test);
    }

    @Test
    public void filterOnInstrumentAndExchangeMatchFalse_tickerSymbolNotEqual() {

        when(fr.getInstrument()).thenReturn("6N.CME");
        boolean test = filters.filterOnInstrumentAndExchangeMatch(fr, "6M", "CME");

        assertFalse(test);
    }

    @Test
    public void filterOnInstrumentAndExchangeMatchFalse_tickerExchNotEqual() {

        when(fr.getInstrument()).thenReturn("6N.CME");
        boolean test = filters.filterOnInstrumentAndExchangeMatch(fr, "6N", "CMEE");

        assertFalse(test);
    }

    @Test
    public void isInvalidRequestDataTrue_allEntriesNull() {

        when(fcr.getQuantity()).thenReturn(null);
        when(fcr.getPrice()).thenReturn(null);
        when(fcr.getAssetType()).thenReturn(null);
        when(fcr.getFullExecutingBrokerName()).thenReturn(null);
        when(fcr.getAssetType()).thenReturn(null);
        when(fcr.getSymbolCurrency()).thenReturn(null);
        boolean test = filters.isInvalidRequestData(fcr);

        assertTrue(test);
    }
    @Test
    public void isInvalidRequestDataTrue_symbolCCYIsNull() {

        when(fcr.getQuantity()).thenReturn(10);
        when(fcr.getPrice()).thenReturn(56.8);
        when(fcr.getAssetType()).thenReturn(AssetType.F.name());
        when(fcr.getFullExecutingBrokerName()).thenReturn("FIX Bayou Broker");
        when(fcr.getAssetType()).thenReturn(AssetType.S.name());
        when(fcr.getSymbolCurrency()).thenReturn(null);
        boolean test = filters.isInvalidRequestData(fcr);

        assertTrue(test);
    }
    @Test
    public void isInvalidRequestDataTrue_assetTypeNull() {

        when(fcr.getQuantity()).thenReturn(10);
        when(fcr.getPrice()).thenReturn(56.8);
        when(fcr.getAssetType()).thenReturn(AssetType.F.name());
        when(fcr.getFullExecutingBrokerName()).thenReturn("FIX Bayou Broker");
        when(fcr.getAssetType()).thenReturn(null);
        when(fcr.getSymbolCurrency()).thenReturn(null);
        boolean test = filters.isInvalidRequestData(fcr);

        assertTrue(test);
    }

    @Test
    public void isInvalidRequestDataTrue_fullExecutingBrokerNameIsNull() {

        when(fcr.getQuantity()).thenReturn(10);
        when(fcr.getPrice()).thenReturn(56.8);
        when(fcr.getAssetType()).thenReturn(AssetType.F.name());
        when(fcr.getFullExecutingBrokerName()).thenReturn(null);
        when(fcr.getAssetType()).thenReturn(null);
        when(fcr.getSymbolCurrency()).thenReturn(null);
        boolean test = filters.isInvalidRequestData(fcr);

        assertTrue(test);
    }
    @Test
    public void isInvalidRequestDataTrue_assetTypeIsNull() {

        when(fcr.getQuantity()).thenReturn(10);
        when(fcr.getPrice()).thenReturn(56.8);
        when(fcr.getAssetType()).thenReturn(null);
        when(fcr.getFullExecutingBrokerName()).thenReturn(null);
        when(fcr.getAssetType()).thenReturn(null);
        when(fcr.getSymbolCurrency()).thenReturn(null);
        boolean test = filters.isInvalidRequestData(fcr);

        assertTrue(test);
    }
    @Test
    public void isInvalidRequestDataTrue_priceIsNull() {

        when(fcr.getQuantity()).thenReturn(10);
        when(fcr.getPrice()).thenReturn(null);
        when(fcr.getAssetType()).thenReturn(null);
        when(fcr.getFullExecutingBrokerName()).thenReturn(null);
        when(fcr.getAssetType()).thenReturn(null);
        when(fcr.getSymbolCurrency()).thenReturn(null);
        boolean test = filters.isInvalidRequestData(fcr);

        assertTrue(test);
    }

    @Test
    public void isInvalidRequestDataFalse_allEntriesValid() {

        when(fcr.getQuantity()).thenReturn(10);
        when(fcr.getPrice()).thenReturn(56.8);
        when(fcr.getAssetType()).thenReturn(AssetType.F.name());
        when(fcr.getFullExecutingBrokerName()).thenReturn("FIX Bayou Broker");
        when(fcr.getAssetType()).thenReturn(AssetType.S.name());
        when(fcr.getSymbolCurrency()).thenReturn(CurrencyType.AUD.name());
        boolean test = filters.isInvalidRequestData(fcr);

        assertFalse(test);
    }

    @Test
    public void isInvalidRequestDataTrue_allocationTypeContains() {

        when(fcr.getQuantity()).thenReturn(10);
        when(fcr.getPrice()).thenReturn(56.8);
        when(fcr.getAssetType()).thenReturn(AssetType.F.name());
        when(fcr.getFullExecutingBrokerName()).thenReturn("FIX Bayou Broker");
        when(fcr.getSymbolCurrency()).thenReturn(CurrencyType.AUD.name());

        when(fcr.getAllocationType()).thenReturn(AllocationExcludedType.PARI_PASSU_CROSS.name());

        boolean test = filters.isInvalidRequestData(fcr);

        assertTrue(test);
    }



    @Test
    public void isInvalidRequestDataTrue_assetTypeNotEqual() {

        when(fcr.getQuantity()).thenReturn(10);
        when(fcr.getPrice()).thenReturn(56.8);
        when(fcr.getAssetType()).thenReturn("W");
        when(fcr.getFullExecutingBrokerName()).thenReturn("FIX Bayou Broker");
        when(fcr.getSymbolCurrency()).thenReturn(CurrencyType.AUD.name());

        when(fcr.getAllocationType()).thenReturn(null);

        boolean test = filters.isInvalidRequestData(fcr);

        assertTrue(test);
    }
    @Test
    public void isInvalidRequestDataFalse_assetTypeEqual() {

        when(fcr.getQuantity()).thenReturn(10);
        when(fcr.getPrice()).thenReturn(56.8);
        when(fcr.getAssetType()).thenReturn(AssetType.X.name());
        when(fcr.getFullExecutingBrokerName()).thenReturn("FIX Bayou Broker");
        when(fcr.getSymbolCurrency()).thenReturn(CurrencyType.AUD.name());

        when(fcr.getAllocationType()).thenReturn(null);

        boolean test = filters.isInvalidRequestData(fcr);

        assertFalse(test);
    }
    @Test
    public void isInvalidRequestDataFalse_allocationTypeNotContains() {

        when(fcr.getQuantity()).thenReturn(10);
        when(fcr.getPrice()).thenReturn(56.8);
        when(fcr.getAssetType()).thenReturn(AssetType.F.name());
        when(fcr.getFullExecutingBrokerName()).thenReturn("FIX Bayou Broker");
        when(fcr.getSymbolCurrency()).thenReturn(CurrencyType.AUD.name());

        when(fcr.getAllocationType()).thenReturn("PARI_PASSU");

        boolean test = filters.isInvalidRequestData(fcr);

        assertFalse(test);
    }
    @Test
    public void isInvalidRequestDataFalse_allocationTypeIsNull() {

        when(fcr.getQuantity()).thenReturn(10);
        when(fcr.getPrice()).thenReturn(56.8);
        when(fcr.getAssetType()).thenReturn(AssetType.F.name());
        when(fcr.getFullExecutingBrokerName()).thenReturn("FIX Bayou Broker");
        when(fcr.getSymbolCurrency()).thenReturn(CurrencyType.AUD.name());

        when(fcr.getAllocationType()).thenReturn(null);

        boolean test = filters.isInvalidRequestData(fcr);

        assertFalse(test);
    }


    @Test
    public void isInvalidRequestDataTrue_currencyTypeNotContains() {

        when(fcr.getQuantity()).thenReturn(10);
        when(fcr.getPrice()).thenReturn(56.8);
        when(fcr.getAssetType()).thenReturn(AssetType.F.name());
        when(fcr.getFullExecutingBrokerName()).thenReturn("FIX Bayou Broker");
        when(fcr.getAssetType()).thenReturn(AssetType.S.name());
        when(fcr.getSymbolCurrency()).thenReturn("LEI");
        boolean test = filters.isInvalidRequestData(fcr);

        assertTrue(test);
    }

    @Test
    public void isInvalidRequestDataFalse_currencyTypeContains() {

        when(fcr.getQuantity()).thenReturn(10);
        when(fcr.getPrice()).thenReturn(56.8);
        when(fcr.getAssetType()).thenReturn(AssetType.F.name());
        when(fcr.getFullExecutingBrokerName()).thenReturn("FIX Bayou Broker");
        when(fcr.getAssetType()).thenReturn(AssetType.S.name());
        when(fcr.getSymbolCurrency()).thenReturn(CurrencyType.CHF.name());
        boolean test = filters.isInvalidRequestData(fcr);

        assertFalse(test);
    }
}