package org.example.backend.Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> fieldErrors.putIfAbsent(error.getField(), error.getDefaultMessage()));

        ProblemDetail pd = createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                "One or more fields are invalid."
        );
        pd.setProperty("fieldErrors", fieldErrors);

        return ResponseEntity.badRequest().body(pd);
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
            HandlerMethodValidationException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        Map<String, List<String>> errors = new LinkedHashMap<>();

        ex.getParameterValidationResults().forEach(result -> {
            String parameterName = result.getMethodParameter().getParameterName();
            List<String> messages = result.getResolvableErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value")
                    .toList();

            errors.put(parameterName != null ? parameterName : "parameter", messages);
        });

        ProblemDetail pd = createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                "One or more request parameters are invalid."
        );
        pd.setProperty("parameterErrors", errors);

        return ResponseEntity.badRequest().body(pd);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        ProblemDetail pd = createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Malformed request body",
                "The request body could not be parsed."
        );

        Throwable cause = ex.getMostSpecificCause();
        if (cause != null && cause.getMessage() != null) {
            pd.setProperty("error", cause.getMessage());
        }

        return ResponseEntity.badRequest().body(pd);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        ProblemDetail pd = createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Missing request parameter",
                "Required request parameter '%s' is missing.".formatted(ex.getParameterName())
        );

        return ResponseEntity.badRequest().body(pd);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        ProblemDetail pd = createProblemDetail(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "Unsupported media type",
                "The request Content-Type is not supported."
        );

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(pd);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
            HttpMediaTypeNotAcceptableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        // No body avoids a second conversion failure when the client Accept header is too restrictive.
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex
    ) {
        String detail = "Parameter '%s' has an invalid value.".formatted(ex.getName());

        if (ex.getRequiredType() != null) {
            detail = "Parameter '%s' must be of type %s."
                    .formatted(ex.getName(), ex.getRequiredType().getSimpleName());
        }

        ProblemDetail pd = createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Type mismatch",
                detail
        );
        pd.setProperty("rejectedValue", ex.getValue());

        return ResponseEntity.badRequest().body(pd);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgumentException(
            IllegalArgumentException ex
    ) {
        ProblemDetail pd = createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Bad request",
                ex.getMessage() != null ? ex.getMessage() : "Invalid request."
        );

        return ResponseEntity.badRequest().body(pd);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(NotFoundException ex) {
        ProblemDetail pd = createProblemDetail(
                HttpStatus.NOT_FOUND,
                "Resource not found",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pd);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ProblemDetail> handleForbidden(ForbiddenException ex) {
        ProblemDetail pd = createProblemDetail(
                HttpStatus.FORBIDDEN,
                "Forbidden",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(pd);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ProblemDetail> handleConflict(ConflictException ex) {
        ProblemDetail pd = createProblemDetail(
                HttpStatus.CONFLICT,
                "Conflict",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(pd);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnexpected(Exception ex) {
        log.error("Unhandled exception", ex);

        // If content negotiation is the failure itself, return status-only to avoid recursive handler failure.
        if (ex instanceof HttpMediaTypeNotAcceptableException) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        ProblemDetail pd = createProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error",
                "An unexpected error occurred."
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(pd);
    }

    private ProblemDetail createProblemDetail(HttpStatus status, String title, String detail) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        pd.setType(URI.create("about:blank"));
        return pd;
    }
}