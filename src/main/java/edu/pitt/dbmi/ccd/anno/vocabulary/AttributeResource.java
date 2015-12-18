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

package edu.pitt.dbmi.ccd.anno.vocabulary;

import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import org.springframework.hateoas.core.Relation;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.pitt.dbmi.ccd.db.entity.Vocabulary;
import edu.pitt.dbmi.ccd.db.entity.Attribute;

/**
 * Attribute DTO with self link
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Relation(value="attribute", collectionRelation="attributes")
@JsonPropertyOrder({"id", "level", "name", "requirementLevel"})
public final class AttributeResource extends ResourceSupport {

    // content
    private final Long innerId;
    private final String level;
    private final String name;
    private final String requirementLevel;
    private final Set<AttributeResource> children = new HashSet<>(0);

    // links
    // private final Link vocabulary;
    // private final Link parent;

    /**
     * Empty constructor
     * @return new AttributeResource with empty variables
     */
    protected AttributeResource() {
        this.innerId = null;
        this.level = "";
        this.name = "";
        this.requirementLevel = "";
        // vocabulary = null;
        // parent = null;
    }

    /**
     * Constructor
     * @param  attribute content
     * @return  new AttributeResource
     */
    public AttributeResource(Attribute attribute) {
        this.innerId = attribute.getInnerId();
        this.level = attribute.getLevel();
        this.name = attribute.getName();
        this.requirementLevel = attribute.getRequirementLevel();
        this.children.addAll(attribute.getChildren().stream()
                                            .map(a -> new AttributeResource(a))
                                            .collect(Collectors.toSet()));

        // if (attribute.getVocabulary() != null) {
        //     this.vocabulary = linkTo(methodOn(VocabularyController.class).getVocabulary(attribute.getVocabulary().getName())).withRel("vocabulary");
        //     this.add(vocabulary);
        // } else {
        //     vocabulary = null;
        // }

        // if (attribute.getParent() != null) {
        //     final long parentId = attribute.getParent().getInnerId();
        //     this.parent = linkTo(methodOn(VocabularyController.class).getAttribute(attribute.getVocabulary().getName(), parentId)).withRel("parent");
        //     this.add(parent);
        // } else {
        //     parent = null;
        // }

    }
    
    /**
     * Constructor
     * @param  attribute content
     * @param  links (optional) links to include
     * @return       new AttributeResource
     */
    public AttributeResource(Attribute attribute, Link... links) {
        this(attribute);
        this.add(links);
    }

    /* content */

    /**
     * get attribute id
     * @return id
     */
    @JsonProperty("id")
    public Long getInnerId() {
        return innerId;
    }

    /**
     * get attribute level
     * @return level
     */
    public String getLevel() {
        return level;
    }

    /**
     * get attribute name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * get attribute requirement level
     * @return requirement level
     */
    public String getRequirementLevel() {
        return requirementLevel;
    }

    /**
     * get child attributes
     * @return child attributes
     */
    public Set<AttributeResource> getChildren() {
        return children;
    }

    /* links */

    /**
     * Get attribute link
     * @return link
     */
    // @JsonIgnore
    // public Link getLink() {
    //     return self;
    // }

    // /**
    //  * Get vocabulary link
    //  * @return link to vocabulary
    //  */
    // @JsonIgnore
    // public Link getVocabulary() {
    //     return vocabulary;
    // }

    // /**
    //  * Get parent attribute link
    //  * @return link to parent attribute
    //  */
    // @JsonIgnore
    // public Link getParent() {
    //     return parent;
    // }
}
