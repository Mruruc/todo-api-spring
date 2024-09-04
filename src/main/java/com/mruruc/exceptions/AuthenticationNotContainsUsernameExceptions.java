package com.mruruc.exceptions;

/**
 *<h3> Exception when the current authentication instance does not contains the principle.</h3>
 */
public class AuthenticationNotContainsUsernameExceptions extends RuntimeException {
    public AuthenticationNotContainsUsernameExceptions(String message) {
        super(message);
    }
}
