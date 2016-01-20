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

package edu.pitt.dbmi.ccd.anno.annotation;

import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import org.springframework.hateoas.core.Relation;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Link;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import edu.pitt.dbmi.ccd.db.entity.Annotation;
import edu.pitt.dbmi.ccd.db.entity.AnnotationData;

/**
 * Annotation entity DTO representation
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Relation(value="annotation", collectionRelation="annotations")
public final class AnnotationResource extends ResourceSupport {

    // content

    /**
     * Empty constructor
     * @return  AnnotationResource with empty/null variables
     */
    protected AnnotationResource() {
    }

    /**
     * Constructor
     * @param  annotation content
     * @return       new AnnotationResource
     */
    public AnnotationResource(Annotation annotation) {
    }

    /**
     * Constructor
     * @param  annotation content
     * @param  links (optional) links to include
     * @return       new AnnotationResource
     */
    public AnnotationResource(Annotation annotation, Link... links) {
        this(annotation);
        this.add(links);
    }
}
