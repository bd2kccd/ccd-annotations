package edu.pitt.dbmi.ccd.anno.group;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.hateoas.Link;
import edu.pitt.dbmi.ccd.db.entity.Group;

public class GroupResourceAssembler extends ResourceAssemblerSupport<Group, GroupResource> {

    public GroupResourceAssembler() {
        super(GroupController.class, GroupResource.class);
    }

    @Override
    public GroupResource toResource(Group group) {
        return new GroupResource(group);
    }

    public GroupResource toResource(Group group, Link... links) {
        return new GroupResource(group, links);
    }
}
