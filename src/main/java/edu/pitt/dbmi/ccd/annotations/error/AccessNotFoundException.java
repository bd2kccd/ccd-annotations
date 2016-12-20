package edu.pitt.dbmi.ccd.annotations.error;

/**
 * Mark Silvis (marksilvis@pitt.edu)
 */
public class AccessNotFoundException extends NotFoundException {

    private static final String ACCESS = "Access";
    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String KEY = "key";

    public AccessNotFoundException(final String name) {
        super(ACCESS, NAME, name);
    }

    public AccessNotFoundException(final Long id) {
        super(ACCESS, ID, id);
    }

    public AccessNotFoundException(final Object key) {
        super(ACCESS, KEY, key);
    }
}
