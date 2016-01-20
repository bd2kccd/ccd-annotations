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
import org.springframework.data.web.PageableDefault;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Link;
import edu.pitt.dbmi.ccd.db.entity.Vocabulary;
import edu.pitt.dbmi.ccd.db.entity.Attribute;
import edu.pitt.dbmi.ccd.db.service.VocabularyService;
import edu.pitt.dbmi.ccd.db.service.AttributeService;
import edu.pitt.dbmi.ccd.anno.vocabulary.attribute.*;
import edu.pitt.dbmi.ccd.anno.resources.EmptyResource;

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
    private final VocabularyService vocabService;
    private final VocabularyResourceAssembler assembler;
    private final VocabularyPagedResourcesAssembler pageAssembler;
    private final VocabularyLinks vocabLinks;
    private final AttributeService attributeService;
    private final AttributeResourceAssembler attributeAssembler;
    private final AttributePagedResourcesAssembler attributePageAssembler;


    @Autowired(required=true)
    public VocabularyController(HttpServletRequest request,
                                VocabularyService vocabService,
                                VocabularyResourceAssembler assembler,
                                VocabularyPagedResourcesAssembler pageAssembler,
                                VocabularyLinks vocabLinks,
                                AttributeService attributeService,
                                AttributeResourceAssembler attributeAssembler,
                                AttributePagedResourcesAssembler attributePageAssembler) {
        this.request = request;
        this.vocabService = vocabService;
        this.assembler = assembler;
        this.pageAssembler = pageAssembler;
        this.vocabLinks = vocabLinks;
        this.attributeService = attributeService;
        this.attributeAssembler = attributeAssembler;
        this.attributePageAssembler = attributePageAssembler;
    }

    /* GET requests */

    /**
     * Get all vocabularies
     * @param  pageable page request
     * @return          a page of vocabulary
     */
    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<PagedResources<VocabularyResource>> vocabularies(Pageable pageable) {
        try {
            Page<Vocabulary> page = vocabService.findAll(pageable);
            PagedResources<VocabularyResource> pagedResources = pageAssembler.toResource(page, assembler, request);
            pagedResources.add(vocabLinks.search());
            return new ResponseEntity<>(pagedResources, HttpStatus.OK);
        } catch (PropertyReferenceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get single vocabulary
     * @param name vocabulary name
     * @return     single vocabulary if found
     *             404 if not
     */
    @RequestMapping(value=VocabularyLinks.VOCABULARY, method=RequestMethod.GET)
    public ResponseEntity<VocabularyResource> vocabulary(@PathVariable String name) {
        final Optional<Vocabulary> vocab = vocabService.findByName(name);
        if (vocab.isPresent()) {
            final VocabularyResource resource = assembler.toResource(vocab.get());
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Vocabulary search page
     * @return search links
     */
    @RequestMapping(value=VocabularyLinks.SEARCH, method=RequestMethod.GET)
    public ResponseEntity<EmptyResource> search() {
        final Link self = vocabLinks.search().withSelfRel();
        EmptyResource resource = new EmptyResource(self);
        resource.add(
            vocabLinks.nameStartsWith(),
            vocabLinks.nameContains(),
            vocabLinks.descriptionContains()
        );
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    /**
     * Search for vocabularies whose name starts with terms
     * @param terms    terms to search for
     * @param pageable page request
     * @return         matching vocabularies
     */
    @RequestMapping(value=VocabularyLinks.NAME_STARTS, method=RequestMethod.GET)
    public ResponseEntity<PagedResources<VocabularyResource>> findByNameStartsWith(@RequestParam("terms") String terms, Pageable pageable) {
        try {
            Page<Vocabulary> page = vocabService.findByNameStartsWith(terms, pageable);
            PagedResources<VocabularyResource> pagedResources = pageAssembler.toResource(page, assembler, request);
            return new ResponseEntity<>(pagedResources, HttpStatus.OK);
        } catch (PropertyReferenceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Search for vocabularies whose name contains terms
     * @param terms    terms to search for
     * @param pageable page request
     * @return         matching vocabularies
     */
    @RequestMapping(value=VocabularyLinks.NAME_CONTAINS, method=RequestMethod.GET)
    public ResponseEntity<PagedResources<VocabularyResource>> findByNameContains(@RequestParam("terms") String terms, Pageable pageable) {
        try {
            Page<Vocabulary> page = vocabService.findByNameContains(terms, pageable);
            PagedResources<VocabularyResource> pagedResources = pageAssembler.toResource(page, assembler, request);
            return new ResponseEntity<>(pagedResources, HttpStatus.OK);
        } catch (PropertyReferenceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Search for vocabularies whose descriptino contains terms
     * @param terms    terms to search for
     * @param pageable page request
     * @return         matching vocabularies
     */
    @RequestMapping(value=VocabularyLinks.DESCRIPTION_CONTAINS, method=RequestMethod.GET)
    public ResponseEntity<PagedResources<VocabularyResource>> findByDescriptionContains(@RequestParam("terms") String terms, Pageable pageable) {
        try {
            Page<Vocabulary> page = vocabService.findByDescriptionContains(terms, pageable);
            PagedResources<VocabularyResource> pagedResources = pageAssembler.toResource(page, assembler, request);
            return new ResponseEntity<>(pagedResources, HttpStatus.OK);
        } catch (PropertyReferenceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Attributes test
    // @RequestMapping(value="{vocabName}/attributes", method=RequestMethod.GET)
    // public ResponseEntity<PagedResources<AttributeResource>> attributes(@PathVariable("vocabName") String vocabName, @PageableDefault(size=20, sort={"id"}) Pageable pageable) {
    //     Optional<Vocabulary> vocab = vocabService.findByName(vocabName);
    //     if (!vocab.isPresent()) {
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }
    //     try {
    //         Page<Attribute> page = attributeService.findByVocabAndParentIsNull(vocab.get(), pageable);
    //         PagedResources<AttributeResource> pagedResources = attributePageAssembler.toResource(page, attributeAssembler, request);
    //         // pagedResources.add(attributeLinks.search());
    //         return new ResponseEntity<>(pagedResources, HttpStatus.OK);
    //     } catch (PropertyReferenceException e) {
    //         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    //     }
    // }
}
