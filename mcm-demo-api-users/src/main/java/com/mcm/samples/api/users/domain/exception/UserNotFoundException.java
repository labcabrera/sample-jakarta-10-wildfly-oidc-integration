package com.mcm.samples.api.users.domain.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("User not found");
    }

    public UserNotFoundException(String idUser) {
        super(String.format("User '%s' not found.", idUser));
    }

}
