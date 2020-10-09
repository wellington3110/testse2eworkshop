package br.com.wellingtonmacedo.testsworkshop.exception;

import br.com.wellingtonmacedo.testsworkshop.dto.ErrorDTO;
import br.com.wellingtonmacedo.testsworkshop.validator.ErrorCause;
import lombok.Data;

import java.util.List;

@Data
public class ValidationException extends IllegalArgumentException {

    public final static String CODE_ERROR = "validation_error";
    public final static String VALUE_ALREADY_REGISTERED = "value already registered";

    private final ErrorDTO error;

    public ValidationException(List<ErrorCause> errorCauses) {
        error = new ErrorDTO(CODE_ERROR, errorCauses);
    }
}
