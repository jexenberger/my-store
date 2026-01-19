package net.exenberger.mystore.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import jakarta.validation.ConstraintViolationException;
import net.exenberger.mystore.util.BusinessException;
import net.exenberger.mystore.util.Failure;

class GlobalExceptionHandlerTest {

    @Test
    void handleBusinessException() {
        var result = new GlobalExceptionHandler().handleBusinessException(new BusinessException(Failure.notFound("stuff")));
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void handle() {
        var result   = new GlobalExceptionHandler().handle(new RuntimeException());
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(result.getBody().code()).isEqualTo(Failure.Errors.internalError);
    }

    @Test
    void handleConstraintViolation() {

        var result = new GlobalExceptionHandler().handleValidation(new ConstraintViolationException(Set.of()));
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody().code()).isEqualTo(Failure.Errors.invalidInput);
    }

}
