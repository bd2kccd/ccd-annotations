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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.Link;
import edu.pitt.dbmi.ccd.db.entity.Annotation;
import edu.pitt.dbmi.ccd.anno.links.ResourceLinks;

/**
 * Annotation links
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Component
public class AnnotationLinks implements ResourceLinks {

    // annotation links
    public static final String INDEX = "/meta";
    public static final String ANNOTATION = "/{id}";

    // annotations rels
    public final String REL_ANNOTATION;
    public final String REL_ANNOTATIONS;

    // query parameters

    // dependencies
    private final EntityLinks entityLinks;
    private final RelProvider relProvider;

    @Autowired(required=true)
    public AnnotationLinks(EntityLinks entityLinks, RelProvider relProvider) {
        this.entityLinks = entityLinks;
        this.relProvider = relProvider;
        REL_ANNOTATION = relProvider.getItemResourceRelFor(AnnotationResource.class);
        REL_ANNOTATIONS = relProvider.getCollectionResourceRelFor(AnnotationResource.class);
    }

    /**
     * Get link to annotation resource collection
     * @return link to collection
     */
    public Link annotations() {
        String template = toTemplate(entityLinks.linkFor(AnnotationResource.class).toString(), PAGEABLE);
        return new Link(template, REL_ANNOTATIONS);
    }

    /**
     * Get link to a annotation resource
     * @param  name annotation name
     * @return      link to resource
     */
    public Link annotation(Annotation annotation) {
        return entityLinks.linkForSingleResource(AnnotationResource.class, annotation.getId()).withRel(REL_ANNOTATION);
    }

    /**
     * Get link to annotation search page
     * @return link to search
     */
    public Link search() {
        String template = toTemplate(entityLinks.linkFor(AnnotationResource.class).slash(SEARCH).toString(), PAGEABLE);
        return new Link(template, REL_SEARCH);
    }
}
