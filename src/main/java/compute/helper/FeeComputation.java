package compute.helper;

import compute.model.FeeCalculationRequest;
import model.entities.Account;
import model.entities.FeeApplicationResult;
import model.entities.FeeRule;
import model.types.ExternalCommType;
import model.types.FeeLevelType;
import model.types.FeeRuleType;
import providers.IFeeRulesProvider;

import java.util.ArrayList;
import java.util.List;

public class FeeComputation {

    IFeeRulesRetriever fr = new FeeRulesRetriever();

    public List<FeeApplicationResult> computeFeeCommissionCharge(IFeeRulesProvider feeRulesProvider,
                                                                 FeeCalculationRequest fcr,
                                                                 Account account,
                                                                 Boolean isCommissionAllInFee,
                                                                 String defaultFeeExchange,
                                                                 Double consideration,
                                                                 String tickerSymbol,
                                                                 String tickerExch,
                                                                 String oldHostOrderId) {

        List<FeeApplicationResult> feeApplicationResults = new ArrayList<>();

        // STEP 1 - SEARCH COMMISSION RULES
        List<FeeRule> feeRuleList = fr.retrieveCommissionRules(feeRulesProvider,
                fcr,
                isCommissionAllInFee,
                defaultFeeExchange,
                consideration,
                tickerSymbol,
                tickerExch);
        // STEP 2 - compute fee for COMMISSION
        feeApplicationResults.addAll(computeFees(fcr, account, feeRuleList, FeeLevelType.Firm.name(),
                consideration, false, false, oldHostOrderId));

        return feeApplicationResults;
    }


    /**
     * Compute Fee Base charges
     * Exchange vs Non-Exchange charges
     *
     * @param fcr
     * @param account
     * @return
     */
    public List<FeeApplicationResult> computeFeeBaseCharge(IFeeRulesProvider feeRulesProvider,
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
        List<FeeApplicationResult> feeApplicationResults = new ArrayList<>();

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
        feeApplicationResults.addAll(computeFees(fcr, account, feeNonExchangeRules, feeLevel, consideration, isChargedPerOwner, isCommissionAllInFee, oldHostOrderId));

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
        feeApplicationResults.addAll(computeFees(fcr, account, feeExchangeRules, feeLevel, consideration, isChargedPerOwner, isCommissionAllInFee, oldHostOrderId));

        return feeApplicationResults;
    }

