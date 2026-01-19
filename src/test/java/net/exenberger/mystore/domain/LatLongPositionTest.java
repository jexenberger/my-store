package net.exenberger.mystore.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LatLongPositionTest {

    @Test
    void calculateDistance() {

        //we know that the distance between these two Jumbos are ~7,06 and ~7,07 depending on which formula you use
        var jumboAmsterdamBaarjsesWeg = new LatLongPosition(52.358817d, 4.855152d);
        var jumboAmsterdamBuikslotermeerplein = new LatLongPosition(52.399066, 4.935776);


        var distance = jumboAmsterdamBaarjsesWeg.calculateDistance(jumboAmsterdamBuikslotermeerplein);

        assertThat(distance).isBetween(7.06, 7.07);
    }
}
