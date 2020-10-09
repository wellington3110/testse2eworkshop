package br.com.wellingtonmacedo.testsworkshop.validator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@Accessors(fluent = true)
public class ErrorCause {
    private String field;
    private String cause;
}
