package br.com.wellingtonmacedo.testsworkshop.dto;

import br.com.wellingtonmacedo.testsworkshop.validator.ErrorCause;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Data
@NoArgsConstructor
public class ErrorDTO {

    private String code;
    private Map<String, String> causes = new HashMap<>();

    public ErrorDTO(String code, List<ErrorCause> errorCauses) {
        this.code = code;
        this.causes = errorCauses.stream().collect(toMap(ErrorCause::field, ErrorCause::cause));
    }

    public ErrorDTO addError(String field, String cause) {
        causes.put(field, cause);
        return this;
    }
}
