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
import edu.pitt.dbmi.ccd.anno.resources.EmptyResource;

// logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@RestController
@ExposesResourceFor(VocabularyResource.class)
@RequestMapping(value="vs")
public class VocabularyController {
    
    // loggers
    private static final Logger LOGGER = LoggerFactory.getLogger(VocabularyController.class);

    // servlet
    private final HttpServletRequest request;

    // services and components
    private final VocabularyService vocabService;
    private final AttributeService attribService;

    @Autowired(required=true)
    public VocabularyController(HttpServletRequest request, VocabularyService vocabService, AttributeService attribService) {
        this.request = request;
        this.vocabService = vocabService;
        this.attribService = attribService;
    }

    // Get vocabulary by name
    @RequestMapping(value="/{name}", method=RequestMethod.GET)
    public ResponseEntity<VocabularyResource> getVocabulary(@PathVariable String name) {
        final Optional<Vocabulary> vocab = vocabService.findByName(name);
        final VocabularyResource resource;
        if (vocab.isPresent()) {
            resource = new VocabularyResource(vocab.get());
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // // Get vocabulary's attributes
    // @RequestMapping(value="/{name}/attributes", method=RequestMethod.GET)
    // public ResponseEntity<Collection<AttributeResource>> getAttributes(@PathVariable String name) {
    //     final Vocabulary vocab = vocabService.findByName(name);
    //     final Collection<Attribute> attributes = attribService.findAllParentless(vocab);
    //     final Collection<AttributeResource> resources = attributes.stream()
    //                                                               .map(a -> new AttributeResource(a))
    //                                                               .collect(Collectors.toList());
    //     return new ResponseEntity<Collection<AttributeResource>>(resources, HttpStatus.OK);
    // }

    // // Get attribute by id
    // @RequestMapping(value="/{name}/attributes/{id}", method=RequestMethod.GET)
    // public ResponseEntity<AttributeResource> getAttribute(@PathVariable String name, @PathVariable Long id) {
    //     final Vocabulary vocab = vocabService.findByName(name);
    //     final Attribute attrib = attribService.findByInnerId(vocab, id);
    //     return new ResponseEntity<AttributeResource>(new AttributeResource(attrib), HttpStatus.OK);
    // }
}