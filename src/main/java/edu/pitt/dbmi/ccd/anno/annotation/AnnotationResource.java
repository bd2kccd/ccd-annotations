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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import edu.pitt.dbmi.ccd.anno.annotation.data.AnnotationDataResource;
import edu.pitt.dbmi.ccd.anno.vocabulary.VocabularyResource;
import edu.pitt.dbmi.ccd.db.entity.Annotation;

/**
 * Annotation entity DTO representation
 *
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Relation(value="annotation", collectionRelation="annotations")
@JsonPropertyOrder({"id", "created", "modified", "redacted", "upload", "user", "access", "group", "vocabulary", "data"})
public final class AnnotationResource extends ResourceSupport {

    // content
    private Long id;
    private Date created;
    private Date modified;
    private boolean redacted;
    private Long upload;
    private String user;
    private String access;
    private String group;
    private VocabularyResource vocabulary;
    private Set<AnnotationDataResource> data = new HashSet<>(0);

    /**
     * Empty constructor
     * @return AnnotationResource with empty variables
     */
    protected AnnotationResource() {
        this.id = null;
        this.created = null;
        this.modified = null;
        this.redacted = false;
        this.upload = null;
        this.user = "";
        this.access = "";
        this.group = "";
        this.vocabulary = null;
    }

    /**
     * Constructor
     * @param  annotation content
     */
    public AnnotationResource(Annotation annotation) {
        this.id = annotation.getId();
        this.created = annotation.getCreated();
        this.modified = annotation.getModified();
        this.redacted = annotation.isRedacted();
        this.upload = annotation.getTarget().getId();
        this.user = annotation.getUser().getUsername();
        this.access = annotation.getAccess().getName();
        this.group = (annotation.getGroup() != null) ? annotation.getGroup().getName()
                                                     : null;
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
     * Get annotation id
     * @return annotation id
     */
    @JsonProperty("id")
    public Long getIdentifier() {
        return id;
    }

    /** Set annotation id
     * @param id annotation id
     */
    public void setIdentifier(Long id) {
        this.id = id;
    }

    /**
     * Get created date
     * @return created date
     */
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * Get modified date
     * @return modified date
     */
    public Date getModified() {
        return modified;
    }

    /**
     * Show redaction
     * @return redacted
     */
    @JsonInclude(Include.NON_DEFAULT)
    public boolean getRedacted() {
        return redacted;
    }

    /**
     * Get targeted upload
     * @return upload
     */
    public Long getUpload() {
        return upload;
    }

    /**
     * Get user
     * @return username
     */
    public String getUser() {
        return user;
    }

    /**
     * Get access control level
     * @return access control
     */
    public String getAccess() {
        return access;
    }

    /**
     * Get group
     * @return group name
     */
    @JsonInclude(Include.NON_EMPTY)
    public String getGroup() {
        return group;
    }

    /**
     * Get vocabulary
     * @return vocabulary name
     */
    public VocabularyResource getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(VocabularyResource vocabulary) {
        this.vocabulary = vocabulary;
    }

    /**
     * Get annotation data
     * @return annotation data
     */
    public Set<AnnotationDataResource> getData() {
        return data;
    }

    /**
     * Add annotation data resource
     * @param data annotation data resource
     */
    public void addData(AnnotationDataResource data) {
        this.data.add(data);
    }

    /**
     * Add multiple annotation data resources
     * @param data annotation data resources
     */
    public void addData(AnnotationDataResource... data) {
        for (AnnotationDataResource d : data) {
            addData(d);
        }
    }

    /**
     * Add multiple annotation data resources
     * @param data annotation data resources
     */
    public void addData(Set<AnnotationDataResource> data) {
        this.data.addAll(data);
    }
}
