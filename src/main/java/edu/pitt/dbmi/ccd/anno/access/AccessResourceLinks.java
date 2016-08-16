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

package edu.pitt.dbmi.ccd.anno.access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RelProvider;
import org.springframework.stereotype.Component;

import edu.pitt.dbmi.ccd.anno.links.ResourceLinks;
import edu.pitt.dbmi.ccd.db.entity.Access;

/**
 * Access links
 *
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Component
public class AccessResourceLinks implements ResourceLinks {

    // access links
    public static final String INDEX = "/accesses";
    public static final String ACCESS = "/{id}";

    // access rels
    private final String REL_ACCESS;
    private final String REL_ACCESSES;

    // dependencies
    private final EntityLinks entityLinks;

    @Autowired(required = true)
    public AccessResourceLinks(EntityLinks entityLinks, RelProvider relProvider) {
        this.entityLinks = entityLinks;
        REL_ACCESS = relProvider.getItemResourceRelFor(AccessResource.class);
        REL_ACCESSES = relProvider.getCollectionResourceRelFor(AccessResource.class);
    }

    /**
     * Get link to access resource collection
     *
     * @return link to collection
     */
    public Link accesses() {
        String template = toTemplate(entityLinks.linkFor(AccessResource.class).toString(), PAGEABLE);
        return new Link(template, REL_ACCESSES);
    }

    /**
     * Get link to access resource
     *
     * @param access entity
     * @return link to resource
     */
    public Link access(Access access) {
        return entityLinks.linkForSingleResource(AccessResource.class, access.getId()).withRel(REL_ACCESS);
    }

    // no search for Accesses
    public Link search() {
        return null;
    }
}
