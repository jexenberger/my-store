package net.exenberger.mystore.service.impl;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import net.exenberger.mystore.domain.GeoService;
import net.exenberger.mystore.domain.LatLongPosition;
import net.exenberger.mystore.domain.impl.JsonStoreRepository;
import net.exenberger.mystore.service.LatLongDTO;
import net.exenberger.mystore.service.StoreDTO;
import net.exenberger.mystore.service.SurroundingStoreDTO;
import net.exenberger.mystore.service.SurroundingStoreService;
import net.exenberger.mystore.util.Failure;
import net.exenberger.mystore.util.Result;

@Service
@Validated
public class DefaultSurroundingStoreService implements SurroundingStoreService {

    private final GeoService geoService;
    private final JsonStoreRepository jsonStoreRepository;

    public DefaultSurroundingStoreService(GeoService geoService, JsonStoreRepository jsonStoreRepository) {
        this.geoService = geoService;
        this.jsonStoreRepository = jsonStoreRepository;
    }

    @Override
    public Result<List<SurroundingStoreDTO>, Failure> findBySystemLocation(int max) {
        return runLookup(geoService.getSystemLocation(), max);

    }


    private Result<List<SurroundingStoreDTO>, Failure> runLookup(Result<LatLongPosition, Failure> geoService, int limit) {
        return geoService
                .mapOk(pos -> jsonStoreRepository.findClosest(pos, limit))
                .mapOk(stores -> stores.map(it -> {
                    var distance = it.left();
                    var store = it.right();
                    return new SurroundingStoreDTO(
                            new StoreDTO(
                                    store.id(),
                                    store.name(),
                                    store.city(),
                                    new LatLongDTO(store.location().latitude(), store.location().longitude())
                            ),
                            distance
                    );
                }).toList());
    }

    @Override
    @Cacheable("address")
    public Result<List<SurroundingStoreDTO>, Failure> findByAddress(
            String postalCode,
            int houseNumber,
            int max) {
        return runLookup(geoService.getLocationFromAddress(postalCode, houseNumber), max);

    }

    @Override
    @Cacheable("latLong")
    public Result<List<SurroundingStoreDTO>, Failure> findByLatLong(LatLongDTO location, int max) {
        return runLookup(Result.ok(new LatLongPosition(location.latitude(), location.longitude())), max);
    }
}
