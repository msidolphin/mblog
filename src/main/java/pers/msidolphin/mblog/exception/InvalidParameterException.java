package pers.msidolphin.mblog.exception;

import java.util.Map;

/**
 * Created by msidolphin on 2017/12/30.
 */
@SuppressWarnings("ALL")
public class InvalidParameterException extends AbstractApplicationException {

    private Map<String, String> validateResult;

    public InvalidParameterException() {
    }

    public InvalidParameterException(String message, Map<String, String> validateResult) {
        super(message);
        this.validateResult = validateResult;
    }

    public InvalidParameterException(String message) {
        super(message);
    }

    public Map<String, String> getValidateResult() {
        return validateResult;
    }
}
