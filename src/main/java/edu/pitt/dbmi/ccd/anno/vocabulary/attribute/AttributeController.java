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
    private final VocabularyService vocabularyService;
    private final AttributeResourceAssembler assembler;
    private final AttributePagedResourcesAssembler pageAssembler;
    private final AttributeLinks attributeLinks;

    @Autowired(required=true)
    public AttributeController(
            HttpServletRequest request,
            AttributeService attributeService,
            VocabularyService vocabularyService,
            AttributeResourceAssembler assembler,
            AttributePagedResourcesAssembler pageAssembler,
            AttributeLinks attributeLinks) {

        this.request = request;
        this.attributeService = attributeService;
        this.vocabularyService = vocabularyService;
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
    public ResponseEntity<PagedResources<AttributeResource>> index(
            @RequestParam(value="level", required=false) String level,
            @RequestParam(value="name", required=false) String name,
            @RequestParam(value="requirement", required=false) String requirementLevel,
            @PageableDefault(size=20, sort={"vocab", "id"}) Pageable pageable) {

        try {
            final Page<Attribute> page;
            final PagedResources<AttributeResource> pagedResources;
            if (level != null) {
                if (name != null) {
                    // by level and name and requirement level
                    if (requirementLevel != null) {
                        page = attributeService.findByLevelAndNameAndRequirementLevel(level, name, requirementLevel, pageable);
                    } else {
                        // by level and name
                        page = attributeService.findByLevelAndName(level, name, pageable);
                    }
                } else if (requirementLevel != null) {
                    // by level and requirement level
                    page = attributeService.findByLevelAndRequirementLevel(level, requirementLevel, pageable);
                } else {
                    // by level
                    page = attributeService.findByLevel(level, pageable);
                }
            } else if (requirementLevel != null) {
                // by requirement level and name
                if (name != null) {
                    page = attributeService.findByNameAndRequirementLevel(name, requirementLevel, pageable);
                } else {
                    // by requirement level
                    page = attributeService.findByRequirementLevel(requirementLevel, pageable);
                }
            } else if (name != null) {
                // by name
                page = attributeService.findByName(name, pageable);

            } else {
                // all attributes
                page = attributeService.findAll(pageable);
            }

            pagedResources = pageAssembler.toResource(page, assembler, request);
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
    public ResponseEntity<PagedResources<AttributeResource>> attributes(
            @PathVariable String vocabName,
            @RequestParam(value="level", required=false) String level,
            @RequestParam(value="requirement", required=false) String requirementLevel,
            @RequestParam(value="name", required=false) String name,
            @PageableDefault(size=20, sort={"id"}) Pageable pageable) {

        Optional<Vocabulary> vocab = vocabularyService.findByName(vocabName);
        // 404 if vocabulary isn't found
        if (!vocab.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            final Page<Attribute> page;
            final PagedResources<AttributeResource> pagedResources;
            if (level != null) {
                // by level and name
                if (name != null) {
                    // doesn't matter whether or not requirement level is null, level and name (with vocab) form a composite key
                    page = attributeService.findByVocabAndLevelAndName(vocab.get(), level, name, pageable);
                } else if (requirementLevel != null) {
                    // by level and requirement level
                    page = attributeService.findByVocabAndLevelAndRequirementLevel(vocab.get(), level, requirementLevel, pageable);
                } else {
                    // by level
                    page = attributeService.findByVocabAndLevel(vocab.get(), level, pageable);
                }
            } else if (requirementLevel != null) {
                // by requirement level and name
                if (name != null) {
                    page = attributeService.findByVocabAndNameAndRequirementLevel(vocab.get(), name, requirementLevel, pageable);
                } else {
                    // by requirement level
                    page = attributeService.findByVocabAndRequirementLevel(vocab.get(), requirementLevel, pageable);
                }
            } else if (name != null) {
                // by name
                page = attributeService.findByVocabAndName(vocab.get(), name, pageable);

            } else {
                // all attributes
                page = attributeService.findByVocab(vocab.get(), pageable);
            }

            pagedResources = pageAssembler.toResource(page, assembler, request);
            return new ResponseEntity<>(pagedResources, HttpStatus.OK);

        } catch (PropertyReferenceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get an attribute for a specified vocabulary by id
     * @param  vocabName  name of vocabulary
     * @param  id          id of attribute
     * @return             a single attribute
     */
    @RequestMapping(value=AttributeLinks.ATTRIBUTE, method=RequestMethod.GET)
    public ResponseEntity<AttributeResource> attribute(@PathVariable String vocabName, @PathVariable Long id) {
        final Optional<Vocabulary> vocab = vocabularyService.findByName(vocabName);
        if (vocab.isPresent()) {
            final Optional<Attribute> attribute = attributeService.findByVocabAndId(vocab.get(), id);
            if (attribute.isPresent()) {
                final AttributeResource resource = assembler.toResource(attribute.get());
                return new ResponseEntity<>(resource, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value=AttributeLinks.CHILDREN, method=RequestMethod.GET)
    public ResponseEntity<PagedResources<AttributeResource>> children(@PathVariable String vocabName, @PathVariable Long id, Pageable pageable) {
        try {
            final Optional<Vocabulary> vocab = vocabularyService.findByName(vocabName);
            if (vocab.isPresent()) {
                final Optional<Attribute> attribute = attributeService.findByVocabAndId(vocab.get(), id);
                if (attribute.isPresent()) {
                    final Page<Attribute> page = attributeService.findByVocabAndParent(vocab.get(), attribute.get(), pageable);
                    final PagedResources<AttributeResource> pagedResources = pageAssembler.toResource(page, assembler, request);
                    return new ResponseEntity<>(pagedResources, HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (PropertyReferenceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Attribute search page
     * @return search results
     */
    @RequestMapping(value=AttributeLinks.SEARCH, method=RequestMethod.GET)
    public ResponseEntity<PagedResources<AttributeResource>> search(
            @RequestParam(value="vocab", required=false) String vocabName,
            @RequestParam(value="levelContains", required=false) String level,
            @RequestParam(value="nameContains", required=false) String name,
            @RequestParam(value="requirementContains", required=false) String requirementLevel,
            @PageableDefault(size=20, sort={"vocab", "id"}) Pageable pageable) {
        final Page<Attribute> page;
        final PagedResources<AttributeResource> pagedResources;

        // change null parameters to empty strings
        // variables can be plugged into queries regardles of value
        level = (level == null) ? "" : level;
        name = (name == null) ? "" : name;
        requirementLevel = (requirementLevel == null) ? "" : requirementLevel;

        try {
            // search by vocab
            if (vocabName != null) {
                Optional<Vocabulary> vocab = vocabularyService.findByName(vocabName);
                if (vocab.isPresent()) {
                    page = attributeService.findByVocabAndLevelContainsAndNameContainsAndRequirementLevelContains(vocab.get(), level, name, requirementLevel, pageable);
                } else {
                    return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } else {
                // search without vocab
                page = attributeService.findByLevelContainsAndNameContainsAndRequirementLevelContains(level, name, requirementLevel, pageable);
            }

            pagedResources = pageAssembler.toResource(page, assembler, request);
            return new ResponseEntity<>(pagedResources, HttpStatus.OK);
        } catch (PropertyReferenceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
