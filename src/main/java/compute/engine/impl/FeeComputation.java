package compute.engine.impl;

import compute.engine.IFeeComputation;
import compute.engine.IFeeRulesRetriever;
import model.entities.Account;
import model.entities.FeeCalculationRequest;
import model.entities.FeeCalculationResponse;
import model.entities.FeeRule;
import model.types.ExternalCommType;
import model.types.FeeLevelType;
import model.types.FeeRuleType;
import providers.IFeeRulesProvider;

import java.util.ArrayList;
import java.util.List;

public class FeeComputation implements IFeeComputation {

    IFeeRulesRetriever fr = new FeeRulesRetriever();

    @Override
    public List<FeeCalculationResponse> computeFeeCommissionCharge(IFeeRulesProvider feeRulesProvider,
                                                                   FeeCalculationRequest fcr,
                                                                   Account account,
                                                                   Boolean isCommissionAllInFee,
                                                                   String defaultFeeExchange,
                                                                   Double consideration,
                                                                   String tickerSymbol,
                                                                   String tickerExch,
                                                                   String oldHostOrderId) {

        List<FeeCalculationResponse> feeCalculationResponses = new ArrayList<>();

        // STEP 1 - SEARCH COMMISSION RULES
        List<FeeRule> feeRuleList = fr.retrieveCommissionRules(feeRulesProvider,
                fcr,
                isCommissionAllInFee,
                defaultFeeExchange,
                consideration,
                tickerSymbol,
                tickerExch);
        // STEP 2 - compute fee for COMMISSION
        feeCalculationResponses.addAll(computeFees(fcr, account, feeRuleList, FeeLevelType.Firm.name(),
                consideration, false, false, oldHostOrderId));

        return feeCalculationResponses;
    }


    /**
     * Compute Fee Base charges
     * Exchange vs Non-Exchange charges
     *
     * @param fcr
     * @param account
     * @return
     */
    @Override
    public List<FeeCalculationResponse> computeFeeBaseCharge(IFeeRulesProvider feeRulesProvider,
                                                             FeeCalculationRequest fcr,
                                                             Account account,
                                                             String feeLevel,
                                                             Double consideration,
                                                             Boolean isChargedPerOwner,
                                                             Boolean isCommissionAllInFee,
                                                             String defaultFeeExchange,
                                                             String tickerSymbol,
                                                             String tickerRoot,
                                                             String tickerExch,
                                                             String oldHostOrderId) {
        List<FeeCalculationResponse> feeCalculationResponses = new ArrayList<>();

        // STEP 1 - SEARCH BASE (NON-EXCHANGE RULES)
        List<FeeRule> feeNonExchangeRules = fr.retrieveNonExchangeBaseRules(feeRulesProvider,
                fcr,
                consideration,
                false,
                defaultFeeExchange,
                tickerSymbol,
                tickerRoot,
                tickerExch);
        // compute fee for NON-EXCHANGE
        feeCalculationResponses.addAll(computeFees(fcr, account, feeNonExchangeRules, feeLevel, consideration, isChargedPerOwner, isCommissionAllInFee, oldHostOrderId));

        // STEP 2 - SEARCH BASE (EXCHANGE RULES)
        List<FeeRule> feeExchangeRules = fr.retrieveExchangeBaseRules(feeRulesProvider,
                fcr,
                consideration,
                true,
                defaultFeeExchange,
                tickerSymbol,
                tickerRoot,
                tickerExch);
        // compute fee for NON-EXCHANGE
        feeCalculationResponses.addAll(computeFees(fcr, account, feeExchangeRules, feeLevel, consideration, isChargedPerOwner, isCommissionAllInFee, oldHostOrderId));

        return feeCalculationResponses;
    }

    /**
     * Compute fees outside of procedure
     *
     * @param fcr
     * @return
     */
    @Override
    public List<FeeCalculationResponse> computeFeeOutsideCommCharge(FeeCalculationRequest fcr, Double consideration) {
        List<FeeCalculationResponse> applicationResults = new ArrayList<>();
        // reset amount
        Double amount = 0.0;

        if (fcr.getExternalCommType().equals(ExternalCommType.PER_TICKET.name())) {
            amount += fcr.getExternalCommRate();
        } else if (fcr.getExternalCommType().equals(ExternalCommType.PER_UNIT.name())) {
            Double amountOutsideCommCurrent = fcr.getExternalCommRate() * Math.abs(fcr.getQuantity());
            amount += amountOutsideCommCurrent;
        } else if (fcr.getExternalCommType().equals(ExternalCommType.BPS.name())) {
            if (fcr.getExternalCommRate() > 1) {
                fcr.setExternalCommRate(fcr.getExternalCommRate() / 10000);
            }

            Double amountOutsideBasisCommCurrent = fcr.getExternalCommRate() * consideration;

            amount += amountOutsideBasisCommCurrent;
        }

        if (amount != 0) {
            if (fcr.getSymbolCurrency() != null) {
                FeeCalculationResponse applicationResult = createAppResult(fcr, FeeLevelType.Firm.name(), fcr.getExternalCommRate(), FeeRuleType.COMMISSION.name(), fcr.getExternalCommType(), fcr.getSymbolCurrency(), amount);

                applicationResults.add(applicationResult);
            }
        }

        return applicationResults;
    }

