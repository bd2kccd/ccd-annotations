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
import org.springframework.hateoas.RelProvider;
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
    public static final String ATTRIBUTES = "/{vocabName}";
    public static final String ATTRIBUTE = "/{vocabName}/{id}";
    public static final String CHILDREN = "/{vocabName}/{id}/children";

    // attribute rels
    public final String REL_ATTRIBUTE;
    public final String REL_ATTRIBUTES;
    public static final String REL_PARENT = "parent";
    public static final String REL_CHILDREN = "children";

    // query parameters
    private static final String VOCAB = "vocab";
    private static final String LEVEL = "level";
    private static final String NAME = "name";
    private static final String REQUIREMENT = "requirement";
    private static final String LEVEL_CONTAINS = "levelContains";
    private static final String NAME_CONTAINS = "nameContains";
    private static final String REQUIREMENT_CONTAINS = "requirementContains";

    // dependencies
    private final EntityLinks entityLinks;
    private final RelProvider relProvider;

    @Autowired(required=true)
    public AttributeLinks(EntityLinks entityLinks, RelProvider relProvider) {
        this.entityLinks = entityLinks;
        this.relProvider = relProvider;
        REL_ATTRIBUTE = relProvider.getItemResourceRelFor(AttributeResource.class);
        REL_ATTRIBUTES = relProvider.getCollectionResourceRelFor(AttributeResource.class);
    }

    /**
     * Get link to collection of attributes
     * @return link to collection
     */
    public Link attributes() {
        String template = toTemplate(entityLinks.linkFor(AttributeResource.class).toString(), LEVEL, NAME, REQUIREMENT, PAGEABLE);
        return new Link(template, REL_ATTRIBUTES);
    }

    /**
     * Get link to collection of attributes in a vocabulary
     * @param  vocabulary attributes of
     * @return            collection of attributes
     */
    public Link attributes(Vocabulary vocabulary) {
        String template = toTemplate(entityLinks.linkFor(AttributeResource.class).slash(vocabulary.getName()).toString(), LEVEL, NAME, REQUIREMENT, PAGEABLE);
        return new Link(template, REL_ATTRIBUTES);
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
     * Get link to child attributes
     */
    public Link children(Attribute attribute) {
        return entityLinks.linkFor(AttributeResource.class).slash(attribute.getVocabulary().getName()).slash(attribute.getId()).slash(REL_CHILDREN).withRel(REL_CHILDREN);
    }

    /**
     * Get link to attribute search page
     * @return link to search
     */
    public Link search() {
        String template = toTemplate(entityLinks.linkFor(AttributeResource.class).slash(SEARCH).toString(), VOCAB, LEVEL_CONTAINS, NAME_CONTAINS, REQUIREMENT_CONTAINS, PAGEABLE);
        return new Link(template, REL_SEARCH);
    }
}