    /**
     * Compute fees outside of procedure
     *
     * @param fcr
     * @return
     */
    public List<FeeApplicationResult> computeFeeOutsideCommCharge(FeeCalculationRequest fcr, Double consideration) {
        List<FeeApplicationResult> applicationResults = new ArrayList<>();
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
                FeeApplicationResult applicationResult = createAppResult(fcr, FeeLevelType.Firm.name(), fcr.getExternalCommRate(), FeeRuleType.COMMISSION.name(), fcr.getExternalCommType(), fcr.getSymbolCurrency(), amount);

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
    public List<FeeApplicationResult> computeFees(
            FeeCalculationRequest fcr,
            Account account,
            List<FeeRule> feeRules,
            String feeLevel,
            Double consideration,
            Boolean isChargedPerOwner,
            Boolean isCommissionAllInFee,
            String oldHostOrderId) {
        List<FeeApplicationResult> feeApplicationResults = new ArrayList<>();
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

            String isAppliedPerExecution = feeRule.getIsAppliedPerExecution();
            String isAppliedPerTicket = feeRule.getIsAppliedPerTicket();

            if (flatFlee != null) {
                amount += flatFlee;
                currentComputeRate = flatFlee;

            }

            if (feePerContract != null) {
                if (isAppliedPerExecution != null && isAppliedPerExecution.equals("YES")) {
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
                if (isAppliedPerExecution != null && isAppliedPerExecution.equals("YES")) {
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

            if (isAppliedPerTicket != null && isAppliedPerTicket.equals("YES")) {

                // If current Trade is the first trade for this Ticket, apply Rule, otherwise skip.
                if (oldHostOrderId == null) {
                    // COMMISSION LEVEL
                    if (feeLevel.equals(FeeLevelType.Firm.name())) {
                        if (feeRule.getFeeCurrencyName() != null) {
                            FeeApplicationResult feeApplicationResult = createAppResult(fcr, feeLevel, currentComputeRate, feeRule.getFeeCategory(), feeRule.getFeeSubCategory(), feeRule.getFeeCurrencyName(), amount);

                            if (feeApplicationResult != null) {
                                feeApplicationResults.add(feeApplicationResult);
                            }
                        }
                    }
                    // BASE LEVEL

                    if (feeLevel.equals(FeeLevelType.Base.name())) {
                        if (isChargedPerOwner) {
                            if (feeRule.getOwnersList() != null && feeRule.getOwnersList().contains(account.getAccountSource().getSource())) {
                                if (feeRule.getFeeCurrencyName() != null) {
                                    FeeApplicationResult feeApplicationResult = createAppResult(fcr, feeLevel, currentComputeRate, feeRule.getFeeCategory(), feeRule.getFeeSubCategory(), feeRule.getFeeCurrencyName(), amount);

                                    if (feeApplicationResult != null) {
                                        feeApplicationResults.add(feeApplicationResult);
                                    }
                                    if (isCommissionAllInFee != false) {
                                        FeeApplicationResult feeApplicationResult2 = createAppResult(fcr, feeLevel, -currentComputeRate, feeRule.getFeeCategory(), feeRule.getFeeSubCategory(), feeRule.getFeeCurrencyName(), -amount);
                                        if (feeApplicationResult2 != null) {
                                            feeApplicationResults.add(feeApplicationResult2);
                                        }
                                    }
                                }
                            }
                        } else {
                            if (feeRule.getFeeCurrencyName() != null) {
                                FeeApplicationResult feeApplicationResult = createAppResult(fcr, feeLevel, currentComputeRate, feeRule.getFeeCategory(), feeRule.getFeeSubCategory(), feeRule.getFeeCurrencyName(), amount);
                                if (feeApplicationResult != null) {
                                    feeApplicationResults.add(feeApplicationResult);
                                }

                                if (isCommissionAllInFee != false) {
                                    FeeApplicationResult feeApplicationResult2 = createAppResult(fcr, feeLevel, -currentComputeRate, feeRule.getFeeCategory(), feeRule.getFeeSubCategory(), feeRule.getFeeCurrencyName(), -amount);
                                    if (feeApplicationResult2 != null) {
                                        feeApplicationResults.add(feeApplicationResult2);
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // COMMISSION LEVEL
                if (feeLevel.equals(FeeLevelType.Firm.name())) {
                    if (feeRule.getFeeCurrencyName() != null) {
                        FeeApplicationResult feeApplicationResult = createAppResult(fcr, feeLevel, currentComputeRate, feeRule.getFeeCategory(), feeRule.getFeeSubCategory(), feeRule.getFeeCurrencyName(), amount);

                        if (feeApplicationResult != null) {
                            feeApplicationResults.add(feeApplicationResult);
                        }
                    }
                }
            }
        }

        return feeApplicationResults;
    }

    private FeeApplicationResult createAppResult(FeeCalculationRequest fcr, String feeLevel, Double currentComputeRate, String feeCategory, String feeSubCategory, String currencyName, Double amount) {
        FeeApplicationResult feeApplicationResult = new FeeApplicationResult();
        feeApplicationResult.setOrderExecutionId(fcr.getOrderExecutionId());
        feeApplicationResult.setFeeLevel(feeLevel);
        feeApplicationResult.setFeeType(feeCategory);
        feeApplicationResult.setFeeCategory(feeSubCategory);
        feeApplicationResult.setCurrency(currencyName);
        feeApplicationResult.setAmount(amount);
        feeApplicationResult.setCommRate(currentComputeRate);

        return feeApplicationResult;
    }


}
