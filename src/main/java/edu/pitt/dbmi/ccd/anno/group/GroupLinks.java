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
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.Link;
import edu.pitt.dbmi.ccd.db.entity.Group;
import edu.pitt.dbmi.ccd.anno.links.ResourceLinks;

/**
 * Group links
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Component
public class GroupLinks implements ResourceLinks {

    // group links
    public static final String INDEX = "/groups";
    public static final String GROUP = "/{name}";

    // groups rels
    public final String REL_GROUP;
    public final String REL_GROUPS;

    // query parameters
    private static final String TERMS = "terms";
    // private static final String NAME_CONTAINS = "nameContains";
    // private static final String DESCRIPTION_CONTAINS = "descriptionContains";

    // dependencies
    private final EntityLinks entityLinks;
    private final RelProvider relProvider;

    @Autowired(required=true)
    public GroupLinks(EntityLinks entityLinks, RelProvider relProvider) {
        this.entityLinks = entityLinks;
        this.relProvider = relProvider;
        REL_GROUP = relProvider.getItemResourceRelFor(GroupResource.class);
        REL_GROUPS = relProvider.getCollectionResourceRelFor(GroupResource.class);
    }

    /**
     * Get link to group resource collection
     * @return link to collection
     */
    public Link groups() {
        String template = toTemplate(entityLinks.linkFor(GroupResource.class).toString(), PAGEABLE);
        return new Link(template, REL_GROUPS);
    }

    /**
     * Get link to a group resource
     * @param  name group name
     * @return      link to resource
     */
    public Link group(Group group) {
        return entityLinks.linkForSingleResource(GroupResource.class, group.getName()).withRel(REL_GROUP);
    }

    /**
     * Get link to group search page
     * @return link to search
     */
    public Link search() {
        String template = toTemplate(entityLinks.linkFor(GroupResource.class).slash(SEARCH).toString(), TERMS, PAGEABLE);
        return new Link(template, REL_SEARCH);
    }
}
