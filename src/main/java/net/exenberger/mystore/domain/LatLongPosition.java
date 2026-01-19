package net.exenberger.mystore.domain;

import java.util.Objects;
import java.util.function.BiFunction;

public record LatLongPosition(double latitude, double longitude) {


    private static final double EARTH_RADIUS = 6371.0088;

    private static final BiFunction<LatLongPosition, LatLongPosition, Double> EQUIRECTANGULAR = (self, target) -> {

        double lat1Rad = Math.toRadians(self.latitude);
        double lat2Rad = Math.toRadians(target.latitude);
        double lon1Rad = Math.toRadians(self.longitude);
        double lon2Rad = Math.toRadians(target.longitude);

        double x = (lon2Rad - lon1Rad) * Math.cos((lat1Rad + lat2Rad) / 2);
        double y = (lat2Rad - lat1Rad);

        return Math.sqrt(x * x + y * y) * EARTH_RADIUS;
    };

    private static BiFunction<LatLongPosition, LatLongPosition, Double> DEFAULT_CALCULATION;

    static {
        setDefaultCalculation(EQUIRECTANGULAR);
    }

    public double calculateDistance(BiFunction<LatLongPosition, LatLongPosition, Double> distanceCalculator, LatLongPosition target) {
        return distanceCalculator.apply(this, target);
    }

    public double calculateDistance(LatLongPosition target) {
        return calculateDistance(DEFAULT_CALCULATION, target);
    }

    public static void setDefaultCalculation(BiFunction<LatLongPosition, LatLongPosition, Double> distanceCalculator) {
        DEFAULT_CALCULATION = Objects.requireNonNull(distanceCalculator, "distance calculator is required");
    }



}
