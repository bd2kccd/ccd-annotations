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
import edu.pitt.dbmi.ccd.anno.metadata.AnnotationResource;
import edu.pitt.dbmi.ccd.anno.metadata.AnnotationLinks;

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
    public static final String ADMINS = "/{name}/admins";

    // groups rels
    public final String REL_GROUP;
    public final String REL_GROUPS;
    public final String REL_ANNOS;
    public final String REL_ADMINS = "admins";

    // query parameters
    private static final String QUERY = "query";
    private static final String NOT = "not";

    // dependencies
    private final EntityLinks entityLinks;
    private final RelProvider relProvider;

    @Autowired(required=true)
    public GroupLinks(EntityLinks entityLinks, RelProvider relProvider) {
        this.entityLinks = entityLinks;
        this.relProvider = relProvider;
        REL_GROUP = relProvider.getItemResourceRelFor(GroupResource.class);
        REL_GROUPS = relProvider.getCollectionResourceRelFor(GroupResource.class);
        REL_ANNOS = relProvider.getCollectionResourceRelFor(AnnotationResource.class);
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
     * @param  group group
     * @return       link to resource
     */
    public Link group(Group group) {
        return entityLinks.linkForSingleResource(GroupResource.class, group.getName()).withRel(REL_GROUP);
    }

    /**
     * Get link to group admins
     * @param  group group
     * @return       link to resources
     */
    public Link admins(Group group) {
        return entityLinks.linkForSingleResource(GroupResource.class, group.getName()).slash(REL_ADMINS).withRel(REL_ADMINS);
    }

    /**
     * Get link to group search page
     * @return link to search
     */
    public Link search() {
        String template = toTemplate(entityLinks.linkFor(GroupResource.class).slash(SEARCH).toString(), QUERY, NOT, PAGEABLE);
        return new Link(template, REL_SEARCH);
    }

    /**
     * Get link to group's annotations
     * @return link to annotations
     */
    public Link annotations(Group group) {
        String template = linkToCollection(entityLinks.linkFor(AnnotationResource.class).toString(), AnnotationLinks.GROUP, group.getName());
        return new Link(template, REL_ANNOS);
    }
}
