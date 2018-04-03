package pers.msidolphin.mblog.exception;

import java.util.Map;

/**
 * Created by msidolphin on 2017/12/30.
 */
@SuppressWarnings("ALL")
public class InvalidParameterException extends AbstractApplicationException {

    private Object validateResult;

    public InvalidParameterException() {
    }

    public InvalidParameterException(String message, Object validateResult) {
        super(message);
        this.validateResult = validateResult;
    }

    public InvalidParameterException(String message) {
        super(message);
    }

    public Object getValidateResult() {
        return validateResult;
    }
}
