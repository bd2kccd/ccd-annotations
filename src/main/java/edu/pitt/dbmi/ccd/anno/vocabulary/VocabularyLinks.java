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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import edu.pitt.dbmi.ccd.db.entity.Vocabulary;
import edu.pitt.dbmi.ccd.anno.links.ResourceLinks;

/**
 * Vocabulary links
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Component
public class VocabularyLinks implements ResourceLinks {

    // vocabulary links
    public static final String INDEX = "/vs";
    public static final String VOCABULARY = "/{name}";
    public static final String NAME_STARTS = "/search/nameStartsWith";
    public static final String NAME_CONTAINS = "/search/nameContains";
    public static final String DESCRIPTION_CONTAINS = "/search/descriptionContains";

    // vocabulary rels
    public static final String REL_VOCABULARY = "vocabulary";
    public static final String REL_NAME_STARTS = "nameStartsWith";
    public static final String REL_NAME_CONTAINS = "nameContains";
    public static final String REL_DESCRIPTION_CONTAINS = "descriptionContains";

    // query parameters
    public static final String TERMS = "terms";

    // dependencies
    private final EntityLinks entityLinks;

    @Autowired(required=true)
    public VocabularyLinks(EntityLinks entityLinks) {
        this.entityLinks = entityLinks;
    }

    /**
     * Get link to vocabulary resource collection
     * @return link to collection
     */
    public Link self() {
        return entityLinks.linkToCollectionResource(VocabularyResource.class);
    }

    /**
     * Get link to vocabulary resource
     * @param  name vocabulary name
     * @return      link to resource
     */
    public Link vocabulary(Vocabulary vocabulary) {
        return entityLinks.linkForSingleResource(VocabularyResource.class, vocabulary.getName()).withRel(REL_VOCABULARY);
    }

    /**
     * Get link to vocabulary search page
     * @return link to search
     */
    public Link search() {
        return entityLinks.linkFor(VocabularyResource.class).slash(SEARCH).withRel(REL_SEARCH);
    }

    /**
     * Get link to vocabulary search by name starts with
     * @return  link to search by name starts with
     */
    public Link nameStartsWith() {
        String template = toTemplate(entityLinks.linkFor(VocabularyResource.class).slash(NAME_STARTS).toString(), TERMS, PAGEABLE);
        return new Link(template, REL_NAME_STARTS);
    }

    /**
     * Get link to vocabulary search by name contains
     * @return link to search by name contains
     */
    public Link nameContains() {
        String template = toTemplate(entityLinks.linkFor(VocabularyResource.class).slash(NAME_CONTAINS).toString(), TERMS, PAGEABLE);
        return new Link(template, REL_NAME_CONTAINS);
    }

    /**
     * Get link to vocabulary search by description contains
     * @return link to search by description contains
     */
    public Link descriptionContains() {
        String template = toTemplate(entityLinks.linkFor(VocabularyResource.class).slash(DESCRIPTION_CONTAINS).toString(), TERMS, PAGEABLE);
        return new Link(template, REL_DESCRIPTION_CONTAINS);
    }
}
