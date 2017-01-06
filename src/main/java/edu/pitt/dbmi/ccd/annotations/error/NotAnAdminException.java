package edu.pitt.dbmi.ccd.annotations.error;

import edu.pitt.dbmi.ccd.db.entity.UserAccount;

/**
 * @author Mark Silvis (marksilvis@pit.edu))
 */
public class NotAnAdminException extends RuntimeException {

    private static final String MESSAGE = "User %s must be an administrator to access this resource";

    private final String username;

    public NotAnAdminException(UserAccount user) {
        super();
        this.username = user.getUsername();
    }

    @Override
    public String getMessage() {
        return String.format(MESSAGE, username);
    }
}