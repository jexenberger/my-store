package net.exenberger.mystore.service;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import net.exenberger.mystore.util.Failure;
import net.exenberger.mystore.util.Result;

public interface SurroundingStoreService {

    Result<List<SurroundingStoreDTO>, Failure> findBySystemLocation(@Positive  int max);

    Result<List<SurroundingStoreDTO>, Failure> findByAddress(
            @NotBlank @Pattern(regexp = "^[0-9]{4}[A-Z0-9]{2}$", message = "Invalid Dutch postal code (no spaces or lowercase letters allowed)") String postalCode,
            @Positive int houseNumber,
            @Positive int max);

    Result<List<SurroundingStoreDTO>, Failure> findByLatLong(
            @Valid @NotNull LatLongDTO location,
            @Positive int max);

}
