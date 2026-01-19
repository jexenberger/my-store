package net.exenberger.mystore.domain.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.exenberger.mystore.domain.GeoService;
import net.exenberger.mystore.domain.LatLongPosition;
import net.exenberger.mystore.util.Result;

public class DefaultGeoServiceTest {

    public static final String GOOGLE_DNS_IP_ADDRESS = "8.8.8.8";
    public static final String POSTAL_CODE = "1234AB";
    public static final LatLongPosition LAT_LONG_POSITION = new LatLongPosition(1.0, 1.0);

    private Supplier<String> ipResolver;
    private Function<String, LatLongPosition> ipGeoLocationResolver;
    private BiFunction<String, Integer, LatLongPosition> postalCodeResolver;
    private GeoService geoService;

    @BeforeEach
    void setUp() {
        ipResolver = mock(Supplier.class);
        ipGeoLocationResolver = mock(Function.class);
        postalCodeResolver = mock(BiFunction.class);
        geoService = new DefaultGeoService(ipResolver, ipGeoLocationResolver, postalCodeResolver);
    }

    @Test
    void getLocationFromAddress() {
        when(ipResolver.get()).thenReturn(GOOGLE_DNS_IP_ADDRESS);
        when(ipGeoLocationResolver.apply(eq(GOOGLE_DNS_IP_ADDRESS))).thenReturn(LAT_LONG_POSITION);
        var result = geoService.getSystemLocation();
        assertThat(result).isEqualTo(Result.ok(LAT_LONG_POSITION));
    }


    @Test
    void getLocationFromAddressNullReturned() {
        when(ipResolver.get()).thenReturn(null);
        var result = geoService.getSystemLocation();
        assertThat(result).isInstanceOf(Result.Err.class);
    }

    @Test
    void getLocationFromPostalCode() {
        when(postalCodeResolver.apply(eq(POSTAL_CODE),eq(12))).thenReturn(LAT_LONG_POSITION);
        var result = geoService.getLocationFromAddress(POSTAL_CODE, 12);
        assertThat(result).isEqualTo(Result.ok(LAT_LONG_POSITION));
    }


}
