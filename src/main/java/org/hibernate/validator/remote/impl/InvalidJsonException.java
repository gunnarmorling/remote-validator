package org.hibernate.validator.remote.impl;

/**
 * @author Gunnar Morling
 * @author Hendrik Ebbers
 */
public class InvalidJsonException extends Exception {

    public InvalidJsonException(String message) {
        super(message);
    }

    public InvalidJsonException(String message, Throwable cause) {
        super(message, cause);
    }
}
