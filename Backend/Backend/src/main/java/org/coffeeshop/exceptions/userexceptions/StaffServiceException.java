package org.coffeeshop.exceptions.userexceptions;

public class StaffServiceException extends  RuntimeException {
    public StaffServiceException(String message) {
        super(message);
    }

    public StaffServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
