package repository.DTO;

import java.util.ArrayList;
import java.util.List;

public class ResultPaymentDTO<T> {
    private final boolean success;
    private final T data;
    private final List<FieldError> errors;
    private final String message;

    private ResultPaymentDTO(boolean success, T data, List<FieldError> errors, String message) {
        this.success = success;
        this.data = data;
        this.errors = errors == null ? new ArrayList<>() : errors;
        this.message = message;
    }

    public static <T> ResultPaymentDTO<T> ok(T data) { return new ResultPaymentDTO<>(true, data, null, null); }
    public static <T> ResultPaymentDTO<T> fail(String message) { return new ResultPaymentDTO<>(false, null, null, message); }
    public static <T> ResultPaymentDTO<T> failWithErrors(List<FieldError> errors) { return new ResultPaymentDTO<>(false, null, errors, null); }

    public boolean isSuccess() { return success; }
    public T getData() { return data; }
    public List<FieldError> getErrors() { return errors; }
    public String getMessage() { return message; }

    public static class FieldError {
        public final String field;
        public final String error;
        public FieldError(String field, String error) { this.field = field; this.error = error; }
    }
}