/*
 * Copyright (C) 2015 University of Pittsburgh.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

package edu.pitt.dbmi.ccd.anno.group;

import java.util.List;
import java.util.stream.StreamSupport;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.util.Assert;
import edu.pitt.dbmi.ccd.db.entity.Group;

/**
 * Assembles Group into GroupResource
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Component
public class GroupResourceAssembler extends ResourceAssemblerSupport<Group, GroupResource> {

    private final GroupLinks groupLinks;

    @Autowired(required=true)
    public GroupResourceAssembler(GroupLinks groupLinks) {
        super(GroupController.class, GroupResource.class);
        this.groupLinks = groupLinks;
    }

    /**
     * convert Group to GroupResource
     * @param  group entity
     * @return       resource
     */
    @Override
    public GroupResource toResource(Group group) {
        Assert.notNull(group);
        GroupResource resource = createResourceWithId(group.getName(), group);
        resource.add(groupLinks.join(group));
        resource.add(groupLinks.leave(group));
        resource.add(groupLinks.mods(group));
        resource.add(groupLinks.members(group));
        resource.add(groupLinks.requesters(group));
        resource.add(groupLinks.annotations(group));
        return resource;
    }

    /**
     * convert Groups to GroupResources
     * @param  groups entities
     * @return        List of resources
     */
    @Override
    public List<GroupResource> toResources(Iterable<? extends Group> groups) {
        Assert.isTrue(groups.iterator().hasNext());
        return StreamSupport.stream(groups.spliterator(), false)
                            .map(this::toResource)
                            .collect(Collectors.toList());
    }

    /**
     * Instantiate GroupResource with non-default constructor
     * @param  group entity
     * @return       resource
     */
    @Override
    protected GroupResource instantiateResource(Group group) {
        Assert.notNull(group);
        try {
            return BeanUtils.instantiateClass(GroupResource.class.getConstructor(Group.class), group);
        } catch(NoSuchMethodException ex) {
            ex.printStackTrace();
            return new GroupResource();
        }
    }
}
