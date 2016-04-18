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

package edu.pitt.dbmi.ccd.anno.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.Link;
import edu.pitt.dbmi.ccd.db.entity.UserAccount;
import edu.pitt.dbmi.ccd.anno.links.ResourceLinks;
import edu.pitt.dbmi.ccd.anno.annotation.AnnotationResource;
import edu.pitt.dbmi.ccd.anno.annotation.AnnotationLinks;
import edu.pitt.dbmi.ccd.anno.data.UploadResource;
import edu.pitt.dbmi.ccd.anno.group.GroupResource;
import edu.pitt.dbmi.ccd.anno.data.UploadLinks;

/**
 * User links
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Component
public class UserLinks implements ResourceLinks {

    // user links
    public static final String INDEX = "/users";
    public static final String USER = "/{username}";
    public static final String GROUPS = "/{username}/groups";

    // users rels
    public final String REL_USER;
    public final String REL_USERS;
    public final String REL_ANNOS;
    public final String REL_UPLOADS;
    public final String REL_GROUPS;

    // query parameters
    public final String EMAIL = "email";
    public final String MEMBER = "member";
    public final String MOD = "moderator";
    public final String REQUESTS = "requests";

    // dependencies
    private final EntityLinks entityLinks;
    private final RelProvider relProvider;

    @Autowired(required=true)
    public UserLinks(EntityLinks entityLinks, RelProvider relProvider) {
        this.entityLinks = entityLinks;
        this.relProvider = relProvider;
        REL_USER = relProvider.getItemResourceRelFor(UserResource.class);
        REL_USERS = relProvider.getCollectionResourceRelFor(UserResource.class);
        REL_ANNOS = relProvider.getCollectionResourceRelFor(AnnotationResource.class);
        REL_UPLOADS = relProvider.getCollectionResourceRelFor(UploadResource.class);
        REL_GROUPS = relProvider.getCollectionResourceRelFor(GroupResource.class);
    }

    /**
     * Get link to user resource collection
     * @return link to collection
     */
    public Link users() {
        String template = toTemplate(entityLinks.linkFor(UserResource.class).toString(), PAGEABLE);
        return new Link(template, REL_USERS);
    }

    /**
     * Get link to a user resource
     * @param  name user name
     * @return      link to resource
     */
    public Link user(UserAccount account) {
        return entityLinks.linkForSingleResource(UserResource.class, account.getUsername()).withRel(REL_USER);
    }

    /**
     * Get link to groups to which user belongs
     */
    public Link groups(UserAccount account) {
        String template = toTemplate(entityLinks.linkForSingleResource(UserResource.class, account.getUsername()).slash(REL_GROUPS).toString(), MOD, REQUESTS, PAGEABLE);
        return new Link(template, REL_GROUPS);
    }

    /**
     * Get link to user search page
     * @return link to search
     */
    public Link search() {
        String template = toTemplate(entityLinks.linkFor(UserResource.class).slash(SEARCH).toString(), EMAIL);
        return new Link(template, REL_SEARCH);
    }

    /**
     * Get link to user's annotations
     * @return link to annotations
     */
    public Link annotations(UserAccount user) {
        String template = linkToCollection(entityLinks.linkFor(AnnotationResource.class).toString(), AnnotationLinks.USER, user.getUsername());
        return new Link(template, REL_ANNOS);
    }

    /**
     * Get link to user's uploads
     * @return link to uploads
     */
    public Link uploads(UserAccount user) {
        String template = linkToCollection(entityLinks.linkFor(UploadResource.class).toString(), UploadLinks.USER, user.getUsername());
        return new Link(template, REL_UPLOADS);
    }
}