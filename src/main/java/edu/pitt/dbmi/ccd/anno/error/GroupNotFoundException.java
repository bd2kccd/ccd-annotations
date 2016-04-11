package edu.pitt.dbmi.ccd.anno.error;

/**
 * Mark Silvis (marksilvis@pitt.edu)
 */
public class GroupNotFoundException extends NotFoundException {

    private static final String GROUP = "Group";
    private static final String NAME = "name";
    private static final String ID = "id";

    public GroupNotFoundException(String name) {
        super(GROUP, NAME, name);
    }

    public GroupNotFoundException(Long id) {
        super(GROUP, ID, id);
    }
}
