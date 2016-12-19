package edu.pitt.dbmi.ccd.annotations.error;

/**
 * Mark Silvis (marksilvis@pitt.edu)
 */
public class UserNotFoundException extends NotFoundException {

    private static final String USER = "User";
    private static final String ID = "id";

    public UserNotFoundException(String id) {
        super(USER, ID, id);
    }

    public UserNotFoundException(String field, String value) {
        super(USER, field, value);
    }

    public UserNotFoundException(Long id) {
        super(USER, ID, id);
    }
}
