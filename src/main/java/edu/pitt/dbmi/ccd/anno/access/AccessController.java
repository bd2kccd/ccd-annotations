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

package edu.pitt.dbmi.ccd.anno.access;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import edu.pitt.dbmi.ccd.anno.error.AccessNotFoundException;
import edu.pitt.dbmi.ccd.anno.error.NotFoundException;
import edu.pitt.dbmi.ccd.db.entity.Access;
import edu.pitt.dbmi.ccd.db.service.AccessService;

// logging

/**
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@RestController
@ExposesResourceFor(AccessResource.class)
@RequestMapping(AccessLinks.INDEX)
public class AccessController {
    
    // loggers
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessController.class);

    // servlet
    private final HttpServletRequest request;

    // services and components
    private final AccessLinks accessLinks;
    private final AccessService accessService;
    private final AccessResourceAssembler assembler;
    private final AccessPagedResourcesAssembler pageAssembler;    

    @Autowired(required=true)
    public AccessController(
            HttpServletRequest request,
            AccessLinks accessLinks,
            AccessService accessService,
            AccessResourceAssembler assembler,
            AccessPagedResourcesAssembler pageAssembler) {
        this.request = request;
        this.accessLinks = accessLinks;
        this.accessService = accessService;
        this.assembler = assembler;
        this.pageAssembler = pageAssembler;
    }

    /* GET requests */

    /**
     * Get all accesses
     * @param  pageable page request
     * @return          page of accesss
     */
    @RequestMapping(method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PagedResources<AccessResource> accesses(Pageable pageable) {
            final Page<Access> page = accessService.findAll(pageable);
            final PagedResources<AccessResource> pagedResources = pageAssembler.toResource(page, assembler, request);
            return pagedResources;
    }

    /** 
     * Get access
     * @param name access name
     * @return access
     */
    @RequestMapping(value=AccessLinks.ACCESS, method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AccessResource access(@PathVariable String name) throws NotFoundException {
        final Access access = accessService.findByName(name).orElseThrow(() -> new AccessNotFoundException(name));
        final AccessResource resource = assembler.toResource(access);
        return resource;
    }
}