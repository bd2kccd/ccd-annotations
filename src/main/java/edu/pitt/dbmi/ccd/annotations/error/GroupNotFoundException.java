package edu.pitt.dbmi.ccd.annotations.error;

/**
 * Mark Silvis (marksilvis@pitt.edu)
 */
public class GroupNotFoundException extends NotFoundException {

    private static final String GROUP = "Group";
    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String KEY = "key";

    public GroupNotFoundException(String name) {
        super(GROUP, NAME, name);
    }

    public GroupNotFoundException(Long id) {
        super(GROUP, ID, id);
    }

    public GroupNotFoundException(Object key) {
        super(GROUP, KEY, key);
    }
}
