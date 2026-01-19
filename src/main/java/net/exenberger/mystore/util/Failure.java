package net.exenberger.mystore.util;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

public record Failure(Errors code, String message, @JsonInclude(JsonInclude.Include.NON_NULL) Set<ValidationError> errors) implements Serializable {


    public enum Errors {
        notFound,
        invalidInput,
        invalidResult,
        recordExists,
        internalError
    }


    public static Failure notFound(String message) {
        return new Failure(Errors.notFound, message, null);
    }

    public static Failure internalError(String message) {
        return new Failure(Errors.internalError, message, null);
    }

    public static Failure invalidInput(Set<ValidationError> errors) {
        return new Failure(Errors.invalidInput, "invalid input", errors);
    }





}
