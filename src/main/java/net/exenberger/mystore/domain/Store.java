package net.exenberger.mystore.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Store(String id, String name, String city, LatLongPosition location) {


    @JsonCreator
    public Store(@JsonProperty("uuid") String id, @JsonProperty("addressName") String name, String city, double longitude, double latitude) {
        this(id, name, city, new LatLongPosition(latitude, longitude));
    }

}
