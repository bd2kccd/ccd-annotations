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

package edu.pitt.dbmi.ccd.anno.upload;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import edu.pitt.dbmi.ccd.db.entity.Upload;
import edu.pitt.dbmi.ccd.db.service.UploadService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    // services andn components
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
     * @param pageable page request
     * @return         page of uploads
     */
    @RequestMapping(method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PagedResources<UploadResource> uploads(Pageable pageable) {
        final Page<Upload> page = uploadService.findAll(pageable);
        final PagedResources<UploadResource> pagedResources = pageAssembler.toResource(page, assembler, request);
        pagedResources.add(uploadLinks.search());
        return pagedResources;
    }
}