package net.exenberger.mystore.domain.impl;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import net.exenberger.mystore.domain.GeoService;
import net.exenberger.mystore.domain.LatLongPosition;
import net.exenberger.mystore.util.Failure;
import net.exenberger.mystore.util.Result;

@Service
public class DefaultGeoService implements GeoService {

    private Supplier<String> ipAddressDelegate;
    private Function<String, LatLongPosition> ipGeoLocationDelegate;
    private BiFunction<String, Integer, LatLongPosition> postalCodePositionDelegate;

    public DefaultGeoService(
            @Qualifier("ipAddressDelegate") Supplier<String> ipAddressDelegate,
            @Qualifier("ipGeoLocationDelegate") Function<String, LatLongPosition> ipGeoLocationDelegate,
            @Qualifier("postalCodePositionDelegate") BiFunction<String, Integer, LatLongPosition> postalCodePositionDelegate) {
        this.ipAddressDelegate = ipAddressDelegate;
        this.ipGeoLocationDelegate = ipGeoLocationDelegate;
        this.postalCodePositionDelegate = postalCodePositionDelegate;
    }

    @Override
    public Result<LatLongPosition, Failure> getSystemLocation() {
        return Optional
                .ofNullable(ipAddressDelegate.get())
                .filter(ip -> !"127.0.0.1".equals(ip))
                .map(it -> Result.<LatLongPosition, Failure>ok(ipGeoLocationDelegate.apply(it)))
                .orElse(Result.err(Failure.notFound("Unable to resolve system location from IP address")));
    }

    @Override
    public Result<LatLongPosition, Failure> getLocationFromAddress(String postalCode, int houseNumber) {
        return Optional
                .ofNullable(postalCodePositionDelegate.apply(postalCode, houseNumber))
                .map(Result::<LatLongPosition, Failure>ok)
                .orElse(Result.err(Failure.notFound("Unable to resolve postal code")));
    }
}
