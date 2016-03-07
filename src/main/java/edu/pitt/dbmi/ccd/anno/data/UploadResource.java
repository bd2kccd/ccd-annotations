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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import edu.pitt.dbmi.ccd.db.entity.Upload;

/**
 * Upload entity DTO representation
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Relation(value="data", collectionRelation="data")
public final class UploadResource extends ResourceSupport {

    // content
    private final Long id;
    private final Date created;
    private final Date modified;
    private final String uploader;
    private final String title;
    private final String file;
    private final String address;

    /**
     * Empty constructor
     * @return AnnotationResource with empty variables
     */
    protected AnnotationResource() {
        this.id = null;
        this.created = null;
        this.modified = null;
        this.uploader = "";
        this.title = "";
        this.file = "";
        this.address = "";
    }

    /**
     * Constructor
     * @param  upload content
     */
    public UploadResource(Upload upload) {
        this.id = upload.getId();
        this.created = upload.getCreated();
        this.modified = upload.getModified();
        this.uploader = upload.getUploader().getUsername();
        this.title = upload.getTitle();
        if (upload.getFile() != null) {
            this.file = upload.getFile().getName();
            this.address = null;
        } else {
            this.file = null;
            this.address = upload.getAddress();
        }
    }

    /**
     * Constructor
     * @param  upload content
     * @param  links (optional) links to include
     */
    public UploadResource(Upload upload, Link... links) {
        this(upload);
        this.add(links);
    }

    /**
     * Get id
     * @return id
     */
    // public Long getIdentifier() {
    //     return id;
    // }

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
     * Get uploader
     * @return username
     */
    public String getUploader() {
        return uploader;
    }

    /**
     * Get title
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get file name
     * @return file name
     */
    @JsonInclude(Include.NON_NULL)
    public String getFile() {
        return file;
    }

    /**
     * Get address
     * @return address
     */
    @JsonInclude(Include.NON_NULL)
    public String getAddress() {
        return address;
    }
}
