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
import org.springframework.data.web.PageableDefault;
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
     * Get attributes across all vocabularies
     * @param  level        attribute level
     * @param  requirement  attribute requirement level
     * @param  name         attribute name
     * @param  pageable     page request
     * @return              page of all attributes meeting criteria
     */
    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<PagedResources<AttributeResource>> index(@RequestParam(value="level", required=false) String level, @RequestParam(value="requirement", required=false) String requirementLevel, @RequestParam(value="name", required=false) String name, @PageableDefault(size=20, sort={"vocab", "id"}) Pageable pageable) {
        try {
            final Page<Attribute> page;
            final PagedResources<AttributeResource> pagedResources;
            if (level != null) {
                if (name != null) {
                    // by level and name and requirement level
                    if (requirementLevel != null) {
                        page = attributeService.findByLevelAndRequirementLevelAndName(level, requirementLevel, name, pageable);
                        pagedResources = pageAssembler.toResource(page, assembler, request);
                    } else {
                        // by level and name
                        page = attributeService.findByLevelAndName(level, name, pageable);
                        pagedResources = pageAssembler.toResource(page, assembler, request);   
                    }
                } else if (requirementLevel != null) {
                    // by level and requirement level
                    page = attributeService.findByLevelAndRequirementLevel(level, requirementLevel, pageable);
                    pagedResources = pageAssembler.toResource(page, assembler, request);
                } else {
                    // by level
                    page = attributeService.findByLevel(level, pageable);
                    pagedResources = pageAssembler.toResource(page, assembler, request);
                }
            } else if (requirementLevel != null) {
                // by requirement level and name
                if (name != null) {
                    page = attributeService.findByRequirementLevelAndName(requirementLevel, name, pageable);
                    pagedResources = pageAssembler.toResource(page, assembler, request);
                } else {
                    // by requirement level
                    page = attributeService.findByRequirementLevel(requirementLevel, pageable);
                    pagedResources = pageAssembler.toResource(page, assembler, request);
                }
            } else if (name != null) {
                // by name
                page = attributeService.findByName(name, pageable);
                pagedResources = pageAssembler.toResource(page, assembler, request);

            } else {
                // all attributes
                page = attributeService.findAllByParentIsNull(pageable);
                pagedResources = pageAssembler.toResource(page, assembler, request);
            }

            pagedResources.add(attributeLinks.search());
            return new ResponseEntity<>(pagedResources, HttpStatus.OK);

        } catch (PropertyReferenceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get attributes by specified vocabulary
     * @param  vocabulary  vocabulary name
     * @param  level       attribute level
     * @param  requirement attribute requirement level
     * @param  name        attribute name
     * @param  pageable    page request
     * @return             page of attributes meeting criteria by vocabulary
     */
    @RequestMapping(value=AttributeLinks.ATTRIBUTES, method=RequestMethod.GET)
    public ResponseEntity<PagedResources<AttributeResource>> attributes(@PathVariable String vocabulary, @RequestParam(value="level", required=false) String level, @RequestParam(value="requirement", required=false) String requirementLevel, @RequestParam(value="name", required=false) String name, @PageableDefault(size=20, sort={"id"}) Pageable pageable) {
        Optional<Vocabulary> vocab = vocabService.findByName(vocabulary);
        if (!vocab.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            final Page<Attribute> page;
            final PagedResources<AttributeResource> pagedResources;
            if (level != null) {
                // by level and name
                // doesn't matter whether or not requirement level is null, level and name (with vocab) form a composite key
                if (name != null) {
                    page = attributeService.findByVocabAndLevelAndName(vocab.get(), level, name, pageable);
                    pagedResources = pageAssembler.toResource(page, assembler, request);
                } else if (requirementLevel != null) {
                    // by level and requirement level
                    page = attributeService.findByVocabAndLevelAndRequirementLevel(vocab.get(), level, requirementLevel, pageable);
                    pagedResources = pageAssembler.toResource(page, assembler, request);
                } else {
                    // by level
                    page = attributeService.findByVocabAndLevel(vocab.get(), level, pageable);
                    pagedResources = pageAssembler.toResource(page, assembler, request);
                }
            } else if (requirementLevel != null) {
                // by requirement level and name
                if (name != null) {
                    page = attributeService.findByVocabAndRequirementLevelAndName(vocab.get(), requirementLevel, name, pageable);
                    pagedResources = pageAssembler.toResource(page, assembler, request);
                } else {
                    // by requirement level
                    page = attributeService.findByVocabAndRequirementLevel(vocab.get(), requirementLevel, pageable);
                    pagedResources = pageAssembler.toResource(page, assembler, request);
                }
            } else if (name != null) {
                // by name
                page = attributeService.findByVocabAndName(vocab.get(), name, pageable);
                pagedResources = pageAssembler.toResource(page, assembler, request);

            } else {
                // all attributes
                page = attributeService.findByVocabAndParentIsNull(vocab.get(), pageable);
                pagedResources = pageAssembler.toResource(page, assembler, request);
            }

            pagedResources.add(attributeLinks.vocabularySearch(vocab.get()));
            return new ResponseEntity<>(pagedResources, HttpStatus.OK);
        } catch (PropertyReferenceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get an attribute for a specified vocabulary by id
     * @param  vocabulary  name of vocabulary
     * @param  id          id of attribute
     * @return             a single attribute
     */
    @RequestMapping(value=AttributeLinks.ATTRIBUTE, method=RequestMethod.GET)
    public ResponseEntity<AttributeResource> attribute(@PathVariable String vocabulary, @PathVariable Long id) {
        Optional<Vocabulary> vocab = vocabService.findByName(vocabulary);
        if (vocab.isPresent()) {
            Optional<Attribute> attribute = attributeService.findByVocabAndId(vocab.get(), id);
            if (attribute.isPresent()) {
                final AttributeResource resource = assembler.toResource(attribute.get());
                return new ResponseEntity<>(resource, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Attribute search page
     * @return search links
     */
    @RequestMapping(value=AttributeLinks.SEARCH, method=RequestMethod.GET)
    public ResponseEntity<EmptyResource> search() {
        final Link self = attributeLinks.search().withSelfRel();
        EmptyResource resource = new EmptyResource(self);
        resource.add(
            attributeLinks.levelStartsWith(),
            attributeLinks.levelContains(),
            attributeLinks.nameStartsWith(),
            attributeLinks.nameContains(),
            attributeLinks.requirementLevelStartsWith(),
            attributeLinks.requirementLevelContains()
        );
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    /**
     * Attribute search page by vocabulary
     * @return search links
     */
    @RequestMapping(value=AttributeLinks.VOCABULARY_SEARCH, method=RequestMethod.GET)
    public ResponseEntity<EmptyResource> search(@PathVariable String vocabulary) {
        Optional<Vocabulary> vocab = vocabService.findByName(vocabulary);
        if (!vocab.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        final Link self = attributeLinks.vocabularySearch(vocab.get()).withSelfRel();
        EmptyResource resource = new EmptyResource(self);
        resource.add(
            attributeLinks.vocabularyLevelStartsWith(vocab.get()),
            attributeLinks.vocabularyLevelContains(vocab.get()),
            attributeLinks.vocabularyNameStartsWith(vocab.get()),
            attributeLinks.vocabularyNameContains(vocab.get()),
            attributeLinks.vocabularyRequirementLevelStartsWith(vocab.get()),
            attributeLinks.vocabularyRequirementLevelContains(vocab.get())
        );
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
}
