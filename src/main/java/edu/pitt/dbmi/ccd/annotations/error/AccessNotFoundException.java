package edu.pitt.dbmi.ccd.annotations.error;

/**
 * Mark Silvis (marksilvis@pitt.edu)
 */
public class AccessNotFoundException extends NotFoundException {

    private static final String ACCESS = "access";
    private static final String NAME = "name";
    private static final String ID = "id";

    public AccessNotFoundException(String name) {
        super(ACCESS, NAME, name);
    }

    public AccessNotFoundException(Long id) {
        super(ACCESS, ID, id);
    }
}
