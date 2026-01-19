package net.exenberger.mystore.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.exenberger.mystore.domain.GeoService;
import net.exenberger.mystore.domain.LatLongPosition;
import net.exenberger.mystore.domain.impl.JsonStoreRepository;
import net.exenberger.mystore.service.LatLongDTO;
import net.exenberger.mystore.service.SurroundingStoreDTO;
import net.exenberger.mystore.util.Failure;
import net.exenberger.mystore.util.Result;
import tools.jackson.databind.ObjectMapper;

public class DefaultSurroundingStoreServiceTest {

    public static final List<String> CLOSEST_CITIES = List.of("Den Haag", "Voorburg", "Leidschendam");
    public static final LatLongPosition MINISTERIE_VAN_ALGEMENE_ZAKEN = new LatLongPosition(52.08350496d, 4.32782494d);
    private JsonStoreRepository repository;
    private GeoService geoService;

    @BeforeEach
    void setUp() {
        repository = new JsonStoreRepository("stores.json", new ObjectMapper());
        geoService = mock(GeoService.class);
    }

    @Test
    void findBySystemLocation() {

        when(geoService.getSystemLocation()).thenReturn(Result.ok(MINISTERIE_VAN_ALGEMENE_ZAKEN));

        var surroundingStoreService = new DefaultSurroundingStoreService(geoService, repository);
        var result = surroundingStoreService.findBySystemLocation(3);
        testResult(result);

    }


    @Test
    void findByAddress() {

        when(geoService.getLocationFromAddress(eq("1234AA"), eq(1))).thenReturn(Result.ok(MINISTERIE_VAN_ALGEMENE_ZAKEN));

        var surroundingStoreService = new DefaultSurroundingStoreService(geoService, repository);
        var result = surroundingStoreService.findByAddress("1234AA", 1, 3);
        testResult(result);

    }

    @Test
    void findByLatitudeAndLongitude() {
        var surroundingStoreService = new DefaultSurroundingStoreService(geoService, repository);
        var result = surroundingStoreService.findByLatLong(new LatLongDTO(MINISTERIE_VAN_ALGEMENE_ZAKEN.latitude(), MINISTERIE_VAN_ALGEMENE_ZAKEN.longitude()), 3);
        testResult(result);

    }


    private static void testResult(Result<List<SurroundingStoreDTO>, Failure> result) {
        assertThat(result).isInstanceOf(Result.Ok.class);
        assertThat(result.toOptional().isPresent()).isTrue();
        var stores = result.getOrElse(Collections.emptyList());
        assertThat(stores).hasSize(3);
        //best effort based on what is on google and in the file
        for (var store : stores) {
            var theStore = store.store();
            assertTrue(CLOSEST_CITIES.contains(theStore.city()));
            assertThat(theStore.id()).isNotBlank();
            assertThat(store.distance()).isGreaterThan(0.0d);
            assertThat(theStore.location()).isNotNull();
            assertThat(theStore.location().latitude()).isGreaterThan(0.0d);
            assertThat(theStore.location().longitude()).isGreaterThan(0.0d);
        }
    }
}



