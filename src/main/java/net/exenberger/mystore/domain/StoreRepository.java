package net.exenberger.mystore.domain;

import java.util.stream.Stream;

import net.exenberger.mystore.util.Pair;

public interface StoreRepository {


    Stream<Pair<Double, Store>> findClosest(LatLongPosition location, int limit);


}
