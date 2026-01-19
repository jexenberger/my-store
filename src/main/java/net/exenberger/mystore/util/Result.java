package net.exenberger.mystore.util;



import java.util.Objects;
import java.util.Optional;
import java.util.function.*;

/**
 * Result monad: Ok(T) or Err(E).
 *
 */
public sealed interface Result<T, E> permits Result.Ok, Result.Err {

    record Ok<T, E>(T value) implements Result<T, E> { }

    record Err<T, E>(E error) implements Result<T, E> {
        public Err {
            Objects.requireNonNull(error, "error must not be null");
        }
    }

    static <T, E> Result<T, E> ok(T value) { return new Ok<>(value); }

    static <T, E> Result<T, E> err(E error) { return new Err<>(error); }

    default boolean isOk()  { return this instanceof Ok<?, ?>; }
    default boolean isErr() { return this instanceof Err<?, ?>; }

    // ===== Core "map" functions =====

    /** Map (transform) the Ok value, leaving the Err as-is. */
    default <U> Result<U, E> mapOk(Function<? super T, ? extends U> f) {
        return switch (this) {
            case Ok<T, E> ok   -> Result.ok(f.apply(ok.value()));
            case Err<T, E> err -> Result.err(err.error());
        };
    }

    /** Map (transform) the Err value, leaving the Ok as-is. */
    default <F> Result<T, F> mapErr(Function<? super E, ? extends F> f) {
        return switch (this) {
            case Ok<T, E> ok   -> Result.ok(ok.value());
            case Err<T, E> err -> Result.err(f.apply(err.error()));
        };
    }

    /** Map both sides in one pass (aka bimap). */
    default <U, F> Result<U, F> biMap(Function<? super T, ? extends U> fOk,
            Function<? super E, ? extends F> fErr) {
        return switch (this) {
            case Ok<T, E> ok   -> Result.ok(fOk.apply(ok.value()));
            case Err<T, E> err -> Result.err(fErr.apply(err.error()));
        };
    }

    // ===== Monad (bind) =====

    /** FlatMap: chain computations that can also return Result. */
    default <U> Result<U, E> flatMap(Function<? super T, ? extends Result<U, E>> f) {
        return switch (this) {
            case Ok<T, E> ok   -> Objects.requireNonNull(f.apply(ok.value()));
            case Err<T, E> err -> Result.err(err.error());
        };
    }

    default <R> R fold(Function<? super E, ? extends R> onErr,
            Function<? super T, ? extends R> onOk) {
        return switch (this) {
            case Ok<T, E> ok   -> onOk.apply(ok.value());
            case Err<T, E> err -> onErr.apply(err.error());
        };
    }

    default T getOrElse(T fallback) {
        return (this instanceof Ok<T, E>(T value)) ? value : fallback;
    }

    /** Throw a mapped exception when Err. */
    default <X extends Throwable> T orElseThrow(Function<E, ? extends X> toThrowable) throws X {
        return switch (this) {
            case Ok<T, E> ok   -> ok.value();
            case Err<T, E> err -> throw toThrowable.apply(err.error());
        };
    }

    // ===== Interop =====

    default Optional<T> toOptional() {
        return (this instanceof Ok<T, E>(T value)) ? Optional.ofNullable(value) : Optional.empty();
    }
}

