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

import edu.pitt.dbmi.ccd.db.entity.Group;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

/**
 * Group entity DTO representation
 *
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Relation(value = "group", collectionRelation = "groups")
public final class GroupResource extends ResourceSupport {

    // content
    private final String name;
    private final String description;

    /**
     * Empty constructor
     *
     * @return GroupResource with empty variables
     */
    protected GroupResource() {
        this.name = "";
        this.description = "";
    }

    /**
     * Constructor
     *
     * @param group content
     */
    public GroupResource(Group group) {
        this.name = group.getName();
        this.description = group.getDescription();
    }

    /**
     * Constructor
     *
     * @param group content
     * @param links (optional) links to include
     */
    public GroupResource(Group group, Link... links) {
        this(group);
        this.add(links);
    }

    /**
     * Get name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get description
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }
}
