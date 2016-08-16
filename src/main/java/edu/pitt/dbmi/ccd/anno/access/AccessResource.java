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

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import edu.pitt.dbmi.ccd.db.entity.Access;

/**
 * Access entity DTO representation
 *
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Relation(value = "access", collectionRelation = "accesses")
public final class AccessResource extends ResourceSupport {

    // content
    private String name;
    private String description;

    /**
     * Default no-args constructor
     */
    public AccessResource() {

    }

    /**
     * Constructor
     *
     * @param access content
     */
    public AccessResource(Access access) {
        this.name = access.getName();
        this.description = access.getDescription();
    }

    /**
     * Constructor
     *
     * @param access content
     * @param links  (optional) links to include
     */
    public AccessResource(Access access, Link... links) {
        this(access);
        this.add(links);
    }

    /**
     * Get access resource name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Set access resource name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get access resource description
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set access resource description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
