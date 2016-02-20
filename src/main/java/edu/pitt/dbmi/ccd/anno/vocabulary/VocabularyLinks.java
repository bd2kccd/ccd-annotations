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
import org.springframework.hateoas.RelProvider;
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
    public static final String INDEX = "/vocabularies";
    public static final String VOCABULARY = "/{name}";

    // vocabulary rels
    public final String REL_VOCABULARY;
    public final String REL_VOCABULARIES;

    // query parameters
    private static final String NAME_CONTAINS = "nameContains";
    private static final String DESCRIPTION_CONTAINS = "descriptionContains";

    // dependencies
    private final EntityLinks entityLinks;
    private final RelProvider relProvider;

    @Autowired(required=true)
    public VocabularyLinks(EntityLinks entityLinks, RelProvider relProvider) {
        this.entityLinks = entityLinks;
        this.relProvider = relProvider;
        REL_VOCABULARY = relProvider.getItemResourceRelFor(VocabularyResource.class);
        REL_VOCABULARIES = relProvider.getCollectionResourceRelFor(VocabularyResource.class);
    }

    /**
     * Get link to vocabulary resource collection
     * @return link to collection
     */
    public Link vocabularies() {
        String template = toTemplate(entityLinks.linkFor(VocabularyResource.class).toString(), PAGEABLE);
        return new Link(template, REL_VOCABULARIES);
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
        String template = toTemplate(entityLinks.linkFor(VocabularyResource.class).slash(SEARCH).toString(), NAME_CONTAINS, DESCRIPTION_CONTAINS, PAGEABLE);
        return new Link(template, REL_SEARCH);
    }
}
