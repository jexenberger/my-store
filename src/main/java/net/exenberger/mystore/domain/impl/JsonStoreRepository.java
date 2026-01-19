package net.exenberger.mystore.domain.impl;

import static net.exenberger.mystore.util.Pair.cons;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import net.exenberger.mystore.domain.LatLongPosition;
import net.exenberger.mystore.domain.Store;
import net.exenberger.mystore.domain.StoreRepository;
import net.exenberger.mystore.util.Pair;
import net.exenberger.mystore.util.SystemException;
import tools.jackson.databind.ObjectMapper;

@Service
public class JsonStoreRepository implements StoreRepository {

    private static final Logger LOG = LoggerFactory.getLogger(JsonStoreRepository.class);

    private Collection<Store> stores;
    private String path;
    private ObjectMapper mapper;

    @Override
    public Stream<Pair<Double, Store>> findClosest(LatLongPosition location, int limit) {
        return stores
                .stream()
                .map(store -> cons(location.calculateDistance(store.location()), store))
                .sorted(Comparator.comparingDouble(Pair::left))
                .limit(limit);
    }

    public JsonStoreRepository(@Value("${json.store.path:stores.json}") String path, ObjectMapper mapper) {
        this.path = path;
        this.mapper = mapper;
        init();
    }

    private void init() {
        LOG.info("Loading stores from classpath:{}", path);
        var storesDb = new ClassPathResource(path);
        try (var is = storesDb.getInputStream()) {
            var storesMap = mapper.readValue(is, Stores.class);
            for (var store : storesMap.stores()) {
                LOG.trace("Loading store {}", store.name());
            }
            stores = storesMap.stores();
        } catch (NullPointerException | IOException e) {
            LOG.error(e.getMessage(), e);
            throw new SystemException("failed to load stores", e);
        }
    }

}