    /**
     * Compute fees based on input data
     *
     * @param fcr
     * @param account
     * @param feeRules
     * @return
     */
    @Override
    public List<FeeCalculationResponse> computeFees(
            FeeCalculationRequest fcr,
            Account account,
            List<FeeRule> feeRules,
            String feeLevel,
            Double consideration,
            Boolean isChargedPerOwner,
            Boolean isCommissionAllInFee,
            String oldHostOrderId) {
        List<FeeCalculationResponse> feeCalculationResponses = new ArrayList<>();
        for (FeeRule feeRule : feeRules) {
            Double amount = 0.0;

            Double maxBPValue = 0.0;
            Double amountCurrent = 0.0;
            Double amountBasisCurrent = 0.0;
            Double currentComputeRate = 0.0;

            Double flatFlee = feeRule.getFlatFee();
            Double feePerContract = feeRule.getFeePerContract();
            Double feePerContractMin = feeRule.getFeePerContractMin();
            Double feePerContractMaxBP = feeRule.getFeePerContractMaxBP();
            Double basisPoints = feeRule.getBasisPoints();
            Double basisPointsFeeMax = feeRule.getBasisPointsFeeMax();
            Double basisPointsFeeMin = feeRule.getBasisPointsFeeMin();

            Boolean isAppliedPerExecution = feeRule.getIsAppliedPerExecution();
            Boolean isAppliedPerTicket = feeRule.getIsAppliedPerTicket();

            if (flatFlee != null) {
                amount += flatFlee;
                currentComputeRate = flatFlee;

            }

            if (feePerContract != null) {
                if (isAppliedPerExecution != null && isAppliedPerExecution == true) {
                    amountCurrent = feePerContract;
                } else {
                    amountCurrent = feePerContract * Math.abs(fcr.getQuantity());
                }

                boolean foundValue = false;
                if (feePerContractMin != null && amountCurrent < feePerContractMin) {
                    amount += feePerContractMin;

                    feePerContract = feePerContractMin;

                    foundValue = true;
                } else if (feePerContractMaxBP != null) {
                    maxBPValue = feePerContractMaxBP * consideration;

                    if (amountCurrent > maxBPValue) {
                        amount += maxBPValue;
                        foundValue = true;
                    }
                }

                if (foundValue == false) {
                    amount += amountCurrent;
                }
                currentComputeRate = feePerContract;

                if (feeRule.getIsRoundedUp() != null) {
                    amount = Math.ceil(amount);
                }
            }

            if (basisPoints != null) {
                if (isAppliedPerExecution != null && isAppliedPerExecution == true) {
                    amountBasisCurrent = basisPoints;
                } else {
                    amountBasisCurrent = basisPoints * consideration;
                }
                boolean foundValue = false;

                if (basisPointsFeeMax != null && amountBasisCurrent > basisPointsFeeMax) {
                    amount += basisPointsFeeMax;
                    foundValue = true;
                } else if (basisPointsFeeMin != null && amountBasisCurrent < basisPointsFeeMin) {
                    amount += basisPointsFeeMin;
                    foundValue = true;
                }

                if (foundValue == false) {
                    amount += amountBasisCurrent;
                }

                currentComputeRate = basisPoints;

                if (feeRule.getIsRoundedUp() != null) {
                    amount = Math.ceil(amount);
                }
            }

            if (feeRule.getUnderlyingType() != null) {
                if (!feeRule.getUnderlyingType().equals(fcr.getUnderlyingType())) {
                    System.err.println("Skipped rule: " + feeRule.getRuleId() + " due to mismatch for underlying type");
                    continue;
                }
            }

            if (isAppliedPerTicket != null && isAppliedPerTicket == true) {
                // If current Trade is the first trade for this Ticket, apply Rule, otherwise skip.
                if (oldHostOrderId == null) {
                    // COMMISSION LEVEL
                    computeFirmLevel(fcr, feeLevel, feeCalculationResponses, feeRule, amount, currentComputeRate);
                    // BASE LEVEL
                    computationBaseLevel(fcr, account, feeLevel, isChargedPerOwner, isCommissionAllInFee, feeCalculationResponses, feeRule, amount, currentComputeRate);
                }
            } else {
                // COMMISSION LEVEL
                computeFirmLevel(fcr, feeLevel, feeCalculationResponses, feeRule, amount, currentComputeRate);
                // BASE LEVEL
                computationBaseLevel(fcr, account, feeLevel, isChargedPerOwner, isCommissionAllInFee, feeCalculationResponses, feeRule, amount, currentComputeRate);
            }
        }

        return feeCalculationResponses;
    }

