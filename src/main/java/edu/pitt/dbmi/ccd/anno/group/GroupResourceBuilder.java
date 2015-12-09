package edu.pitt.dbmi.ccd.anno.group;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import java.util.Collection;
import java.util.HashSet;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.hateoas.Link;
import edu.pitt.dbmi.ccd.db.entity.Group;

/**
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
public class GroupResourceBuilder {
    
    public GroupResourceBuilder() { }

    /**
     * Builds group resource with desired links. Each group resource includes a self referencing link.
     * @param  group Group to use as resource content
     * @param  links Links to include (optional)
     * @return       Group resource with links
     */
    public static Resource<Group> groupResourceBuilder(Group group, Link... links) {
        final Link self = linkTo(methodOn(GroupController.class).getGroup(group.getName())).withSelfRel();
        Link[] groupLinks = new Link[links.length + 1];
        groupLinks[0] = self;
        System.arraycopy(links, 0, groupLinks, 1, links.length);
        return new Resource<Group>(group, groupLinks);
    }

    /**
     * Build group resource collection with desired links. Each group resource includes a self referencing link.
     * @param  groups Groups to use as resoure collection content
     * @param  links  Links to include (optional)
     * @return        Group resource collection with links
     */
    public static Collection<Resource<Group>> groupResourceCollectionBuilder(Collection<Group> groups, Link... links) {
        final Collection<Resource<Group>> resources = new HashSet<Resource<Group>>(0);
        for (Group group : groups) {
            Resource<Group> resource = groupResourceBuilder(group, links);
            resources.add(resource);
        }
        return resources;
    }

    /**
     * Build paged group resource collection with desired links. Each group resource includes a self referencing link.
     * @param  groupResources Groups to use as page content
     * @param  pageable       Page request information
     * @param  links          Links to include (optional)
     * @return                Page of Group resources with metadata links
     */
    public static PagedResources<Resource<Group>> groupPagedResourceBuilder(Collection<Resource<Group>> groupResources, Pageable pageable, Link... links) {
        final Link self = linkTo(methodOn(GroupController.class).index(pageable)).withSelfRel();
        Link[] pageLinks = new Link[links.length + 1];
        pageLinks[0] = self;
        System.arraycopy(links, 0, pageLinks, 1, links.length);
        PageMetadata pageData = new PageMetadata(pageable.getPageSize(), pageable.getPageNumber(), groupResources.size());
        return new PagedResources<Resource<Group>>(groupResources, pageData, pageLinks);
    }
}
