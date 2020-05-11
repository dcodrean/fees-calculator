package providers;

import model.entities.Asset;
import model.entities.Instrument;

public interface ISymbologyProvider {
    Instrument getByTicker(String ticker);

    Asset getByAssetName(String assetName);
}
