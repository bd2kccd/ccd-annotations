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

package edu.pitt.dbmi.ccd.anno.metadata;

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
import edu.pitt.dbmi.ccd.db.entity.Annotation;
import edu.pitt.dbmi.ccd.db.entity.AnnotationData;
import edu.pitt.dbmi.ccd.db.service.AnnotationService;

// logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@RestController
@ExposesResourceFor(AnnotationResource.class)
@RequestMapping(AnnotationLinks.INDEX)
public class AnnotationController {
    
    // loggers
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationController.class);

    // servlet
    private final HttpServletRequest request;

    // services and components
    private final AnnotationLinks annotationLinks;
    private final AnnotationService annotationService;
    private final AnnotationResourceAssembler assembler;
    private final AnnotationPagedResourcesAssembler pageAssembler;

    @Autowired(required=true)
    public AnnotationController(
            HttpServletRequest request,
            AnnotationLinks annotationLinks,
            AnnotationService annotationService,
            AnnotationResourceAssembler assembler,
            AnnotationPagedResourcesAssembler pageAssembler) {
        this.request = request;
        this.annotationLinks = annotationLinks;
        this.annotationService = annotationService;
        this.assembler = assembler;
        this.pageAssembler = pageAssembler;
    }

    /* GET requests */

}