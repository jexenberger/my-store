package net.exenberger.mystore.api;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import net.exenberger.mystore.service.LatLongDTO;
import net.exenberger.mystore.service.StoreDTO;
import net.exenberger.mystore.service.SurroundingStoreDTO;
import net.exenberger.mystore.service.SurroundingStoreService;
import net.exenberger.mystore.util.Result;

class MyStoreAPITest {

    private SurroundingStoreService mock;
    private StoreDTO storeDTO;
    private MyStoreAPI api;

    @BeforeEach
    void setUp() {
        mock = Mockito.mock(SurroundingStoreService.class);
        storeDTO = new StoreDTO("1", "x", "x,", new LatLongDTO(1.0d, 1.0d));
        api = new MyStoreAPI(mock);
    }

    @Test
    void findClosestStoresByAddress() {

        when(mock.findByAddress(eq("1234AA"), eq(1), eq(1))).thenReturn(Result.ok(List.of(new SurroundingStoreDTO(storeDTO, 10.0d))));
        var result = api.findClosestStoresByPostalCode("1234AA", 1, 1);
        assertThat(result.getContent().size()).isEqualTo(1);
    }


    @Test
    void findClosestStoresBySystemLocation() {

        when(mock.findBySystemLocation(eq(1))).thenReturn(Result.ok(List.of(new SurroundingStoreDTO(storeDTO, 10.0d))));
        var result = api.findClosestStoresBySystemLocation(1);
        assertThat(result.getContent().size()).isEqualTo(1);
    }


    @Test
    void findClosestStoresByLatLong() {

        when(mock.findByLatLong(new LatLongDTO(1.0d, 1.0d), 1)).thenReturn(Result.ok(List.of(new SurroundingStoreDTO(storeDTO, 10.0d))));
        var result = api.findClosestStoresByLatLong(1.0d, 1.0d, 1);
        assertThat(result.getContent().size()).isEqualTo(1);
    }

}
