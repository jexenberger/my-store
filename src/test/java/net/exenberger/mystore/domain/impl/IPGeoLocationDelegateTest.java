package net.exenberger.mystore.domain.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


class IPGeoLocationDelegateTest {

    @Test
    void resolveLocation() throws Exception {
        var result = new IPGeoLocationDelegate("https://ipinfo.io").resolveLocationFromIP("8.8.8.8");
        System.out.println(result);
        assertThat(result.latitude()).isGreaterThan(0.0d);

    }
}
