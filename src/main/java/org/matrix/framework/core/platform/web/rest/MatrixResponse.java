package org.matrix.framework.core.platform.web.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class MatrixResponse<T> {
    
    @XmlElement(name = "status")
    private String status = "FAILED";
    
    @XmlElement(name = "result")
    private T result;
    
    @XmlElement(name = "error_code")
    private String errorCode;

    @XmlElement(name = "message")
    private String message;
    
    @XmlElement(name = "exception_trace")
    private String exceptionTrace;

    public static <T> MatrixResponse<T> createMatrixResponse(T result) {
        MatrixResponse<T> response = new MatrixResponse<T>();
        response.setStatus("SUCESS");
        response.setResult(result);
        return response;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExceptionTrace() {
        return exceptionTrace;
    }

    public void setExceptionTrace(String exceptionTrace) {
        this.exceptionTrace = exceptionTrace;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}