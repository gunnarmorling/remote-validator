package org.hibernate.validator.remote.impl;

import java.util.Objects;

public class Assert {

    private static final String NOT_NULL_MSG_FORMAT = "Argument '%s' may not be null";
    private static final String NOT_EMPTY_MSG_FORMAT = "Argument '%s' may not be empty";

    public static <T> T requireNonNull(T value, String argumentName) {
        Objects.requireNonNull(argumentName, String.format(NOT_NULL_MSG_FORMAT, "argumentName"));

        return Objects.requireNonNull(value, String.format(NOT_NULL_MSG_FORMAT, argumentName));
    }

    public static String requireNonBlank(String str, String argumentName) {
        requireNonNull(str, argumentName);

        if (isBlank(str)) {
            throw new IllegalArgumentException(String.format(NOT_EMPTY_MSG_FORMAT, argumentName));
        }
        return str;
    }

    /**
     * <p>Determines whether a given string is <code>null</code>, empty,
     * or only contains whitespace. If it contains anything other than
     * whitespace then the string is not considered to be blank and the
     * method returns <code>false</code>.</p>
     *
     * @param str The string to test.
     * @return <code>true</code> if the string is <code>null</code>, or
     * blank.
     */
    public static boolean isBlank(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isWhitespace(c)) {
                return false;
            }
        }
        return true;
    }
}
