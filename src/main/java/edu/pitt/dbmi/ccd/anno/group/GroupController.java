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

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import edu.pitt.dbmi.ccd.anno.resources.EmbeddedResourcePage;
import edu.pitt.dbmi.ccd.anno.resources.EmptyResource;
import edu.pitt.dbmi.ccd.db.entity.Group;
import edu.pitt.dbmi.ccd.db.service.GroupService;

// logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@RestController
@ExposesResourceFor(GroupResource.class)
@RequestMapping(value="gs")
public class GroupController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    private HttpServletRequest request;

    private static final GroupResourceAssembler assembler = new GroupResourceAssembler();
    private static final GroupPagedResourcesAssembler pageAssembler = new GroupPagedResourcesAssembler();
    // private static final TemplatedLinkBuilder linkBuilder = new TemplatedLinkBuilder();
    
    private final EntityLinks entityLinks;
    private final RelProvider relProvider;
    private final GroupService groupService;

    @Autowired(required=true)
    public GroupController(EntityLinks entityLinks, RelProvider relProvider, GroupService groupService) {
        this.entityLinks = entityLinks;
        this.relProvider = relProvider;
        this.groupService = groupService;
    }

    // Get all groups
    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<PagedResources<GroupResource>> groups(Pageable pageable) {
        try {
            final Link self = entityLinks.linkFor(GroupResource.class).withSelfRel();
            Page<Group> groups = groupService.findAll(pageable);
            PagedResources<GroupResource> page = pageAssembler.toResource(groups, assembler, self);
            return new ResponseEntity<PagedResources<GroupResource>>(page, HttpStatus.OK);            
        } catch (PropertyReferenceException e) {
            return new ResponseEntity<PagedResources<GroupResource>>(HttpStatus.BAD_REQUEST);
        }
    }

    // Get group by group name
    @RequestMapping(value="/{name}", method=RequestMethod.GET)
    public ResponseEntity<GroupResource> group(@PathVariable String name) {
        final Group group = groupService.findByName(name);
        final GroupResource resource;
        if (group != null) {
            resource = assembler.toResource(group);
            return new ResponseEntity<GroupResource>(resource, HttpStatus.OK);
        } else {
            return new ResponseEntity<GroupResource>(HttpStatus.NOT_FOUND);
        }
    }

    // // Search page
    // @RequestMapping(value="/search", method=RequestMethod.GET)
    // public ResponseEntity<EmptyResource> search() {
    //     EmptyResource resource = assembler.buildSearch();
    //     return new ResponseEntity<EmptyResource>(resource, HttpStatus.OK);
    // } 

    // // Search by name contains
    // @RequestMapping(value="/search/byName", method=RequestMethod.GET)
    // public ResponseEntity<EmbeddedResourcePage> searchNames(@RequestParam("terms") String terms, Pageable pageable) {
    //     try {
    //         final Link self = linkBuilder.fromRequest(request);
    //         Page<Group> groups = groupService.searchNames(terms, pageable);
    //         EmbeddedResourcePage page = assembler.toResourcePage(groups, pageable, self);
    //         return new ResponseEntity<EmbeddedResourcePage>(page, HttpStatus.OK);
    //     } catch (PropertyReferenceException e) {
    //         return new ResponseEntity<EmbeddedResourcePage>(HttpStatus.BAD_REQUEST);
    //     }
    // }

    // // Search by description contains
    // @RequestMapping(value="/search/byDescription", method=RequestMethod.GET)
    // public ResponseEntity<EmbeddedResourcePage> searchDescriptions(@RequestParam("terms") String terms, Pageable pageable) {
    //     try {
    //         final Link self = linkBuilder.fromRequest(request);
    //         Page<Group> groups = groupService.searchDescriptions(terms, pageable);
    //         EmbeddedResourcePage page = assembler.toResourcePage(groups, pageable, self);
    //         return new ResponseEntity<EmbeddedResourcePage>(page, HttpStatus.OK);
    //     } catch (PropertyReferenceException e) {
    //         return new ResponseEntity<EmbeddedResourcePage>(HttpStatus.BAD_REQUEST);
    //     }
    // }

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