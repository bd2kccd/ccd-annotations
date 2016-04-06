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

import static edu.pitt.dbmi.ccd.db.util.StringUtils.isNullOrEmpty;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import edu.pitt.dbmi.ccd.anno.error.AttributeNotFoundException;
import edu.pitt.dbmi.ccd.anno.error.VocabularyNotFoundException;
import edu.pitt.dbmi.ccd.anno.vocabulary.attribute.AttributePagedResourcesAssembler;
import edu.pitt.dbmi.ccd.anno.vocabulary.attribute.AttributeResource;
import edu.pitt.dbmi.ccd.anno.vocabulary.attribute.AttributeResourceAssembler;
import edu.pitt.dbmi.ccd.db.entity.Attribute;
import edu.pitt.dbmi.ccd.db.entity.Vocabulary;
import edu.pitt.dbmi.ccd.db.error.NotFoundException;
import edu.pitt.dbmi.ccd.db.service.AttributeService;
import edu.pitt.dbmi.ccd.db.service.VocabularyService;

// logging

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
    private final AttributeService attributeService;
    private final VocabularyResourceAssembler assembler;
    private final VocabularyPagedResourcesAssembler pageAssembler;
    private final AttributeResourceAssembler attributeAssembler;
    private final AttributePagedResourcesAssembler attributePageAssembler;

    @Autowired(required=true)
    public VocabularyController(
            HttpServletRequest request,
            VocabularyLinks vocabularyLinks,
            VocabularyService vocabularyService,
            AttributeService attributeService,
            VocabularyResourceAssembler assembler,
            VocabularyPagedResourcesAssembler pageAssembler,
            AttributeResourceAssembler attributeAssembler,
            AttributePagedResourcesAssembler attributePageAssembler) {
        this.request = request;
        this.vocabularyLinks = vocabularyLinks;
        this.vocabularyService = vocabularyService;
        this.attributeService = attributeService;
        this.assembler = assembler;
        this.pageAssembler = pageAssembler;
        this.attributeAssembler = attributeAssembler;
        this.attributePageAssembler = attributePageAssembler;
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
     * @param vocabName vocabulary name
     * @return          single vocabulary
     */
    @RequestMapping(value=VocabularyLinks.VOCABULARY, method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public VocabularyResource vocabulary(@PathVariable String vocabName) throws NotFoundException {
        final Vocabulary vocab = vocabularyService.findByName(vocabName).orElseThrow(() -> new VocabularyNotFoundException(vocabName));
        final VocabularyResource resource = assembler.toResource(vocab);
        return resource;
    }

    /**
     * Get vocabulary attributes
     * @param  vocabName  vocabulary name
     * @return            page of attributes
     */
    @RequestMapping(value=VocabularyLinks.ATTRIBUTES, method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PagedResources<AttributeResource> attributes(
            @PathVariable String vocabName,
            @RequestParam(value="level", required=false) String level,
            @RequestParam(value="name", required=false) String name,
            @RequestParam(value="requirement", required=false) String requirementLevel,
            @PageableDefault(size=20, sort={"id"}) Pageable pageable)
            throws NotFoundException {
        final Vocabulary vocab = vocabularyService.findByName(vocabName).orElseThrow(() -> new VocabularyNotFoundException(vocabName));
        final Page<Attribute> page;
        if (isNullOrEmpty(level) && isNullOrEmpty(name) && isNullOrEmpty(requirementLevel)) {
            page = attributeService.findByVocabAndLevelAndNameAndRequirementLevelAndParentIsNull(vocab, level, name, requirementLevel, pageable);
        } else {
            page = attributeService.findByVocabAndLevelAndNameAndRequirementLevel(vocab, level, name, requirementLevel, pageable);
        }
        final PagedResources<AttributeResource> pagedResources = attributePageAssembler.toResource(page, attributeAssembler, request);
        return pagedResources;
    }

    /**
     * Get vocabulary attribute
     * @param  vocabName  vocabulary name
     * @param  id         attribute id
     * @return            page of attributes
     */
    @RequestMapping(value=VocabularyLinks.ATTRIBUTE, method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AttributeResource attribute(@PathVariable String vocabName, @PathVariable Long id) throws NotFoundException {
        final Vocabulary vocab = vocabularyService.findByName(vocabName).orElseThrow(() -> new VocabularyNotFoundException(vocabName));
        final Attribute attribute = vocab.getAttributes()
                                         .stream()
                                         .filter(a -> a.getId().equals(id))
                                         .findFirst()
                                         .orElseThrow(() -> new AttributeNotFoundException(vocab.getName(), id));
        final AttributeResource resource = attributeAssembler.toResource(attribute);
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
        final Set<String> matches = (query != null)
                                  ? new HashSet<>(Arrays.asList(query.trim().split("\\s+")))
                                  : null;
        final Set<String> nots = (not != null)
                               ? new HashSet<>(Arrays.asList(not.trim().split("\\s+")))
                               : null;
        final Page<Vocabulary> page = vocabularyService.search(matches, nots, pageable);
        final PagedResources<VocabularyResource> pagedResources = pageAssembler.toResource(page, assembler, request);
        return pagedResources;
    }
}
