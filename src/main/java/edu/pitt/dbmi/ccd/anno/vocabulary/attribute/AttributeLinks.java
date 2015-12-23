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
import edu.pitt.dbmi.ccd.anno.links.ResourceLinks;

/**
 * Attribute links
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Component
public class AttributeLinks implements ResourceLinks {

    // attribute links
    public static final String INDEX = "/vs/{vocabulary}/attributes";
    public static final String ATTRIBUTE = "/{innerId}";

    // attribute rels
    public static final String REL_ATTRIBUTE = "attribute";

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
     * Get link to attribute resource
     * @param  name attribute name
     * @return      link to resource
     */
    public Link attribute(Attribute attribute) {
        return entityLinks.linkForSingleResource(AttributeResource.class, attribute.getId()).withRel(REL_ATTRIBUTE);
    }

    /**
     * Get link to attribute search page
     * @return link to search
     */
    public Link search() {
        return entityLinks.linkFor(AttributeResource.class).slash(SEARCH).withRel(REL_SEARCH);
    }
}
