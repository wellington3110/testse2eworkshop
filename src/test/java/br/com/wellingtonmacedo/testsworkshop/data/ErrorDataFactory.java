package br.com.wellingtonmacedo.testsworkshop.data;

import br.com.wellingtonmacedo.testsworkshop.dto.ErrorDTO;

import static java.lang.String.format;

public class ErrorDataFactory {

    private ErrorDataFactory() {}

    private ErrorDTO error = new ErrorDTO();

    public static ErrorDataFactory validationError() {
        ErrorDataFactory builder = new ErrorDataFactory();
        builder.error.setCode("validation_error");
        return builder;
    }

    public ErrorDataFactory mustNotBeBlank(String field) {
        error.addError(field, "must not be blank");
        return this;
    }

    public ErrorDataFactory mustNotBeNull(String field) {
        error.addError(field, "must not be null");
        return this;
    }

    public ErrorDataFactory invalidSize(String field, Integer min, Integer max) {
        error.addError(field, format("size must be between %d and %d", min, max));
        return this;
    }

    public ErrorDataFactory invalidCpf() {
        error.addError("cpf", "invalid Brazilian individual taxpayer registry number (CPF)");
        return this;
    }

    public ErrorDataFactory invalidEmail() {
        error.addError("email", "must be a well-formed email address");
        return this;
    }

    public ErrorDataFactory mustBeADateInThePastOrInThePresent(String field) {
        error.addError(field, "must be a date in the past or in the present");
        return this;
    }

    public ErrorDataFactory repeatedField(String field) {
        error.addError(field, "value already registered");
        return this;
    }

    public ErrorDTO build() {
        return error;
    }
}
