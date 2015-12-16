package edu.pitt.dbmi.ccd.anno.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.core.Relation;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import edu.pitt.dbmi.ccd.db.entity.Group;

/**
 * Group entity DTO representation with self link
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Relation(value="group", collectionRelation="groups")
public class GroupResource extends ResourceSupport {

    private EntityLinks entityLinks;

    // content
    private String name;
    private String description;

    protected GroupResource() { }

    @Autowired(required=true)
    public GroupResource(EntityLinks entityLinks) {
        this.entityLinks = entityLinks;
    }

    /**
     * Generate new GroupResource with self link
     * @param  group content
     * @param  links links to include (optional)
     * @return       generated GroupResource
     */
    public GroupResource(EntityLinks entityLinks, Group group, Link... links) {
        this(entityLinks);
        this.name = group.getName();
        this.description = group.getDescription();
        this.add(entityLinks.linkToSingleResource(GroupResource.class, this.name));
        this.add(links);
    }

    /**
     * Get group name
     * @return name
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get group description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
