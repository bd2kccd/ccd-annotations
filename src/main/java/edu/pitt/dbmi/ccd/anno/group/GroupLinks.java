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

    public static final String SEARCH = "/search";
    public static final String NAME_CONTAINS = "/search/byNameContains";
    public static final String DESCRIPTION_CONTAINS = "/search/byDescriptionContains";
    public static final String SEARCH_REL = "search";
    public static final String NAME_CONTAINS_REL = "byNameContains";
    public static final String DESCRIPTION_CONTAINS_REL = "byDescriptionContains";

    private final EntityLinks entityLinks;

    @Autowired(required=true)
    GroupLinks(EntityLinks entityLinks) {
        this.entityLinks = entityLinks;
    }

    public Link self() {
        return entityLinks.linkFor(GroupResource.class).withSelfRel();
    }

    public Link search() {
        return entityLinks.linkFor(GroupResource.class).slash(SEARCH).withRel(SEARCH_REL);
    }

    public Link nameContains() {
        return new Link(String.format("%s{?%s}", entityLinks.linkFor(GroupResource.class).slash(NAME_CONTAINS).toString(), "terms,page,size,sort"), NAME_CONTAINS_REL);
    }

    public Link descriptionContains() {
        return new Link(String.format("%s{?%s}", entityLinks.linkFor(GroupResource.class).slash(DESCRIPTION_CONTAINS).toString(), "terms,page,size,sort"), DESCRIPTION_CONTAINS_REL);
    }
}
