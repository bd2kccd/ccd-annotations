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

import edu.pitt.dbmi.ccd.db.entity.Group;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * Assembles page of GroupResources
 *
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Component
public class GroupPagedResourcesAssembler extends PagedResourcesAssembler<Group> {

    private final GroupLinks groupLinks;

    /**
     * Create new PagedResourcesAssembler for Group entity
     *
     * @return GroupPagedResourcesAssembler
     */
    @Autowired(required = true)
    public GroupPagedResourcesAssembler(GroupLinks groupLinks) {
        super(null, null);
        this.groupLinks = groupLinks;
    }

    /**
     * Create PagedResources of group resources
     *
     * @param page page of entities
     * @param assembler resource assembler
     * @param request request data
     * @return PagedResources of group resources
     */
    public PagedResources<GroupResource> toResource(Page<Group> page, ResourceAssembler<Group, GroupResource> assembler, HttpServletRequest request) {
        final Link self = groupLinks.getRequestLink(request);
        return this.toResource(page, assembler, self);
    }
}
