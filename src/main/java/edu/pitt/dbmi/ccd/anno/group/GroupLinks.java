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
    public static final String INDEX = "/gs";
    public static final String GROUP = "/{name}";
    public static final String NAME_STARTS = "/nameStartsWith";
    public static final String NAME_CONTAINS = "/nameContains";
    public static final String DESCRIPTION_CONTAINS = "/descriptionContains";

    // groups rels
    public static final String REL_GROUP = "group";
    public static final String REL_NAME_STARTS = "nameStartsWith";
    public static final String REL_NAME_CONTAINS = "nameContains";
    public static final String REL_DESCRIPTION_CONTAINS = "descriptionContains";

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
     * Get link to group searc by name starts with
     * @return link to search by name starts with
     */
    public Link nameStartsWith() {
        String template = toTemplate(entityLinks.linkFor(GroupResource.class).slash(SEARCH).slash(NAME_STARTS).toString(), TERMS, PAGEABLE);
        return new Link(template, REL_NAME_STARTS);
    }

    /**
     * Get link to group search by name contains
     * @return link to search by name contains
     */
    public Link nameContains() {
        String template = toTemplate(entityLinks.linkFor(GroupResource.class).slash(SEARCH).slash(NAME_CONTAINS).toString(), TERMS, PAGEABLE);
        return new Link(template, REL_NAME_CONTAINS);
    }

    /**
     * Get linkt og roup search by description contains
     * @return link to search by description contains
     */
    public Link descriptionContains() {
        String template = toTemplate(entityLinks.linkFor(GroupResource.class).slash(SEARCH).slash(DESCRIPTION_CONTAINS).toString(), TERMS, PAGEABLE);
        return new Link(template, REL_DESCRIPTION_CONTAINS);
    }
}
