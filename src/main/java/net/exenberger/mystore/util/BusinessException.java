package net.exenberger.mystore.util;

import java.util.Objects;

public class BusinessException extends RuntimeException {

    private final Failure failure;

    public BusinessException(Failure failure) {
        this.failure = Objects.requireNonNull(failure, "failure is required!");
    }

    public Failure getFailure() {
        return failure;
    }
}
