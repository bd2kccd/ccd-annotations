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

package edu.pitt.dbmi.ccd.anno.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.Link;
import edu.pitt.dbmi.ccd.db.entity.Upload;
import edu.pitt.dbmi.ccd.anno.links.ResourceLinks;

/**
 * Upload links
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Component
public class UploadLinks implements ResourceLinks {

    // annotation links
    public static final String INDEX = "/data";
    public static final String DATA = "/{id}";
    public static final String ANNOTATIONS = "/{id}/annotations";

    // annotations rels
    private final String REL_DATA;
    private final String REL_ANNOTATIONS = "annotations";
    private final String REL_USER = "user";

    // query parameters
    // filter
    // public static final String USER = "user";
    // public static final String GROUP = "group";
    // public static final String UPLOAD = "upload";
    // public static final String VOCAB = "vocab";
    // public static final String LEVEL = "level";
    // public static final String NAME = "name";
    // public static final String REQUIREMENT = "requirement";
    // public static final String REDACTED = "showRedacted";

    //search
    // public static final String QUERY = "query";
    // public static final String NOT = "not";

    // dependencies
    private final EntityLinks entityLinks;
    private final RelProvider relProvider;

    @Autowired(required=true)
    public AnnotationLinks(EntityLinks entityLinks, RelProvider relProvider) {
        this.entityLinks = entityLinks;
        this.relProvider = relProvider;
        REL_DATA = relProvider.getItemResourceRelFor(UploadResource.class);
    }

    /**
     * Get link to upload resource collection
     * @return link to collection
     */
    public Link uploads() {
        String template = toTemplate(entityLinks.linkFor(UploadResource.class).toString(), PAGEABLE);
        return new Link(template, REL_DATA);
    }

    /**
     * Get link to a upload resource
     * @param  upload  upload entity
     * @return         link to resource
     */
    public Link upload(Upload upload) {
        return entityLinks.linkForSingleResource(UploadResource.class, upload.getId()).withRel(REL_DATA);
    }

    /**
     * Get link to annotation search page
     * @return link to search
     */
    public Link search() {
        String template = toTemplate(entityLinks.linkFor(UploadResource.class).slash(SEARCH).toString(), PAGEABLE);
        return new Link(template, REL_SEARCH);
    }
}
