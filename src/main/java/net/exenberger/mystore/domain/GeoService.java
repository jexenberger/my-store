package net.exenberger.mystore.domain;

import net.exenberger.mystore.util.Failure;
import net.exenberger.mystore.util.Result;

public interface GeoService {

    Result<LatLongPosition, Failure> getSystemLocation();

    Result<LatLongPosition, Failure> getLocationFromAddress(String postalCode, int houseNumber);

}
