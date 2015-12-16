package edu.pitt.dbmi.ccd.anno.group;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.ArrayList;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.hateoas.Link;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import edu.pitt.dbmi.ccd.anno.resources.EmbeddedResourcePage;
import edu.pitt.dbmi.ccd.anno.resources.EmptyResource;
// import edu.pitt.dbmi.ccd.anno.links.TemplatedLinkBuilder;
import edu.pitt.dbmi.ccd.db.entity.Group;

/**
 * Assembles Groups into their DTO (GroupResource)
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
public class GroupResourceAssembler extends ResourceAssemblerSupport<Group, GroupResource> {

    // private static TemplatedLinkBuilder linkBuilder = new TemplatedLinkBuilder();

    public GroupResourceAssembler() {
        super(GroupController.class, GroupResource.class);
    }

    /**
     * convert Group to GroupResource
     * @param  group entity
     * @return       GroupResource
     */
    @Override
    public GroupResource toResource(Group group) {
        GroupResource resource = createResourceWithId(group.getName(), group);
        resource.setName(group.getName());
        resource.setDescription(group.getDescription());
        return resource;
    }

    /**
     * convert Group to GroupResource
     * @param  group entity
     * @param  links (optional) links to include
     * @return       GroupResource
     */
    public GroupResource toResource(Group group, Link... links) {
        GroupResource resource = toResource(group);
        resource.add(links);
        return resource;
    }

    /**
     * convert Groups to GroupResources
     * @param  groups entities
     * @return        List of GroupResources
     */
    @Override
    public List<GroupResource> toResources(Iterable<? extends Group> groups) {
        List<GroupResource> result = new ArrayList<>();
        for (Group g : groups) {
            result.add(toResource(g));
        }
        return result;
    }

    /**
     * convert Groups to Page of GroupResources
     * @param  groups   entities
     * @param  pageable page request data
     * @param  self     link to self
     * @param  links    (optional) links to include
     * @return          Page with embedded GroupResources
     */
    public EmbeddedResourcePage toResourcePage(Page<? extends Group> groups, Pageable pageable, Link self, Link... links) {
        List<GroupResource> resources = toResources(groups);
        PageMetadata pageMetadata = new PageMetadata(groups.getSize(), groups.getNumber(), groups.getTotalElements(), groups.getTotalPages());
        return new EmbeddedResourcePage<GroupResource>(resources, pageMetadata, self, links);
    }

    // /**
    //  * construct GroupController search endpoint
    //  * @param  links (optional) links to include
    //  * @return       GroupController search endpoint
    //  */
    // public EmptyResource buildSearch(Link... links) {
    //     Pageable pageable = new PageRequest(0, 10);
    //     EmptyResource resource = new EmptyResource();
    //     resource.add(
    //         linkBuilder.templatedLinkPageable(linkTo(GroupController.class).slash("search").slash("byName").withRel("byName"), "terms"),
    //         linkBuilder.templatedLinkPageable(linkTo(GroupController.class).slash("search").slash("byDescription").withRel("byDescription"), "terms"),
    //         linkTo(methodOn(GroupController.class).search()).withSelfRel()
    //     );
    //     resource.add(links);
    //     return resource;
    // }
}
