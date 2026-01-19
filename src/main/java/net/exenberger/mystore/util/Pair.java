package net.exenberger.mystore.util;

public record Pair<T, K>(T left, K right) {

    public static <T, K> Pair<T, K> cons(T left, K right) {
        return new Pair<>(left, right);
    }

}
