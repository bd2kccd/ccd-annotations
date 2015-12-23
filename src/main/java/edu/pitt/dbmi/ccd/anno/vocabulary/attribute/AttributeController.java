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

package edu.pitt.dbmi.ccd.anno.vocabulary.attribute;

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
import edu.pitt.dbmi.ccd.db.entity.Attribute;
import edu.pitt.dbmi.ccd.db.entity.Vocabulary;
import edu.pitt.dbmi.ccd.db.service.AttributeService;
import edu.pitt.dbmi.ccd.db.service.VocabularyService;
import edu.pitt.dbmi.ccd.anno.resources.EmptyResource;

// logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for Attriubte endpoints
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@RestController
@ExposesResourceFor(AttributeResource.class)
@RequestMapping(value=AttributeLinks.INDEX)
public class AttributeController {
    
    // loggers
    private static final Logger LOGGER = LoggerFactory.getLogger(AttributeController.class);

    // servlet
    private final HttpServletRequest request;

    // services and components
    private final AttributeService attributeService;
    private final VocabularyService vocabService;
    private final AttributeResourceAssembler assembler;
    private final AttributePagedResourcesAssembler pageAssembler;
    private final AttributeLinks attributeLinks;

    @Autowired(required=true)
    public AttributeController(HttpServletRequest request,
                                AttributeService attributeService,
                                VocabularyService vocabService,
                                AttributeResourceAssembler assembler,
                                AttributePagedResourcesAssembler pageAssembler,
                                AttributeLinks attributeLinks) {
        this.request = request;
        this.attributeService = attributeService;
        this.vocabService = vocabService;
        this.assembler = assembler;
        this.pageAssembler = pageAssembler;
        this.attributeLinks = attributeLinks;
    }

    /* GET requests */

    /**
     * Get all vocabularies
     * @param  pageable page request
     * @return          a page of vocabulary
     */
    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<PagedResources<AttributeResource>> attributes(@PathVariable String vocabulary, Pageable pageable) {
        Optional<Vocabulary> vocab = vocabService.findByName(vocabulary);
        if (!vocab.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            Page<Attribute> page = attributeService.findByVocabAndParentIsNull(vocab.get(), pageable);
            PagedResources<AttributeResource> pagedResources = pageAssembler.toResource(page, assembler, request);
            pagedResources.add(attributeLinks.search());
            return new ResponseEntity<>(pagedResources, HttpStatus.OK);
        } catch (PropertyReferenceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
