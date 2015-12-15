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

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.PagedResources;
import edu.pitt.dbmi.ccd.db.entity.Group;
import edu.pitt.dbmi.ccd.db.service.GroupService;

// logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@RestController
@RequestMapping(value="gc")
public class GroupController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupController.class);

    private final EntityLinks entityLinks;
    private final GroupService groupService;
    // private final ResourceAssemblerSupport resourceAssembler;

    @Autowired(required=true)
    public GroupController(EntityLinks entityLinks, GroupService groupService) {
        this.entityLinks = entityLinks;
        this.groupService = groupService;
        // this.resourceAssembler = new GroupResourceAssembler();
    }

    // Groups index request
    // @RequestMapping(method=RequestMethod.GET)
    // public ResponseEntity<GroupResourcesPage> index(Pageable pageable) {
    //     final Collection<Group> groups = groupService.findAll();
    //     final Link self = linkTo(methodOn(GroupController.class).index(pageable)).withSelfRel();
    //     // final Link next = linkTo(methodOn(GroupController.class).index(pageable.next())).withRel(Link.REL_NEXT).expand(pageable.next());
    //     final Link next = new Link(String.format("%s?page%d", self.toString(), pageable.getPageNumber()+1));
    //     GroupResourcesPage page = new GroupResourcesPage(groups, pageable, self);
    //     return new ResponseEntity<GroupResourcesPage>(page, HttpStatus.OK);
    // }

    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<Page<GroupResource>> index(Pageable pageable) {
        Collection<Group> groups = groupService.findAll(pageable);
        List<GroupResource> resources = groups.stream()
                                                .map(g -> new GroupResource(g))
                                                .collect(Collectors.toList());
        Page<GroupResource> page = new PageImpl<GroupResource>(resources);
        return new ResponseEntity<Page<GroupResource>>(page, HttpStatus.OK);
    }

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

    // Get group by group name
    @RequestMapping(value="/{name}", method=RequestMethod.GET)
    public ResponseEntity<GroupResource> getGroup(@PathVariable String name) {
        final Group group = groupService.findByName(name);
        final GroupResource resource;
        if (group != null) {
            resource = new GroupResource(group);
            return new ResponseEntity<GroupResource>(resource, HttpStatus.OK);
        } else {
            return new ResponseEntity<GroupResource>(HttpStatus.NOT_FOUND);
        }
    }
}