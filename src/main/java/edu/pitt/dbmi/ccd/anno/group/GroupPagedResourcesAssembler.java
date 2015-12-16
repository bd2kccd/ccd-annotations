package edu.pitt.dbmi.ccd.anno.group;

import org.springframework.data.web.PagedResourcesAssembler;
import edu.pitt.dbmi.ccd.db.entity.Group;

/**
 * Assembles page of GroupResources
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
public class GroupPagedResourcesAssembler extends PagedResourcesAssembler<Group> {

    /**
     * Create new PagedResourcesAssembler for Group entity
     * @return GroupPagedResourcesAssembler
     */
    public GroupPagedResourcesAssembler() {
        super(null, null);
    }
}
