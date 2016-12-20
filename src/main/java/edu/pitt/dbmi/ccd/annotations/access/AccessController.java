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
package edu.pitt.dbmi.ccd.annotations.access;

import javax.servlet.http.HttpServletRequest;

import edu.pitt.dbmi.ccd.db.entity.Access;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    private final AccessRestService accessRestService;
    private final AccessResourceAssembler assembler;
    private final AccessPagedResourcesAssembler pageAssembler;

    @Autowired(required = true)
    public AccessController(
            HttpServletRequest request,
            AccessLinks accessLinks,
            AccessRestService accessRestService,
            AccessResourceAssembler assembler,
            AccessPagedResourcesAssembler pageAssembler) {
        this.request = request;
        this.accessLinks = accessLinks;
        this.accessRestService = accessRestService;
        this.assembler = assembler;
        this.pageAssembler = pageAssembler;
    }

    /* GET requests */

    /**
     * Get all accesses
     *
     * @param pageable page request
     * @return page of accesses
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PagedResources<AccessResource> accesses(Pageable pageable) {
        final Page<Access> page = accessRestService.findAll(pageable);
        return pageAssembler.toResource(page, assembler, request);
    }

    /**
     * Get access
     *
     * @param id access id
     * @return access
     */
    @RequestMapping(value = AccessLinks.ACCESS, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AccessResource access(@PathVariable final Long id) {
        final Access access = accessRestService.findById(id);
        return assembler.toResource(access);
    }
}
