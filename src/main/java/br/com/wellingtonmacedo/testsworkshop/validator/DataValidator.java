package br.com.wellingtonmacedo.testsworkshop.validator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;

public abstract class DataValidator {

    private static Validator INSTANCE = null;

    private static Validator getValidator() {
        if (INSTANCE == null) {
            INSTANCE = Validation.buildDefaultValidatorFactory().getValidator();
        }
        return INSTANCE;
    }

    public final List<ErrorCause> validate() {
        Set<ConstraintViolation<DataValidator>> violations = getValidator().validate(this);
        if (violations.isEmpty()) {
            return Collections.emptyList();
        }
        return buildCauses(violations);
    }

    private List<ErrorCause> buildCauses(Set<ConstraintViolation<DataValidator>> violations) {
        List<ErrorCause> errorCauses = new ArrayList<>();
        for (ConstraintViolation<DataValidator> violation : violations) {
            ErrorCause errorCause = new ErrorCause(violation.getPropertyPath().toString(), violation.getMessage());
            errorCauses.add(errorCause);
        }
        return errorCauses;

    }
}
