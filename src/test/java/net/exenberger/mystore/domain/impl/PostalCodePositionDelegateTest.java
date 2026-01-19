package net.exenberger.mystore.domain.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class PostalCodePositionDelegateTest {

    @Test
    void resolveLocationFromAddress() {

        //ministry of Algemene zaken
        var result = new PostalCodePositionDelegate("https://api.pdok.nl/bzk/locatieserver/search/v3_1").resolveLocationFromAddress("2594AC", 73);
        assertThat(result.longitude()).isEqualTo(4.32782494d);
        assertThat(result.latitude()).isEqualTo(52.08350496);

    }
}
