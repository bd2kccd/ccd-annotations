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

package edu.pitt.dbmi.ccd.anno.data;

import static edu.pitt.dbmi.ccd.anno.util.ControllerUtils.formatParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.pitt.dbmi.ccd.anno.error.AnnotationTargetNotFoundException;
import edu.pitt.dbmi.ccd.anno.error.NotFoundException;
import edu.pitt.dbmi.ccd.db.entity.AnnotationTarget;
import edu.pitt.dbmi.ccd.db.entity.UserAccount;
import edu.pitt.dbmi.ccd.db.service.AnnotationTargetService;
import edu.pitt.dbmi.ccd.security.userDetails.UserAccountDetails;

/**
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@RestController
@ExposesResourceFor(AnnotationTargetResource.class)
@RequestMapping(value=AnnotationTargetLinks.INDEX)
public class AnnotationTargetController {
    
    // loggers
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationTargetController.class);

    // servlet
    private final HttpServletRequest request;

    // services and components
    private final AnnotationTargetLinks annotationTargetLinks;
    private final AnnotationTargetService annotationTargetService;
    private final AnnotationTargetResourceAssembler assembler;
    private final AnnotationTargetPagedResourcesAssembler pageAssembler;

    @Autowired(required=true)
    public AnnotationTargetController(
            HttpServletRequest request,
            AnnotationTargetLinks annotationTargetLinks,
            AnnotationTargetService annotationTargetService,
            AnnotationTargetResourceAssembler assembler,
            AnnotationTargetPagedResourcesAssembler pageAssembler) {
        this.request = request;
        this.annotationTargetLinks = annotationTargetLinks;
        this.annotationTargetService = annotationTargetService;
        this.assembler = assembler;
        this.pageAssembler = pageAssembler;
    }

    /* GET requests */

    /**
     * Get all AnnotationTargets
     * @param  username AnnotationTargeter (nullable)
     * @param  type     AnnotationTarget type (nullable)
     * @param pageable  page request
     * @return          page of AnnotationTargets
     */
    @RequestMapping(method= RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PagedResources<AnnotationTargetResource> AnnotationTargets(@RequestParam(value="user", required=false) String username, @RequestParam(value="type", required=false) String type, Pageable pageable) {
        final Page<AnnotationTarget> page = annotationTargetService.filter(username, type, pageable);
        final PagedResources<AnnotationTargetResource> pagedResources = pageAssembler.toResource(page, assembler, request);
        pagedResources.add(annotationTargetLinks.search());
        return pagedResources;
    }

    /**
     * Get single AnnotationTarget
     * @param  id AnnotationTarget id
     * @return    AnnotationTarget
     */
    @RequestMapping(value=AnnotationTargetLinks.DATA, method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AnnotationTargetResource AnnotationTarget(@PathVariable Long id) throws NotFoundException {
        final AnnotationTarget annotationTarget = annotationTargetService.findById(id);
        if (annotationTarget == null) {
            throw new AnnotationTargetNotFoundException(id);
        }
        final AnnotationTargetResource resource = assembler.toResource(annotationTarget);
        return resource;
    }

    /**
     * Search for AnnotationTargets
     * @param  username AnnotationTargeter (nullable)
     * @param  type     AnnotationTarget type (nullable)
     * @param  query    search terms (nullable)
     * @param  not      negated search terms (nullable)
     * @param  pageable page request
     * @return          page of AnnotationTargets matching parameters
     */
    @RequestMapping(value=AnnotationTargetLinks.SEARCH, method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PagedResources<AnnotationTargetResource> search(
            @RequestParam(value="user", required=false) String username,
            @RequestParam(value="type", required=false) String type,
            @RequestParam(value="query", required=false) String query,
            @RequestParam(value="not", required=false) String not,
            Pageable pageable) {
        final Set<String> matches = (query != null)
                                  ? new HashSet<>(formatParam(query))
                                  : null;
        final Set<String> nots = (not != null)
                               ? new HashSet<>(formatParam(not))
                               : null;
        final Page<AnnotationTarget> page = annotationTargetService.search(username, type, matches, nots, pageable);
        final PagedResources<AnnotationTargetResource> pagedResources = pageAssembler.toResource(page, assembler, request);
        return pagedResources;
    }

    /* POST requests */

    /**
     * Create new AnnotationTarget (only allows URLs)
     * @param principal requester
     * @param form      AnnotationTarget content
     * @return          AnnotationTarget
     */
    @RequestMapping(method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public AnnotationTargetResource create(@AuthenticationPrincipal UserAccountDetails principal, @RequestBody @Valid AnnotationTargetForm form) {
        UserAccount requester = principal.getUserAccount();
        AnnotationTarget annotationTarget = new AnnotationTarget(requester, form.getTitle(), form.getAddress());
        annotationTarget = annotationTargetService.save(annotationTarget);
        final AnnotationTargetResource resource = assembler.toResource(annotationTarget);
        return resource;
    }

//    @RequestMapping(method=RequestMethod.DELETE)
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @ResponseBody
//    public void delete(@AuthenticationPrincipal UserAccountDetails userAccountDetails, @PathVariable Long id) {
//        UserAccount principal = userAccountDetails.getUserAccount();
//        AnnotationTarget AnnotationTarget = annotationTargetService.findById(id).orElseThrow(() -> new NotFoundException("AnnotationTarget", "id", id));
//
//    }

    // I don't think this is necessary
//    /* PUT requests */
//
//    /**
//     * Alias for newAnnotationTarget with HTTP method PUT
//     */
//    @RequestMapping(method=RequestMethod.PUT)
//    @ResponseStatus(HttpStatus.CREATED)
//    @ResponseBody
//    public AnnotationTargetResource newAnnotationTargetPUT(@AuthenticationPrincipal UserAccount principal, @RequestBody @Valid AnnotationTargetForm form) {
//        return newAnnotationTarget(principal, form);
//    }
}