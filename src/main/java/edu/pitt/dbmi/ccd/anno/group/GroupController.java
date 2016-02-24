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

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.Link;
import edu.pitt.dbmi.ccd.db.entity.Group;
import edu.pitt.dbmi.ccd.db.service.GroupService;
import edu.pitt.dbmi.ccd.anno.user.UserResource;
import edu.pitt.dbmi.ccd.anno.user.UserResourceAssembler;

// logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for Group endpoints
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@RestController
@ExposesResourceFor(GroupResource.class)
@RequestMapping(value=GroupLinks.INDEX)
public class GroupController {
    
    // loggers
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupController.class);

    // servlet
    private final HttpServletRequest request;
    
    // services and components
    private final GroupLinks groupLinks;
    private final GroupService groupService;
    private final GroupResourceAssembler assembler;
    private final UserResourceAssembler userAssembler;
    private final GroupPagedResourcesAssembler pageAssembler;    

    @Autowired(required=true)
    public GroupController(
            HttpServletRequest request,
            GroupLinks groupLinks,
            GroupService groupService,
            GroupResourceAssembler assembler,
            UserResourceAssembler userAssembler,
            GroupPagedResourcesAssembler pageAssembler) {
        this.request = request;
        this.groupLinks = groupLinks;
        this.groupService = groupService;
        this.assembler = assembler;
        this.userAssembler = userAssembler;
        this.pageAssembler = pageAssembler;
    }

    /* GET requests */

    /**
     * Get all groups
     * @param  pageable page request
     * @return          page of groups
     */
    @RequestMapping(method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PagedResources<GroupResource> groups(Pageable pageable) {
        final Page<Group> page = groupService.findAll(pageable);
        final PagedResources<GroupResource> pagedResources = pageAssembler.toResource(page, assembler, request);
        pagedResources.add(groupLinks.search());
        return pagedResources;            
    }

    /**
     * Get single group
     * @param name group name
     * @return     group
     */
    @RequestMapping(value=GroupLinks.GROUP, method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GroupResource group(@PathVariable String name) {
        final Group group = groupService.findByName(name);
        final GroupResource resource = assembler.toResource(group);
        return resource;
    }

    /**
     * Get group admins
     * @param  name group name
     * @return      admins
     */
    @RequestMapping(value=GroupLinks.ADMINS, method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Resources<UserResource> admins(@PathVariable String name) {
        final Group group = groupService.findByName(name);
        final Set<UserResource> admins = group.getAdmins().stream()
                                                          .map(userAssembler::toResource)
                                                          .collect(Collectors.toSet());
        return new Resources<UserResource>(admins, new Link(request.getRequestURL().toString()), groupLinks.group(group));
    }

    /**
     * Search for groups
     * @param  query    search terms (nullable)
     * @param  not      negated search terms (nullable)
     * @param  pageable page request
     * @return          page of groups matching parameters
     */
    @RequestMapping(value=GroupLinks.SEARCH, method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PagedResources<GroupResource> test(
            @RequestParam(value="query", required=false) String query,
            @RequestParam(value="not", required=false) String not,
            Pageable pageable) {
        final Set<String> matches = (query != null) ? new HashSet<>(Arrays.asList(query.trim().split("\\s+")))
                                                    : null;
        final Set<String> nots = (not != null) ? new HashSet<>(Arrays.asList(not.trim().split("\\s+")))
                                               : null;
        final Page<Group> page = groupService.search(matches, nots, pageable);
        final PagedResources<GroupResource> pagedResources = pageAssembler.toResource(page, assembler, request);
        return pagedResources;
    }

    /* POST requests */

    /* PUT requests */

    /* PATCH requests */
}
