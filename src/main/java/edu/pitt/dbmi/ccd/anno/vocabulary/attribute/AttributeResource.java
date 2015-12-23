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

package edu.pitt.dbmi.ccd.anno.vocabulary.attribute;

import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import org.springframework.hateoas.core.Relation;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Link;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import edu.pitt.dbmi.ccd.db.entity.Attribute;

/**
 * Attribute entity DTO representation
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

    /**
     * Empty constructor
     * @return new AttributeResource with empty variables
     */
    protected AttributeResource() {
        this.innerId = null;
        this.level = "";
        this.name = "";
        this.requirementLevel = "";
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
                                            .map(AttributeResource::new)
                                            .collect(Collectors.toSet()));
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

    /**
     * get attribute id relative to vocabulary
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
    @JsonUnwrapped
    public Set<AttributeResource> getChildren() {
        return children;
    }
}
