package org.matrix.framework.core.platform.web;

import java.util.List;
import java.util.Locale;
import org.matrix.framework.core.platform.exception.InvalidRequestException;
import org.matrix.framework.core.platform.exception.ResourceNotFoundException;
import org.matrix.framework.core.platform.exception.UserAuthorizationException;
import org.matrix.framework.core.platform.web.rest.MatrixResponse;
import org.matrix.framework.core.util.ExceptionUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class MatrixInternetController extends DefaultController {

    private static final String UNKNOWN = "UNKNOWN";

    private static final String  ARGUMENTS_INVALID = "ARGUMENTS_INVALID";
    
    private static final String  NOT_FOUND = "NOT_FOUND";
    
    private static final String  UN_AUTHORIZED = "UNAUTHORIZED";
        
    @ExceptionHandler
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public MatrixResponse error(Throwable e) {
        MatrixResponse error = new MatrixResponse();
        error.setErrorCode(UNKNOWN);
        error.setMessage(e.getMessage());
        error.setExceptionTrace(ExceptionUtils.stackTrace(e));
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public MatrixResponse validationError(MethodArgumentNotValidException e) {
        return createValidationResponse(e.getBindingResult());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public MatrixResponse validationError(BindException e) {
        return createValidationResponse(e.getBindingResult());
    }

    private MatrixResponse createValidationResponse(BindingResult bindingResult) {
        MatrixResponse error = new MatrixResponse();
        Locale locale = LocaleContextHolder.getLocale();
        List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuilder msg = new StringBuilder();
        for (org.springframework.validation.FieldError fieldError : fieldErrors) {
            msg.append(fieldError.getField()).append(":").append(messages.getMessage(fieldError, locale));
            break;
        }
        error.setErrorCode(ARGUMENTS_INVALID);
        error.setMessage(msg.toString());
        error.setExceptionTrace(msg.toString());
        return error;
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public MatrixResponse validationError(InvalidRequestException e) {
        Locale locale = LocaleContextHolder.getLocale();
        MatrixResponse error = new MatrixResponse();
        StringBuilder msg = new StringBuilder();
        msg.append(e.getField()).append(":").append(messages.getMessage(e.getMessage(), locale));
        error.setErrorCode(ARGUMENTS_INVALID);
        error.setMessage(msg.toString());
        error.setExceptionTrace(msg.toString());
        return error;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public MatrixResponse notFound(ResourceNotFoundException e) {
        MatrixResponse error = new MatrixResponse();
        error.setErrorCode(NOT_FOUND);
        error.setMessage(e.getMessage());
        error.setExceptionTrace(ExceptionUtils.stackTrace(e));
        return error;
    }

    @ExceptionHandler(UserAuthorizationException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public MatrixResponse unauthorized(UserAuthorizationException e) {
        MatrixResponse error = new MatrixResponse();
        error.setErrorCode(UN_AUTHORIZED);
        error.setMessage(e.getMessage());
        error.setExceptionTrace(ExceptionUtils.stackTrace(e));
        return error;
    }

}