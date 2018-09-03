package com.cffex.exception;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * Created by Ming on 2015/3/8.
 */
public class JsonValidationException extends ServiceException {

    private Set<ConstraintViolation<Object>> validationResult;

    public JsonValidationException(Set<ConstraintViolation<Object>> validationResult) {
        super(400, "Bad Request");
        this.validationResult = validationResult;
    }

    public String getErrorMsg() {
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<Object> result : validationResult) {
            sb.append(result.getMessage());
        }
        return sb.toString();
    }
}
