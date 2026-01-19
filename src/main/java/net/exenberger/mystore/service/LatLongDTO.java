package net.exenberger.mystore.service;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Positive;

@Validated
public record LatLongDTO(@Positive double latitude, @Positive double longitude) {
}
