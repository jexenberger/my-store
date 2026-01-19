package net.exenberger.mystore.util;

public record ValidationError(String field, String message) {

    public static ValidationError error(String field, String message) {
        return new ValidationError(field, message);
    }

}
