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
package edu.pitt.dbmi.ccd.annotations.group;

import javax.servlet.http.HttpServletRequest;

import edu.pitt.dbmi.ccd.db.entity.Group;
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

    // services & components
    private final GroupLinks groupLinks;
    private final GroupResourceAssembler groupResourceAssembler;
    private final HttpServletRequest httpServletRequest;


    @Autowired(required = true)
    public GroupPagedResourcesAssembler(GroupLinks groupLinks, GroupResourceAssembler groupResourceAssembler, HttpServletRequest httpServletRequest) {
        super(null, null);
        this.groupLinks = groupLinks;
        this.groupResourceAssembler = groupResourceAssembler;
        this.httpServletRequest = httpServletRequest;
    }

    /**
     * Create PagedResources of GroupResources
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

    /**
     * Created PagedResources of GroupResources
     *
     * @param page page of entities
     * @param links links to add (optional)
     * @return PagedResources of GroupResources
     */
    public PagedResources<GroupResource> createPagedGroupResources(Page<Group> page, Link... links) {
        final PagedResources<GroupResource> groupResources = this.toResource(page, this.groupResourceAssembler, this.httpServletRequest);
        groupResources.add(links);
        return groupResources;
    }
}
