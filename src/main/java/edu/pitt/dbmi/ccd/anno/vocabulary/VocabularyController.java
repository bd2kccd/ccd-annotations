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

package edu.pitt.dbmi.ccd.anno.vocabulary;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Link;
import edu.pitt.dbmi.ccd.db.entity.Vocabulary;
import edu.pitt.dbmi.ccd.db.service.VocabularyService;

// logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for Vocabulary endpoints
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@RestController
@ExposesResourceFor(VocabularyResource.class)
@RequestMapping(value=VocabularyLinks.INDEX)
public class VocabularyController {
    
    // loggers
    private static final Logger LOGGER = LoggerFactory.getLogger(VocabularyController.class);

    // servlet
    private final HttpServletRequest request;

    // services and components
    private final VocabularyLinks vocabularyLinks;
    private final VocabularyService vocabularyService;
    private final VocabularyResourceAssembler assembler;
    private final VocabularyPagedResourcesAssembler pageAssembler;

    @Autowired(required=true)
    public VocabularyController(
            HttpServletRequest request,
            VocabularyLinks vocabularyLinks,
            VocabularyService vocabularyService,
            VocabularyResourceAssembler assembler,
            VocabularyPagedResourcesAssembler pageAssembler) {
        this.request = request;
        this.vocabularyLinks = vocabularyLinks;
        this.vocabularyService = vocabularyService;
        this.assembler = assembler;
        this.pageAssembler = pageAssembler;
    }

    /* GET requests */

    /**
     * Get all vocabularies
     * @param  pageable page request
     * @return          page of vocabularies
     */
    @RequestMapping(method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PagedResources<VocabularyResource> vocabularies(Pageable pageable) {
        final Page<Vocabulary> page = vocabularyService.findAll(pageable);
        final PagedResources<VocabularyResource> pagedResources = pageAssembler.toResource(page, assembler, request);
        pagedResources.add(vocabularyLinks.search());
        return pagedResources;
    }

    /**
     * Get single vocabulary
     * @param name vocabulary name
     * @return     single vocabulary
     */
    @RequestMapping(value=VocabularyLinks.VOCABULARY, method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public VocabularyResource vocabulary(@PathVariable String name) {
        final Vocabulary vocab = vocabularyService.findByName(name);
        final VocabularyResource resource = assembler.toResource(vocab);
        return resource;
    }

    /**
     * Search vocabularies
     * @param  query    search terms (nullable)
     * @param  not      negated search terms (nullable)
     * @param  pageable page request
     * @return          page of vocabularies matching parameters
     */
    @RequestMapping(value=VocabularyLinks.SEARCH, method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PagedResources<VocabularyResource> search(
            @RequestParam(value="query", required=false) String query,
            @RequestParam(value="not", required=false) String not,
            Pageable pageable) {
        final Set<String> matches = (query != null) ? new HashSet<>(Arrays.asList(query.trim().split("\\s+")))
                                                    : null;
        final Set<String> nots = (not != null) ? new HashSet<>(Arrays.asList(not.trim().split("\\s+")))
                                               : null;
        final Page<Vocabulary> page = vocabularyService.search(matches, nots, pageable);
        final PagedResources<VocabularyResource> pagedResources = pageAssembler.toResource(page, assembler, request);
        return pagedResources;
    }
}
