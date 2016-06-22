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

import java.util.Date;
import org.springframework.hateoas.core.Relation;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Link;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import edu.pitt.dbmi.ccd.db.entity.AnnotationTarget;

/**
 * AnnotationTarget entity DTO representation
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Relation(value="data", collectionRelation="data")
@JsonPropertyOrder({"id"})
public final class AnnotationTargetResource extends ResourceSupport {

    // types
    private static final String FILE = "file";
    private static final String URL = "url";

    // content
    private final Long id;
    private final Date created;
    private final Date modified;
    private final String targeter;
    private final String type;
    private final String title;
    private final String file;
    private final String address;

    /**
     * Empty constructor
     * @return AnnotationTargetResource with empty variables
     */
    protected AnnotationTargetResource() {
        this.id = null;
        this.created = null;
        this.modified = null;
        this.targeter = "";
        this.type = "";
        this.title = "";
        this.file = "";
        this.address = "";
    }

    /**
     * Constructor
     * @param  target content
     */
    public AnnotationTargetResource(AnnotationTarget target) {
        this.id = target.getId();
        this.created = target.getCreated();
        this.modified = target.getModified();
        this.targeter = target.getUser().getUsername();
        this.title = target.getTitle();
        if (target.getFile() != null) {
            this.type = FILE;
            this.file = target.getFile().getName();
            this.address = null;
        } else {
            this.type = URL;
            this.file = null;
            this.address = target.getAddress();
        }
    }

    /**
     * Constructor
     * @param  target content
     * @param  links (optional) links to include
     */
    public AnnotationTargetResource(AnnotationTarget target, Link... links) {
        this(target);
        this.add(links);
    }

    /**
     * Get id
     * @return id
     */
    @JsonProperty("id")
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
     * Get targeter
     * @return username
     */
    public String getAnnotationTargeter() {
        return targeter;
    }

    /**
     * Get title
     * @return title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Get type
     * @return type
     */
    public String getType() {
        return type;
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
