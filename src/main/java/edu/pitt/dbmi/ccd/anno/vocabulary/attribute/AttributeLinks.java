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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import edu.pitt.dbmi.ccd.db.entity.Attribute;
import edu.pitt.dbmi.ccd.db.entity.Vocabulary;
import edu.pitt.dbmi.ccd.anno.links.ResourceLinks;

/**
 * Attribute links
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Component
public class AttributeLinks implements ResourceLinks {

    // attribute links
    public static final String INDEX = "/attributes";
    public static final String ATTRIBUTES = "/{vocabulary}";
    public static final String ATTRIBUTE = "/{vocabulary}/{id}";
    public static final String LEVEL_STARTS = "/search/levelStartsWith";
    public static final String LEVEL_CONTAINS = "/search/levelContains";
    public static final String NAME_STARTS = "/search/nameStartsWith";
    public static final String NAME_CONTAINS = "/search/nameContains";
    public static final String REQUIREMENT_LEVEL_STARTS = "/search/requirementStartsWith";
    public static final String REQUIREMENT_LEVEL_CONTAINS = "/search/requirementContains";

    public static final String VOCABULARY_SEARCH = "/{vocabulary}/search";
    public static final String VOCABULARY_LEVEL_STARTS = "{vocabulary}/search/levelStartsWith";
    public static final String VOCABULARY_LEVEL_CONTAINS = "{vocabulary}/search/levelContains";
    public static final String VOCABULARY_NAME_STARTS = "{vocabulary}/search/nameStartsWith";
    public static final String VOCABULARY_NAME_CONTAINS = "{vocabulary}/search/nameContains";
    public static final String VOCABULARY_REQUIREMENT_LEVEL_STARTS = "{vocabulary}/search/requirementStartsWith";
    public static final String VOCABULARY_REQUIREMENT_LEVEL_CONTAINS = "{vocabulary}/search/requirementContains";

    // attribute rels
    public static final String REL_ATTRIBUTES = "attributes";
    public static final String REL_ATTRIBUTE = "attribute";
    public static final String REL_PARENT = "parent";
    public static final String REL_LEVEL_STARTS = "levelStartsWith";
    public static final String REL_LEVEL_CONTAINS = "levelContains";
    public static final String REL_NAME_STARTS = "nameStartsWith";
    public static final String REL_NAME_CONTAINS = "nameContains";
    public static final String REL_REQUIREMENT_LEVEL_STARTS = "requirementLevelStartsWith";
    public static final String REL_REQUIREMENT_LEVEL_CONTAINS = "  requirementLevelContains";

    // query parameters
    public static final String TERMS = "terms";
    public static final String LEVEL = "level";
    public static final String NAME = "name";
    public static final String REQUIREMENT_LEVEL = "requirement"; 

    // dependencies
    private final EntityLinks entityLinks;

    @Autowired(required=true)
    public AttributeLinks(EntityLinks entityLinks) {
        this.entityLinks = entityLinks;
    }

    /**
     * Get link to attribute resource collection
     * @return link to collection
     */
    public Link self() {
        return entityLinks.linkToCollectionResource(AttributeResource.class);
    }

    /**
     * Get link to collection of attributes
     * @param  vocabulary attributes of
     * @return            collection of attributes
     */
    public Link attributes(Vocabulary vocabulary) {
        return entityLinks.linkFor(AttributeResource.class).slash(vocabulary.getName()).withRel(REL_ATTRIBUTES);
    }

    /**
     * Get link to attribute resource
     * @param vocabulary vocabulary
     * @param attribute  attribute
     * @return            link to resource
     */
    public Link attribute(Attribute attribute) {
        return entityLinks.linkFor(AttributeResource.class).slash(attribute.getVocabulary().getName()).slash(attribute.getId()).withRel(REL_ATTRIBUTE);
    }

    /**
     * Get link to parent attribute
     * @param attribute attribute
     * @return          link to parent
     */
    public Link parent(Attribute attribute) {
        return entityLinks.linkFor(AttributeResource.class).slash(attribute.getVocabulary().getName()).slash(attribute.getParent().getId()).withRel(REL_PARENT);
    }

    /**
     * Get link to attribute search page
     * @return link to search
     */
    public Link search() {
        return entityLinks.linkFor(AttributeResource.class).slash(SEARCH).withRel(REL_SEARCH);
    }

    /**
     * Get link to attribute search by level starts with
     * @return  link to search by level starts with
     */
    public Link levelStartsWith() {
        String template = toTemplate(entityLinks.linkFor(AttributeResource.class).slash(LEVEL_STARTS).toString(), TERMS, PAGEABLE);
        return new Link(template, REL_LEVEL_STARTS);
    }

    /**
     * Get link to attribute search by level contains
     * @return link to search by level contains
     */
    public Link levelContains() {
        String template = toTemplate(entityLinks.linkFor(AttributeResource.class).slash(LEVEL_CONTAINS).toString(), TERMS, PAGEABLE);
        return new Link(template, REL_LEVEL_CONTAINS);
    }

    /**
     * Get link to attribute search by name starts with
     * @return  link to search by name starts with
     */
    public Link nameStartsWith() {
        String template = toTemplate(entityLinks.linkFor(AttributeResource.class).slash(NAME_STARTS).toString(), TERMS, PAGEABLE);
        return new Link(template, REL_NAME_STARTS);
    }

    /**
     * Get link to attribute search by name contains
     * @return link to search by name contains
     */
    public Link nameContains() {
        String template = toTemplate(entityLinks.linkFor(AttributeResource.class).slash(NAME_CONTAINS).toString(), TERMS, PAGEABLE);
        return new Link(template, REL_NAME_CONTAINS);
    }


    /**
     * Get link to attribute search by requirementLevel starts with
     * @return  link to search by requirementLevel starts with
     */
    public Link requirementLevelStartsWith() {
        String template = toTemplate(entityLinks.linkFor(AttributeResource.class).slash(REQUIREMENT_LEVEL_STARTS).toString(), TERMS, PAGEABLE);
        return new Link(template, REL_REQUIREMENT_LEVEL_STARTS);
    }

    /**
     * Get link to attribute search by requirementLevel contains
     * @return link to search by requirementLevel contains
     */
    public Link requirementLevelContains() {
        String template = toTemplate(entityLinks.linkFor(AttributeResource.class).slash(REQUIREMENT_LEVEL_CONTAINS).toString(), TERMS, PAGEABLE);
        return new Link(template, REL_REQUIREMENT_LEVEL_CONTAINS);
    }

    /**
     * Get link to attribute search page by vocabulary
     * @return link to search by vocabulary
     */
    public Link vocabularySearch(Vocabulary vocabulary) {
        return entityLinks.linkFor(AttributeResource.class).slash(vocabulary.getName()).slash(SEARCH).withRel(REL_SEARCH);
    }

    /**
     * Get link to attribute search by level starts with
     * @return  link to search by level starts with
     */
    public Link vocabularyLevelStartsWith(Vocabulary vocabulary) {
        String template = toTemplate(entityLinks.linkFor(AttributeResource.class).slash(vocabulary.getName()).slash(LEVEL_STARTS).toString(), TERMS, PAGEABLE);
        return new Link(template, REL_LEVEL_STARTS);
    }

    /**
     * Get link to attribute search by level contains
     * @return link to search by level contains
     */
    public Link vocabularyLevelContains(Vocabulary vocabulary) {
        String template = toTemplate(entityLinks.linkFor(AttributeResource.class).slash(vocabulary.getName()).slash(LEVEL_CONTAINS).toString(), TERMS, PAGEABLE);
        return new Link(template, REL_LEVEL_CONTAINS);
    }

    /**
     * Get link to attribute search by name starts with
     * @return  link to search by name starts with
     */
    public Link vocabularyNameStartsWith(Vocabulary vocabulary) {
        String template = toTemplate(entityLinks.linkFor(AttributeResource.class).slash(vocabulary.getName()).slash(NAME_STARTS).toString(), TERMS, PAGEABLE);
        return new Link(template, REL_NAME_STARTS);
    }

    /**
     * Get link to attribute search by name contains
     * @return link to search by name contains
     */
    public Link vocabularyNameContains(Vocabulary vocabulary) {
        String template = toTemplate(entityLinks.linkFor(AttributeResource.class).slash(vocabulary.getName()).slash(NAME_CONTAINS).toString(), TERMS, PAGEABLE);
        return new Link(template, REL_NAME_CONTAINS);
    }


    /**
     * Get link to attribute search by requirementLevel starts with
     * @return  link to search by requirementLevel starts with
     */
    public Link vocabularyRequirementLevelStartsWith(Vocabulary vocabulary) {
        String template = toTemplate(entityLinks.linkFor(AttributeResource.class).slash(vocabulary.getName()).slash(REQUIREMENT_LEVEL_STARTS).toString(), TERMS, PAGEABLE);
        return new Link(template, REL_REQUIREMENT_LEVEL_STARTS);
    }

    /**
     * Get link to attribute search by requirementLevel contains
     * @return link to search by requirementLevel contains
     */
    public Link vocabularyRequirementLevelContains(Vocabulary vocabulary) {
        String template = toTemplate(entityLinks.linkFor(AttributeResource.class).slash(vocabulary.getName()).slash(REQUIREMENT_LEVEL_CONTAINS).toString(), TERMS, PAGEABLE);
        return new Link(template, REL_REQUIREMENT_LEVEL_CONTAINS);
    }
}
