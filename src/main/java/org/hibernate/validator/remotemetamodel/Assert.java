package org.hibernate.validator.remotemetamodel;

import java.util.Objects;

public class Assert {

    private static final String NOT_NULL_MSG_FORMAT = "Argument '%s' may not be null";

    public static <T> T requireNonNull(T value, String argumentName) {
        Objects.requireNonNull(argumentName, String.format(NOT_NULL_MSG_FORMAT, "argumentName"));

        return Objects.requireNonNull(value, String.format(NOT_NULL_MSG_FORMAT, argumentName));
    }
}
