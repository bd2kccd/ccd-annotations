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

package edu.pitt.dbmi.ccd.anno.metadata;

import java.util.Set;
import java.util.HashSet;
import java.util.Date;
import org.springframework.hateoas.core.Relation;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Link;
import edu.pitt.dbmi.ccd.db.entity.Annotation;

/**
 * Annotation entity DTO representation
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Relation(value="annotation", collectionRelation="annotations")
public final class AnnotationResource extends ResourceSupport {

    // content
    private final Long id;
    private final Date created;
    private final Date modified;
    private final String access;
    // private final Set<Data> data = new HashSet<>(0);

    /**
     * Empty constructor
     * @return AnnotationResource with empty variables
     */
    protected AnnotationResource() {
        this.id = null;
        this.created = null;
        this.modified = null;
        this.access = "";
    }

    /**
     * Constructor
     * @param  annotation content
     */
    public AnnotationResource(Annotation annotation) {
        this.id = annotation.getId();
        this.created = annotation.getCreated();
        this.modified = annotation.getModified();
        this.access = annotation.getAccessControl().getName();
    }

    /**
     * Constructor
     * @param  annotation content
     * @param  links (optional) links to include
     */
    public AnnotationResource(Annotation annotation, Link... links) {
        this(annotation);
        this.add(links);
    }

    /**
     * Get id
     * @return id
     */
    public Long getIdentifier() {
        return id;
    }

    /**
     * Get created date
     * @return created date
     */
    public Date getCreated() {
        return created;
    }

    /**
     * Get modified date
     * @return modified date
     */
    public Date getModified() {
        return modified;
    }

    /**
     * Get access control level
     * @return access control
     */
    public String getAccess() {
        return access;
    }
}
