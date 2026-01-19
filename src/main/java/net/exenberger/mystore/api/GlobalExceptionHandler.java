package net.exenberger.mystore.api;

import static net.exenberger.mystore.util.ValidationError.error;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import net.exenberger.mystore.util.BusinessException;
import net.exenberger.mystore.util.Failure;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class.getName());

    private static final Map<Failure.Errors, HttpStatus> STATUS_MAP = Map.of(
            Failure.Errors.notFound, HttpStatus.NOT_FOUND,
            Failure.Errors.invalidResult, HttpStatus.BAD_GATEWAY,
            Failure.Errors.invalidInput, HttpStatus.BAD_REQUEST,
            Failure.Errors.recordExists, HttpStatus.CONFLICT
    );

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Failure> handleBusinessException(BusinessException ex) {
        var failure = ex.getFailure();
        var statusCode = Optional
                .ofNullable(STATUS_MAP.get(failure.code()))
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(statusCode).body(failure);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Failure> handle(Exception ex) {
        LOG.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Failure.internalError("internal failure, contact administrator"));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Failure> handleValidation(ConstraintViolationException ex) {
        var fields = ex.getConstraintViolations().stream().map(it -> error(getLast(it.getPropertyPath()), it.getMessage())).collect(Collectors.toSet());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Failure.invalidInput(fields));
    }

    private String getLast(Path path) {
        String last = null;
        var iter = path.iterator();
        while (iter.hasNext()) {
            last = iter.next().getName();
        }
        return last;
    }

}
