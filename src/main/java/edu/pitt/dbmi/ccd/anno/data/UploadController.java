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

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import edu.pitt.dbmi.ccd.db.entity.Upload;
import edu.pitt.dbmi.ccd.db.entity.UserAccount;
import edu.pitt.dbmi.ccd.db.error.NotFoundException;
import edu.pitt.dbmi.ccd.db.service.UploadService;

/**
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@RestController
@ExposesResourceFor(UploadResource.class)
@RequestMapping(value=UploadLinks.INDEX)
public class UploadController {
    
    // loggers
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    // servlet
    private final HttpServletRequest request;

    // services and components
    private final UploadLinks uploadLinks;
    private final UploadService uploadService;
    private final UploadResourceAssembler assembler;
    private final UploadPagedResourcesAssembler pageAssembler;

    @Autowired(required=true)
    public UploadController(
            HttpServletRequest request,
            UploadLinks uploadLinks,
            UploadService uploadService,
            UploadResourceAssembler assembler,
            UploadPagedResourcesAssembler pageAssembler) {
        this.request = request;
        this.uploadLinks = uploadLinks;
        this.uploadService = uploadService;
        this.assembler = assembler;
        this.pageAssembler = pageAssembler;
    }

    /* GET requests */

    /**
     * Get all uploads
     * @param  username uploader (nullable)
     * @param  type     upload type (nullable)
     * @param pageable  page request
     * @return          page of uploads
     */
    @RequestMapping(method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PagedResources<UploadResource> uploads(@RequestParam(value="user", required=false) String username, @RequestParam(value="type", required=false) String type, Pageable pageable) {
        final Page<Upload> page = uploadService.filter(username, type, pageable);
        final PagedResources<UploadResource> pagedResources = pageAssembler.toResource(page, assembler, request);
        pagedResources.add(uploadLinks.search());
        return pagedResources;
    }

    /**
     * Get single upload
     * @param  id upload id
     * @return    upload
     */
    @RequestMapping(value=UploadLinks.DATA, method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UploadResource upload(@PathVariable Long id) throws NotFoundException {
        final Upload upload = uploadService.findById(id).orElseThrow(() -> new NotFoundException("Upload", "id", id));
        final UploadResource resource = assembler.toResource(upload);
        return resource;
    }

    /**
     * Search for uploads
     * @param  username uploader (nullable)
     * @param  type     upload type (nullable)
     * @param  query    search terms (nullable)
     * @param  not      negated search terms (nullable)
     * @param  pageable page request
     * @return          page of uploads matching parameters
     */
    @RequestMapping(value=UploadLinks.SEARCH, method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PagedResources<UploadResource> search(
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
        final Page<Upload> page = uploadService.search(username, type, matches, nots, pageable);
        final PagedResources<UploadResource> pagedResources = pageAssembler.toResource(page, assembler, request);
        return pagedResources;
    }

    /* POST requests */

    /**
     * Create new upload (only allows URLs)
     * @param principal requester
     * @param form      upload content
     * @return          upload
     */
    @RequestMapping(method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UploadResource create(@AuthenticationPrincipal UserAccount principal, @RequestBody @Valid UploadForm form) {
        Upload upload = new Upload(principal, form.getTitle(), form.getAddress());
        upload = uploadService.save(upload);
        final UploadResource resource = assembler.toResource(upload);
        return resource;
    }

//    @RequestMapping(method=RequestMethod.DELETE)
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @ResponseBody
//    public void delete(@AuthenticationPrincipal UserAccount principal, @PathVariable Long id) {
//        Upload upload = uploadService.findById(id).orElseThrow(() -> new NotFoundException("Upload", "id", id));
//
//    }

    // I don't think this is necessary
//    /* PUT requests */
//
//    /**
//     * Alias for newUpload with HTTP method PUT
//     */
//    @RequestMapping(method=RequestMethod.PUT)
//    @ResponseStatus(HttpStatus.CREATED)
//    @ResponseBody
//    public UploadResource newUploadPUT(@AuthenticationPrincipal UserAccount principal, @RequestBody @Valid UploadForm form) {
//        return newUpload(principal, form);
//    }
}