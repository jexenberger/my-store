package net.exenberger.mystore.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import net.exenberger.mystore.service.LatLongDTO;
import net.exenberger.mystore.service.SurroundingStoreDTO;
import net.exenberger.mystore.service.SurroundingStoreService;
import net.exenberger.mystore.util.BusinessException;

@RestController
@RequestMapping("/store-locations")
public class MyStoreAPI {

    private static final Logger LOG = LoggerFactory.getLogger(MyStoreAPI.class);

    private final SurroundingStoreService service;

    public MyStoreAPI(SurroundingStoreService service) {
        this.service = service;
    }

    @Operation(summary = "Get closest stores based on the system location (WARNING, does not work through VPN)")
    @GetMapping("/system")
    public CollectionModel<SurroundingStoreDTO> findClosestStoresBySystemLocation(@RequestParam(value = "max", defaultValue = "5") int max) {
        LOG.debug("findClosestStoresBySystemLocation");
        return this
                .service
                .findBySystemLocation(max)
                .mapOk(CollectionModel::of)
                .orElseThrow(BusinessException::new);
    }


    @Operation(summary = "Get closest stores based on a set of longitude and latitude coordinates")
    @GetMapping("/{longitude},{latitude}")
    public CollectionModel<SurroundingStoreDTO> findClosestStoresByLatLong(
            @PathVariable double longitude,
            @PathVariable double latitude,
            @RequestParam(value = "max", defaultValue = "5") int max) {
        LOG.debug("findClosestStoresByLatLong {}, {},{}", longitude, latitude, max);
        return this
                .service
                .findByLatLong(new LatLongDTO(latitude, longitude), max)
                .mapOk(CollectionModel::of)
                .orElseThrow(BusinessException::new);
    }


    @Operation(summary = "Get closest stores based on address using the Postal Code and the house number)")
    @GetMapping("/{postalCode}/{houseNumber}")
    public CollectionModel<SurroundingStoreDTO> findClosestStoresByPostalCode(
            @PathVariable String postalCode,
            @PathVariable int houseNumber,
            @RequestParam(value = "max", defaultValue = "5") int max) {
        LOG.debug("findClosestStoresByPostalCode {}, {},{}", postalCode, houseNumber, max);
        return this
                .service
                .findByAddress(postalCode, houseNumber, max)
                .mapOk(CollectionModel::of)
                .orElseThrow(BusinessException::new);
    }

}
