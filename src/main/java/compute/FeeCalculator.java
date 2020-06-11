package compute;

import compute.engine.impl.FeeCalculatorHelper;
import compute.engine.impl.FeeComputation;
import compute.filters.Filters;
import model.entities.*;
import model.types.CurrencyType;
import model.types.FeeLevelType;
import providers.IAccountProvider;
import providers.IExternalTempProvider;
import providers.IFeeRulesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Fee Compute for a specific input
 */
public class FeeCalculator {
    // service providers
    final IAccountProvider accountProvider;
    final IFeeRulesProvider feeRulesProvider;
    final IExternalTempProvider externalTempProvider;

    // initialize
    final FeeCalculatorHelper fch;
    final Filters filters;
    final FeeComputation fc;

    // host order id
    String hostOrderId;
    // define symbol details
    String tickerSymbol;
    String tickerExch;
    String tickerRoot;
    // status charged by owner
    Boolean isChargedPerOwner = false;
    // default exchange
    String defaultFeeExchange;
    // ALL-IN commission
    Boolean isCommissionAllInFee = false;
    // Fee calculation variables
    Double consideration;

    /**
     * Initialize constructor with providers
     *
     * @param accountProvider
     * @param feeRulesProvider
     * @param externalTempProvider
     */
    public FeeCalculator(IAccountProvider accountProvider,
                         IFeeRulesProvider feeRulesProvider,
                         IExternalTempProvider externalTempProvider) {
        this.accountProvider = accountProvider;
        this.feeRulesProvider = feeRulesProvider;
        this.externalTempProvider = externalTempProvider;

        fch = new FeeCalculatorHelper();
        fc = new FeeComputation();
        filters = new Filters();
    }


    /**
     * @param fcr
     * @return
     */
    public List<FeeCalculationResponse> getFeePerTrade(FeeCalculationRequest fcr) {
        List<FeeCalculationResponse> feeCalculationResponses = new ArrayList<>();

        // preliminary check against input
        if (filters.isInvalidRequestData(fcr)) {
            return null;
        }
        // adjust input data to be as expected
        if (manipulateRequestData(fcr) == false) {
            return null;
        }

        // get account details
        Account account = accountProvider.get(fcr.getAccountId());
        List<FeeRuleComm> feeRuleComms = feeRulesProvider.getByAccount(fcr.getAccountId());

        // handle billable flags
        Billable billable = fch.handleBillableFlags(fcr, account);
        isChargedPerOwner = billable.getIsChargedPerOwner();

        // check if ALL IN comm status
        isCommissionAllInFee = filters.isCommissionAllInStatus(feeRuleComms, fcr.getAccountId(), fcr.getExchangeMIC(), fcr.getTradeTime());

        String oldHostOrderId = null;
        if (hostOrderId != null) {
            oldHostOrderId = externalTempProvider.get(hostOrderId, fcr.getAccountId(), fcr.getTradeTime()).getHostOrderId();
        }

        // -- FEE BASE ALLOCATION -- //
        if (billable.getBaseFeeCharge()) {
            feeCalculationResponses.addAll(fc.computeFeeBaseCharge(feeRulesProvider, fcr,
                    account, FeeLevelType.Base.name(), consideration, isChargedPerOwner, isCommissionAllInFee,
                    defaultFeeExchange, tickerSymbol, tickerRoot, tickerExch, oldHostOrderId));
        }

        if (billable.getCommFeeCharge()) {
            feeCalculationResponses.addAll(fc.computeFeeCommissionCharge(feeRulesProvider, fcr, account,
                    isCommissionAllInFee, defaultFeeExchange, consideration, tickerSymbol, tickerExch, oldHostOrderId));
        }

        if (billable.getCommOutsideFeeCharge()) {
            feeCalculationResponses.addAll(fc.computeFeeOutsideCommCharge(fcr, consideration));
        }

        // save into external temp if we had a special host order id (PER_TICKET)
        if (hostOrderId != null) {
            externalTempProvider.add(hostOrderId, fcr.getAccountId(), fcr.getTradeTime());
        }

        return feeCalculationResponses;
    }


    /**
     * Manipulate Request Data
     *
     * @param fcr
     */
    private Boolean manipulateRequestData(FeeCalculationRequest fcr) {
        boolean isPassed = true;
        // if TicketId is not null we need to construct the HostOrderId
        // adjust symbol names/root/exch as per asset type
        String ticker = fcr.getTicker();

        switch (fcr.getAssetType()) {
            case "S":
                if (fcr.getTicketId() != null) {
                    hostOrderId = fch.buildHostOrderId(fcr, "E-");
                }

                tickerSymbol = ticker.substring(0, ticker.lastIndexOf("."));
                tickerExch = ticker.substring(ticker.lastIndexOf(".") + 1);

                break;
            case "O":
                if (fcr.getTicketId() != null) {
                    hostOrderId = fch.buildHostOrderId(fcr, "O-");
                }

                if (fcr.getUnderlyingTicker() == null || fcr.getUnderlyingTicker().equals("")) {
                    fcr.setUnderlyingTicker(ticker);
                }

                tickerSymbol = fcr.getUnderlyingTicker().substring(0, ticker.lastIndexOf("."));
                tickerExch = "OCC";

                break;
            case "F":
                if (fcr.getTicketId() != null) {
                    hostOrderId = fch.buildHostOrderId(fcr, "F-");
                }

                tickerSymbol = ticker.substring(0, ticker.lastIndexOf("."));
                tickerRoot = ticker.substring(0, ticker.indexOf("/"));
                tickerExch = ticker.substring(ticker.indexOf('.') + 1);

                break;
            default:
                isPassed = false;
                break;
        }

        if (isPassed == false) {
            return isPassed;
        }

        // compute consideration
        consideration = Math.abs(fcr.getQuantity()) *
                fcr.getPrice() *
                (fcr.getContractMultiplier() == null ? 1 : fcr.getContractMultiplier()) *
                (fcr.getCcyMultiplier() == null ? 1.0 : fcr.getCcyMultiplier());

        // adjust currency name and price if GBX
        if (fcr.getSymbolCurrency().equals(CurrencyType.GBX.name())) {
            fcr.setSymbolCurrency(CurrencyType.GBP.name());
            fcr.setPrice(fcr.getPrice() / 100);
        }

        // adjust executing broker name based on trade type
        if (fcr.getIsDoneAway() != null && fcr.getIsDoneAway() == true) {
            fcr.setFullExecutingBrokerName("DA_" + fcr.getFullExecutingBrokerName());
        }

        // adjust executing broker name based on trade type
        if (fcr.getIsDropCopy() != null && fcr.getIsDropCopy() == true) {
            fcr.setFullExecutingBrokerName("DC_" + fcr.getFullExecutingBrokerName());
        }

        // create default fee exchange
        defaultFeeExchange = fcr.getShortExecutingBrokerName() + ".NO_EXCH";

        if (fcr.getShortExecutingBrokerName() == null) {
            defaultFeeExchange = null;
        }

        return isPassed;
    }

}
