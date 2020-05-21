package compute.helper;

import compute.model.FeeCalculationRequest;
import model.types.AllocationExcludedType;
import model.types.AssetType;
import model.types.CurrencyType;

import java.util.Arrays;

public class FeeCalculationRequestValidator {
    /**
     * A set of validators against request
     *
     * @param fcr
     * @return
     */
    public boolean isInvalidRequestData(FeeCalculationRequest fcr) {
        // NOT NULL check against primary columns
        if (fcr.getQuantity() == null ||
                fcr.getPrice() == null ||
                fcr.getAssetType() == null ||
                !Arrays.stream(AssetType.values()).anyMatch(AssetType.valueOf(fcr.getAssetType())::equals) ||
                fcr.getFullExecutingBrokerName() == null ||
                fcr.getSymbolCurrency() == null
        ) {
            System.err.println("Preliminary checks failed");

            return true;
        }

        // not allow specific allocation types
        if (fcr.getAllocationType() != null) {
            if (Arrays.stream(AssetType.values()).anyMatch(AllocationExcludedType.valueOf(fcr.getAllocationType())::equals)) {
                System.err.println("Allocation type is one of excluded Types. " + fcr.getAllocationType());

                return true;
            }
        }

        // validate the symbol currency is one of excepted type
        if (!Arrays.stream(CurrencyType.values()).anyMatch(CurrencyType.valueOf(fcr.getSymbolCurrency())::equals)) {
            System.err.println("Symbol currency is not recognized " + fcr.getSymbolCurrency());

            return true;
        }

        return false;
    }



}
