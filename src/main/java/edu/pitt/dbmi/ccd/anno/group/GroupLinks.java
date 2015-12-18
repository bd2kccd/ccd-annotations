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

package edu.pitt.dbmi.ccd.anno.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import edu.pitt.dbmi.ccd.anno.links.ResourceLinks;

/**
 * Group links
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Component
public class GroupLinks implements ResourceLinks {

    // group links
    public static final String GROUP = "/{name}";
    public static final String SEARCH = "/search";
    public static final String NAME_CONTAINS = "/search/byNameContains";
    public static final String DESCRIPTION_CONTAINS = "/search/byDescriptionContains";

    // groups rels
    public static final String REL_GROUP = "group";
    public static final String REL_SEARCH = "search";
    public static final String REL_NAME_CONTAINS = "byNameContains";
    public static final String REL_DESCRIPTION_CONTAINS = "byDescriptionContains";

    // query parameters
    private static final String TERMS = "terms";

    private final EntityLinks entityLinks;

    @Autowired(required=true)
    public GroupLinks(EntityLinks entityLinks) {
        this.entityLinks = entityLinks;
    }

    /**
     * Get link to group resource collection
     * @return link to collection
     */
    public Link self() {
        return entityLinks.linkToCollectionResource(GroupResource.class);
    }

    /**
     * Get link to a group resource
     * @param  name group name
     * @return      link to resource
     */
    public Link group(String name) {
        return entityLinks.linkForSingleResource(GroupResource.class, name).withRel(REL_GROUP);
    }

    /**
     * Get link to group search page
     * @return link to search
     */
    public Link search() {
        return entityLinks.linkFor(GroupResource.class).slash(SEARCH).withRel(REL_SEARCH);
    }

    /**
     * Get link to group search by name
     * @return link to search by name
     */
    public Link nameContains() {
        String template = toTemplate(entityLinks.linkFor(GroupResource.class).slash(NAME_CONTAINS).toString(), TERMS, PAGEABLE);
        return new Link(template, REL_NAME_CONTAINS);
    }

    /**
     * Get linkt og roup search by description
     * @return link to search by description
     */
    public Link descriptionContains() {
        String template = toTemplate(entityLinks.linkFor(GroupResource.class).slash(DESCRIPTION_CONTAINS).toString(), TERMS, PAGEABLE);
        return new Link(template, REL_DESCRIPTION_CONTAINS);
    }
}
