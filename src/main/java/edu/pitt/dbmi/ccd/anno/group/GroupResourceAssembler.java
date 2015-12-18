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
import java.util.ArrayList;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import edu.pitt.dbmi.ccd.db.entity.Group;

/**
 * Assembles Group into GroupResource
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
public class GroupResourceAssembler extends ResourceAssemblerSupport<Group, GroupResource> {

    public GroupResourceAssembler() {
        super(GroupController.class, GroupResource.class);
    }

    /**
     * convert Group to GroupResource
     * @param  group entity
     * @return       resources
     */
    @Override
    public GroupResource toResource(Group group) {
        Assert.notNull(group);
        GroupResource resource = createResourceWithId(group.getName(), group);
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
        List<GroupResource> result = new ArrayList<>();
        groups.forEach(g -> result.add(toResource(g)));
        return result;
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
        } catch(NoSuchMethodException nsme) {
            nsme.printStackTrace();
            return new GroupResource();
        }
    }
}