    private void computeFirmLevel(FeeCalculationRequest fcr, String feeLevel, List<FeeCalculationResponse> feeCalculationResponses, FeeRule feeRule, Double amount, Double currentComputeRate) {
        if (feeLevel.equals(FeeLevelType.Firm.name())) {
            if (feeRule.getFeeCurrencyName() != null) {
                FeeCalculationResponse feeCalculationResponse = createAppResult(fcr, feeLevel, currentComputeRate, feeRule.getFeeCategory(), feeRule.getFeeSubCategory(), feeRule.getFeeCurrencyName(), amount);

                if (feeCalculationResponse != null) {
                    feeCalculationResponses.add(feeCalculationResponse);
                }
            }
        }
    }

    private void computationBaseLevel(FeeCalculationRequest fcr, Account account, String feeLevel, Boolean isChargedPerOwner, Boolean isCommissionAllInFee, List<FeeCalculationResponse> feeCalculationResponses, FeeRule feeRule, Double amount, Double currentComputeRate) {
        if (feeLevel.equals(FeeLevelType.Base.name())) {
            if (isChargedPerOwner) {
                if (feeRule.getOwnersList() != null && feeRule.getOwnersList().contains(account.getSource())) {
                    if (feeRule.getFeeCurrencyName() != null) {
                        FeeCalculationResponse feeCalculationResponse = createAppResult(fcr, feeLevel, currentComputeRate, feeRule.getFeeCategory(), feeRule.getFeeSubCategory(), feeRule.getFeeCurrencyName(), amount);

                        if (feeCalculationResponse != null) {
                            feeCalculationResponses.add(feeCalculationResponse);
                        }
                        if (isCommissionAllInFee != false) {
                            FeeCalculationResponse feeCalculationResponse2 = createAppResult(fcr, feeLevel, -currentComputeRate, feeRule.getFeeCategory(), feeRule.getFeeSubCategory(), feeRule.getFeeCurrencyName(), -amount);
                            if (feeCalculationResponse2 != null) {
                                feeCalculationResponses.add(feeCalculationResponse2);
                            }
                        }
                    }
                }
            } else {
                if (feeRule.getFeeCurrencyName() != null) {
                    FeeCalculationResponse feeCalculationResponse = createAppResult(fcr, feeLevel, currentComputeRate, feeRule.getFeeCategory(), feeRule.getFeeSubCategory(), feeRule.getFeeCurrencyName(), amount);
                    if (feeCalculationResponse != null) {
                        feeCalculationResponses.add(feeCalculationResponse);
                    }

                    if (isCommissionAllInFee != false) {
                        FeeCalculationResponse feeCalculationResponse2 = createAppResult(fcr, feeLevel, -currentComputeRate, feeRule.getFeeCategory(), feeRule.getFeeSubCategory(), feeRule.getFeeCurrencyName(), -amount);
                        if (feeCalculationResponse2 != null) {
                            feeCalculationResponses.add(feeCalculationResponse2);
                        }
                    }
                }
            }
        }
    }

    private FeeCalculationResponse createAppResult(FeeCalculationRequest fcr, String feeLevel, Double currentComputeRate, String feeCategory, String feeSubCategory, String currencyName, Double amount) {
        FeeCalculationResponse feeCalculationResponse = new FeeCalculationResponse();
        feeCalculationResponse.setOrderExecutionId(fcr.getOrderExecutionId());
        feeCalculationResponse.setFeeLevel(feeLevel);
        feeCalculationResponse.setFeeType(feeCategory);
        feeCalculationResponse.setFeeCategory(feeSubCategory);
        feeCalculationResponse.setCurrency(currencyName);
        feeCalculationResponse.setAmount(amount);
        feeCalculationResponse.setCommRate(currentComputeRate);

        return feeCalculationResponse;
    }


}
