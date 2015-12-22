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

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Link;
import edu.pitt.dbmi.ccd.db.entity.Group;
import edu.pitt.dbmi.ccd.db.service.GroupService;
import edu.pitt.dbmi.ccd.anno.resources.EmptyResource;

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
    private final GroupService groupService;
    private final GroupResourceAssembler assembler;
    private final GroupPagedResourcesAssembler pageAssembler;    
    private final GroupLinks groupLinks;

    @Autowired(required=true)
    public GroupController(HttpServletRequest request,
                           GroupService groupService,
                           GroupResourceAssembler assembler,
                           GroupPagedResourcesAssembler pageAssembler,
                           GroupLinks groupLinks) {
        this.request = request;
        this.groupService = groupService;
        this.assembler = assembler;
        this.pageAssembler = pageAssembler;
        this.groupLinks = groupLinks;
    }

    /* GET requests */

    /**
     * Get all groups
     * @param  pageable page request
     * @return          a page of groups
     */
    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<PagedResources<GroupResource>> groups(Pageable pageable) {
        try {
            Page<Group> page = groupService.findAll(pageable);
            PagedResources<GroupResource> pagedResources = pageAssembler.toResource(page, assembler, request);
            pagedResources.add(groupLinks.search());
            return new ResponseEntity<>(pagedResources, HttpStatus.OK);            
        } catch (PropertyReferenceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get single group
     * @param name group name
     * @return     single group if found
     *             404 if not
     */
    @RequestMapping(value=GroupLinks.GROUP, method=RequestMethod.GET)
    public ResponseEntity<GroupResource> group(@PathVariable String name) {
        final Optional<Group> group = groupService.findByName(name);
        if (group.isPresent()) {
            final GroupResource resource = assembler.toResource(group.get());
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Group search page
     * @return search links
     */
    @RequestMapping(value=GroupLinks.SEARCH, method=RequestMethod.GET)
    public ResponseEntity<EmptyResource> search() {
        final Link self = groupLinks.search().withSelfRel();
        EmptyResource resource = new EmptyResource(self);
        resource.add(
            groupLinks.nameStartsWith(),
            groupLinks.nameContains(),
            groupLinks.descriptionContains()
        );
        return new ResponseEntity<>(resource, HttpStatus.OK);
    } 

    /**
     * Search for groups whose name starts with terms
     * @param  terms    terms to search for
     * @param  pageable page request
     * @return          matching groups
     */
    @RequestMapping(value=GroupLinks.SEARCH+GroupLinks.NAME_STARTS, method=RequestMethod.GET)
    public ResponseEntity<PagedResources<GroupResource>> findByNameStartsWith(@RequestParam("terms") String terms, Pageable pageable) {
        try {
            Page<Group> page = groupService.findByNameStartsWith(terms, pageable);
            PagedResources<GroupResource> pagedResources = pageAssembler.toResource(page, assembler, request);
            return new ResponseEntity<>(pagedResources, HttpStatus.OK);
        } catch (PropertyReferenceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Search for groups whose name contains terms
     * @param  terms    terms to search for
     * @param  pageable page request
     * @return          matching groups
     */
    @RequestMapping(value=GroupLinks.SEARCH+GroupLinks.NAME_CONTAINS, method=RequestMethod.GET)
    public ResponseEntity<PagedResources<GroupResource>> findByNameContains(@RequestParam("terms") String terms, Pageable pageable) {
        try {
            Page<Group> page = groupService.findByNameContains(terms, pageable);
            PagedResources<GroupResource> pagedResources = pageAssembler.toResource(page, assembler, request);
            return new ResponseEntity<>(pagedResources, HttpStatus.OK);
        } catch (PropertyReferenceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Search for groups whose description contains terms
     * @param  terms    terms to search for
     * @param  pageable page request
     * @return          matching groups
     */
    @RequestMapping(value=GroupLinks.SEARCH+GroupLinks.DESCRIPTION_CONTAINS, method=RequestMethod.GET)
    public ResponseEntity<PagedResources<GroupResource>> findByDescriptionContains(@RequestParam("terms") String terms, Pageable pageable) {
        try {
            Page<Group> page = groupService.findByDescriptionContains(terms, pageable);
            PagedResources<GroupResource> pagedResources = pageAssembler.toResource(page, assembler, request);
            return new ResponseEntity<>(pagedResources, HttpStatus.OK);
        } catch (PropertyReferenceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /* POST requests */

    /* PUT requests */

    /* PATCH requests */

    // Get group by group ID
    // @RequestMapping(value="/{id}", method=RequestMethod.GET)
    // public ResponseEntity<Resource<Group>> getGroup(@PathVariable Long id) {
    //     final Group group = groupService.findOne(id);
    //     final Resource<Group> resource;
    //     if (group != null) {
    //         resource = groupResourceBuilder(group); 
    //         return new ResponseEntity<Resource<Group>>(resource, HttpStatus.OK);
    //     } else {
    //         throw new ResourceNotFoundException(String.format("No group found with ID: %d", id));
    //        // return new ResponseEntity<Resource<Group>>(HttpStatus.NOT_FOUND);
    //     }
    // }
}
