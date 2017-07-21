package org.hibernate.validator.remotemetamodel.impl;

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
