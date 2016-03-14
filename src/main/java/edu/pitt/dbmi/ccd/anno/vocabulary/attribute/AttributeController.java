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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ResponseBody;
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
 * Controller for Attribute endpoints
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
    private final AttributeLinks attributeLinks;
    private final AttributeService attributeService;
    private final VocabularyService vocabularyService;
    private final AttributeResourceAssembler assembler;
    private final AttributePagedResourcesAssembler pageAssembler;

    @Autowired(required=true)
    public AttributeController(
            HttpServletRequest request,
            AttributeLinks attributeLinks,
            AttributeService attributeService,
            VocabularyService vocabularyService,
            AttributeResourceAssembler assembler,
            AttributePagedResourcesAssembler pageAssembler) {
        this.request = request;
        this.attributeLinks = attributeLinks;
        this.attributeService = attributeService;
        this.vocabularyService = vocabularyService;
        this.assembler = assembler;
        this.pageAssembler = pageAssembler;
    }

    /* GET requests */

    /**
     * Get attributes across all vocabularies
     * @param  level        attribute level (can be null)
     * @param  requirement  attribute requirement level (can be null)
     * @param  name         attribute name (can be null)
     * @param  pageable     page request
     * @return              page of all attributes meeting criteria
     */
    @RequestMapping(method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PagedResources<AttributeResource> index(
            @RequestParam(value="level", required=false) String level,
            @RequestParam(value="name", required=false) String name,
            @RequestParam(value="requirement", required=false) String requirementLevel,
            @PageableDefault(size=20, sort={"vocab", "id"}) Pageable pageable) {

            final Page<Attribute> page = attributeService.findByVocabAndLevelAndNameAndRequirementLevel(null, level, name, requirementLevel, pageable);
            final PagedResources<AttributeResource> pagedResources = pageAssembler.toResource(page, assembler, request);
            pagedResources.add(attributeLinks.search());
            return pagedResources;
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
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PagedResources<AttributeResource> attributes(
            @PathVariable String vocabName,
            @RequestParam(value="level", required=false) String level,
            @RequestParam(value="requirement", required=false) String requirementLevel,
            @RequestParam(value="name", required=false) String name,
            @PageableDefault(size=20, sort={"id"}) Pageable pageable) {

        final Page<Attribute> page = attributeService.findByVocabAndLevelAndNameAndRequirementLevel(vocabName, level, name, requirementLevel, pageable);
        final PagedResources<AttributeResource> pagedResources = pageAssembler.toResource(page, assembler, request);
        return pagedResources;
    }

    /**
     * Get an attribute for a specified vocabulary by id
     * @param  vocabName  name of vocabulary
     * @param  id          id of attribute
     * @return             a single attribute
     */
    @RequestMapping(value=AttributeLinks.ATTRIBUTE, method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AttributeResource attribute(@PathVariable String vocabName, @PathVariable Long id) {
        vocabularyService.findByName(vocabName);
        final Attribute attribute = attributeService.findOne(id);
        final AttributeResource resource = assembler.toResource(attribute);
        return resource;
    }

    @RequestMapping(value=AttributeLinks.CHILDREN, method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PagedResources<AttributeResource> children(@PathVariable String vocabName, @PathVariable Long id, Pageable pageable) {
        final Page<Attribute> page = attributeService.findByVocabAndParent(vocabName, id, pageable);
        final PagedResources<AttributeResource> pagedResources = pageAssembler.toResource(page, assembler, request);
        return pagedResources;
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

        final Page<Attribute> page = attributeService.findByVocabAndLevelContainsAndNameContainsAndRequirementLevelContains(vocabName, level, name, requirementLevel, pageable);
        final PagedResources<AttributeResource> pagedResources = pageAssembler.toResource(page, assembler, request);
        return new ResponseEntity<>(pagedResources, HttpStatus.OK);
    }
}
