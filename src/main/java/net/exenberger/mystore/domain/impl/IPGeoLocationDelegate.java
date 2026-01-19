package net.exenberger.mystore.domain.impl;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import net.exenberger.mystore.domain.LatLongPosition;
import net.exenberger.mystore.util.SystemException;

@Component("ipGeoLocationDelegate")
public class IPGeoLocationDelegate implements Function<String, LatLongPosition> {

    private final String url;

    public IPGeoLocationDelegate(@Value("${dependency.ipinfo.url}") String url) {
        this.url = url;
    }

    LatLongPosition resolveLocationFromIP(String ip){

        try {
            var result = RestClient.builder()
                    .baseUrl(url)
                    .build()
                    .get()
                    .uri("{ip}/json", ip)
                    .retrieve()
                    .body(Map.class);

            var locS = Optional.ofNullable(result.get("loc")).orElseThrow(() -> new RuntimeException("unable to locate position"));
            var parts = Arrays.stream(locS.toString().split(",")).map(String::trim).toList();
            var lat = Double.valueOf(parts.get(0));
            var lng = Double.valueOf(parts.get(1));

            return new LatLongPosition(lat, lng);
        } catch (Exception e) {
            throw new SystemException("unable to resolve IP address from IP address " + ip, e);
        }
    }

    @Override
    public LatLongPosition apply(String s) {
        return resolveLocationFromIP(s);
    }
}
