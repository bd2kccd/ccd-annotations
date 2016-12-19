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
package edu.pitt.dbmi.ccd.annotations.group;

import edu.pitt.dbmi.ccd.annotations.annotation.AnnotationLinks;
import edu.pitt.dbmi.ccd.annotations.annotation.AnnotationResource;
import edu.pitt.dbmi.ccd.annotations.links.ResourceLinks;
import edu.pitt.dbmi.ccd.db.entity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RelProvider;
import org.springframework.stereotype.Component;

/**
 * Group links
 *
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Component
public class GroupLinks implements ResourceLinks {

    // group links
    public static final String INDEX = "/groups";
    public static final String GROUP = "/{id}";
    public static final String MODS = "/{id}/moderators";
    public static final String MEMBERS = "/{id}/members";
    public static final String JOIN = "/{id}/join";
    public static final String LEAVE = "/{id}/leave";
    public static final String REQUESTS = "/{id}/requests";
    // query parameters
    private static final String QUERY = "query";
    private static final String NOT = "not";
    private static final String ACCEPT = "accept";
    private static final String REMOVE = "remove";
    private static final String MAKE_MOD = "mod";
    // groups rels
    public final String REL_GROUP;
    public final String REL_GROUPS;
    public final String REL_ANNOS;
    public final String REL_MODS = "moderators";
    public final String REL_MEMBERS = "members";
    public final String REL_JOIN = "join";
    public final String REL_LEAVE = "leave";
    public final String REL_REQUESTS = "requests";
    // dependencies
    private final EntityLinks entityLinks;
    private final RelProvider relProvider;

    @Autowired(required = true)
    public GroupLinks(EntityLinks entityLinks, RelProvider relProvider) {
        this.entityLinks = entityLinks;
        this.relProvider = relProvider;
        REL_GROUP = relProvider.getItemResourceRelFor(GroupResource.class);
        REL_GROUPS = relProvider.getCollectionResourceRelFor(GroupResource.class);
        REL_ANNOS = relProvider.getCollectionResourceRelFor(AnnotationResource.class);
    }

    /**
     * Get link to group resource collection
     *
     * @return link to collection
     */
    public Link groups() {
        String template = toTemplate(entityLinks.linkFor(GroupResource.class).toString(), PAGEABLE);
        return new Link(template, REL_GROUPS);
    }

    /**
     * Get link to a group resource
     *
     * @param group group
     * @return link to resource
     */
    public Link group(Group group) {
        return entityLinks.linkForSingleResource(GroupResource.class, group.getId()).withRel(REL_GROUP);
    }

    /**
     * Get link to group mods
     *
     * @param group group
     * @return link to resources
     */
    public Link mods(Group group) {
        return entityLinks.linkForSingleResource(GroupResource.class, group.getId()).slash(REL_MODS).withRel(REL_MODS);
    }

    /**
     * Get link to group members
     *
     * @param group group
     * @return link to resources
     */
    public Link members(Group group) {
        return entityLinks.linkForSingleResource(GroupResource.class, group.getId()).slash(REL_MEMBERS).withRel(REL_MEMBERS);
    }

    /**
     * Get link to group join requests
     *
     * @param group group
     * @return link to resources
     */
    public Link requesters(Group group) {
        return entityLinks.linkForSingleResource(GroupResource.class, group.getId()).slash(REL_REQUESTS).withRel(REL_REQUESTS);
    }

    /**
     * Get link to join group
     *
     * @param group group
     * @return link to join group
     */
    public Link join(Group group) {
        return entityLinks.linkForSingleResource(GroupResource.class, group.getId()).slash(REL_JOIN).withRel(REL_JOIN);
    }

    /**
     * Get link to leave group
     *
     * @param group group
     * @return link to leave group
     */
    public Link leave(Group group) {
        return entityLinks.linkForSingleResource(GroupResource.class, group.getId()).slash(REL_LEAVE).withRel(REL_LEAVE);
    }

    /**
     * Get link to group search page
     *
     * @return link to search
     */
    public Link search() {
        String template = toTemplate(entityLinks.linkFor(GroupResource.class).slash(SEARCH).toString(), QUERY, NOT, PAGEABLE);
        return new Link(template, REL_SEARCH);
    }

    /**
     * Get link to group's annotations
     *
     * @return link to annotations
     */
    public Link annotations(Group group) {
        String template = linkToCollection(entityLinks.linkFor(AnnotationResource.class).toString(), AnnotationLinks.GROUP, group.getName());
        return new Link(template, REL_ANNOS);
    }
}
