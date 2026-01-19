package net.exenberger.mystore.domain.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import net.exenberger.mystore.domain.LatLongPosition;
import net.exenberger.mystore.domain.Store;
import net.exenberger.mystore.util.Pair;
import tools.jackson.databind.ObjectMapper;

public class JsonStoreRepositoryTest {

    public static final List<String> CLOSEST_CITIES = List.of("Den Haag", "Voorburg", "Leidschendam");
    public static final LatLongPosition MINISTERIE_VAN_ALGEMENE_ZAKEN = new LatLongPosition( 52.08350496d, 4.32782494d);

    @Test
    void findClosest() {

        var repository = new JsonStoreRepository("stores.json", new ObjectMapper());

        var stores = repository.findClosest(MINISTERIE_VAN_ALGEMENE_ZAKEN, 3).toList();
        assertThat(stores).hasSize(3);

        //best effort based on what is on google and in the file
        for (Pair<Double, Store> store : stores) {
            assertTrue(CLOSEST_CITIES.contains(store.right().city()));
        }

    }
}
