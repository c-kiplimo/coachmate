package ke.natujenge.baked.web.rest.errors;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RuntimeException {
    private HttpStatus httpStatus;
    private int httpStatusCode;
    private String message;

    public BadRequestException(String message) {
        this.message = message;
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.httpStatusCode = HttpStatus.BAD_REQUEST.value();
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
