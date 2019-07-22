package cs446.budgetMe.Utils;

import org.springframework.http.HttpStatus;

public class BadResponseException extends Exception {
    private HttpStatus status;
    private String errorMessage;

    public BadResponseException(HttpStatus status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.errorMessage;
    }

}
