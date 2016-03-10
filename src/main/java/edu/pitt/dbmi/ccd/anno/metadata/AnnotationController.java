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

import static edu.pitt.dbmi.ccd.db.util.StringUtils.isNullOrEmpty;

import java.util.Optional;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;
import java.util.stream.IntStream;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
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
import org.springframework.hateoas.Link;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import edu.pitt.dbmi.ccd.db.entity.Annotation;
import edu.pitt.dbmi.ccd.db.entity.AnnotationData;
import edu.pitt.dbmi.ccd.db.entity.UserAccount;
import edu.pitt.dbmi.ccd.db.service.AnnotationService;
import edu.pitt.dbmi.ccd.db.service.AnnotationDataService;

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
    private final AnnotationDataService annotationDataService;
    private final AnnotationResourceAssembler assembler;
    private final AnnotationPagedResourcesAssembler pageAssembler;    

    @Autowired(required=true)
    public AnnotationController(
            HttpServletRequest request,
            AnnotationLinks annotationLinks,
            AnnotationService annotationService,
            AnnotationDataService annotationDataService,
            AnnotationResourceAssembler assembler,
            AnnotationPagedResourcesAssembler pageAssembler) {
        this.request = request;
        this.annotationLinks = annotationLinks;
        this.annotationService = annotationService;
        this.annotationDataService = annotationDataService;
        this.assembler = assembler;
        this.pageAssembler = pageAssembler;
    }

    /* GET requests */

    /**
     * Get all annotations
     * @param  principal    authenticated user
     * @param  user         username (optional)
     * @param  group        group name (nullable)
     * @param  upload       upload id (nullable)
     * @param  vocab        vocabulary name (nnullable)
     * @param  level        attribute level (nullable)
     * @param  name         attribute name (nullable)
     * @param  requirement  attribute requirement level (nullable)
     * @param  pageable     page request
     * @return              page of annotations
     */
    @RequestMapping(method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PagedResources<AnnotationResource> annotations(
                @AuthenticationPrincipal UserAccount principal,
                @RequestParam(value="user", required=false) String user,
                @RequestParam(value="group", required=false) String group,
                @RequestParam(value="upload", required=false) Long upload,
                @RequestParam(value="vocab", required=false) String vocab,
                @RequestParam(value="level", required=false) String attributeLevel,
                @RequestParam(value="name", required=false) String attributeName,
                @RequestParam(value="requirement", required=false) String attributeRequirementLevel,
                @RequestParam(value="showRedacted", required=false, defaultValue="false") Boolean showRedacted,
                Pageable pageable) {
            final Page<Annotation> page = annotationService.filter(principal, user, group, upload, vocab, attributeLevel, attributeName, attributeRequirementLevel, showRedacted, pageable);
            final PagedResources<AnnotationResource> pagedResources = pageAssembler.toResource(page, assembler, request);
            pagedResources.add(annotationLinks.search());
            return pagedResources;
    }

    /**
     * Get annotation by id
     * @param  principal authenticated user
     * @param  id        annotation id
     * @return           annotation
     */
    @RequestMapping(value=AnnotationLinks.ANNOTATION, method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AnnotationResource annotation(@AuthenticationPrincipal UserAccount principal, @PathVariable Long id) {
        final Annotation annotation = annotationService.findById(principal, id);
        final AnnotationResource resource = assembler.toResource(annotation);
        return resource;
    }

    /**
     * Get child annotations by parent
     * @param  principal authenticated user
     * @param  id        parent annotation id
     * @return           page of annotations
     */
    @RequestMapping(value=AnnotationLinks.CHILDREN, method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PagedResources<AnnotationResource> children(@AuthenticationPrincipal UserAccount principal, @PathVariable Long id, Pageable pageable) {
        final Page<Annotation> page = annotationService.findByParent(principal, id, pageable);
        final PagedResources<AnnotationResource> pagedResources = pageAssembler.toResource(page, assembler, request);
        return pagedResources;
    }

    /**
     * Search for annotations
     * @param  principal    authenticated user (required)
     * @param  user         username (nullable)
     * @param  group        group name (nullable)
     * @param  upload       upload id (nullable)
     * @param  vocab        vocabulary name (nnullable)
     * @param  level        attribute level (nullable)
     * @param  name         attribute name (nullable)
     * @param  requirement  attribute requirement level (nullable)
     * @param  query        search terms (nullable)
     * @param  not          negated search terms (nullable)
     * @param  pageable     page request
     * @return              page of annotations matching parameters
     */
    @RequestMapping(value=AnnotationLinks.SEARCH, method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PagedResources<AnnotationResource> search(
            @AuthenticationPrincipal UserAccount principal,
            @RequestParam(value="user", required=false) String user,
            @RequestParam(value="group", required=false) String group,
            @RequestParam(value="upload", required=false) Long upload,
            @RequestParam(value="vocab", required=false) String vocab,
            @RequestParam(value="level", required=false) String attributeLevel,
            @RequestParam(value="name", required=false) String attributeName,
            @RequestParam(value="requirement", required=false) String attributeRequirementLevel,
            @RequestParam(value="showRedacted", required=false, defaultValue="false") Boolean showRedacted,
            @RequestParam(value="query", required=false) String query,
            @RequestParam(value="not", required=false) String not,
            Pageable pageable) {
        final Set<String> matches = (query != null) ? new HashSet<>(Arrays.asList(query.trim().split("\\s+")))
                                                    : null;
        final Set<String> nots = (not != null) ? new HashSet<>(Arrays.asList(not.trim().split("\\s+")))
                                               : null;
        final Page<Annotation> page = annotationService.search(principal, user, group, upload, vocab, attributeLevel, attributeName, attributeRequirementLevel, showRedacted, matches, nots, pageable);
        final PagedResources<AnnotationResource> pagedResources = pageAssembler.toResource(page, assembler, request);
        return pagedResources;
    }

    /* POST requests */

    @RequestMapping(method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public AnnotationResource newAnnotation(@AuthenticationPrincipal UserAccount principal, @RequestBody @Valid AnnotationForm form) {
        Annotation annotation = annotationService.create(principal, form.getTarget(), form.getParent(), form.getAccess(), form.getGroup(), form.getVocabulary());
        annotation = newAnnotationData(annotation, form.getData());
        annotation = annotationService.saveAndFlush(annotation);
        final AnnotationResource resource = assembler.toResource(annotation);
        return resource;
    }

    private Annotation newAnnotationData(Annotation annotation, @Valid List<AnnotationDataForm> data) {        
        IntStream.range(0, data.size())
                 .forEach(i -> {
                   final long attribute = data.get(i).getAttribute();
                   final String value = data.get(i).getValue();
                   final List<AnnotationDataForm> children = data.get(i).getChildren();
                   final AnnotationData annoData = annotationDataService.create(annotation, attribute, value);
                   if (children.size() > 0) {
                      newAnnotationDataChildren(annotation, annoData, children);
                   }
                   annotation.addData(annoData);
                  });
        return annotation;
    }

    private void newAnnotationDataChildren(Annotation annotation, AnnotationData parent, @Valid List<AnnotationDataForm> children) {
        IntStream.range(0, children.size())
                 .forEach(i -> {
                   final long attribute = children.get(i).getAttribute();
                   final String value = children.get(i).getValue();
                   final List<AnnotationDataForm> moreChildren = children.get(i).getChildren();
                   final AnnotationData annoData = annotationDataService.create(annotation, parent, attribute, value);
                   if (children.size() > 0) {
                       newAnnotationDataChildren(annotation, annoData, moreChildren);
                   }
                 });
    }
}