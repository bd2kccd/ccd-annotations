package edu.pitt.dbmi.ccd.anno.group;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.core.Relation;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.pitt.dbmi.ccd.db.entity.Group;

/**
 * Group entity DTO representation with self link
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Relation(value="group", collectionRelation="groups")
public class GroupResource extends ResourceSupport {

    // content
    private final String name;
    private final String description;

    // links
    private final Link self;

    /**
     * Generate new GroupResource with self link
     * @param  group content
     * @param  links links to include (optional)
     * @return       generated GroupResource
     */
    public GroupResource(Group group, Link... links) {
        this.name = group.getName();
        this.description = group.getDescription();
        this.self = linkTo(methodOn(GroupController.class).getGroup(name)).withSelfRel();
        this.add(self);
        this.add(links);
    }

    /* content */

    /**
     * Get group name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get group description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /* links */

    /**
     * Get group link
     * @return link
     */
    @JsonIgnore
    public Link getLink() {
        return self;
    }
}
