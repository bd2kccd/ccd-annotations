package edu.pitt.dbmi.ccd.anno.vocabulary;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Stream;
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
@JsonPropertyOrder({"id", "requirementLevel", "level", "name"})
public class AttributeResource extends ResourceSupport {

    // content
    private final Long innerId;
    private final String level;
    private final String name;
    private final String requirementLevel;
    private final Collection<AttributeResource> children = new HashSet<>(0);

    // links
    private final Link self;
    private final Link vocabulary;
    private final Link parent;

    /**
     * Generate new AttributeResource with self link
     * @param  attribute content
     * @param  links links to include (optional)
     * @return       generated AttributeResource
     */
    public AttributeResource(Attribute attribute, Link... links) {
        this.innerId = attribute.getInnerId();
        this.level = attribute.getLevel();
        this.name = attribute.getName();
        this.requirementLevel = attribute.getRequirementLevel();
        this.children.addAll(attribute.getChildren().stream()
                            .map(a -> new AttributeResource(a))
                            .collect(Collectors.toSet()));
        this.self = linkTo(methodOn(VocabularyController.class).getAttribute(attribute.getVocabulary().getName(), innerId)).withSelfRel();
        this.add(self);
        this.add(links);

        if (attribute.getVocabulary() != null) {
            this.vocabulary = linkTo(methodOn(VocabularyController.class).getVocabulary(attribute.getVocabulary().getName())).withRel("vocabulary");
            this.add(vocabulary);
        } else {
            vocabulary = null;
        }

        if (attribute.getParent() != null) {
            final long parentId = attribute.getParent().getInnerId();
            this.parent = linkTo(methodOn(VocabularyController.class).getAttribute(attribute.getVocabulary().getName(), parentId)).withRel("parent");
            this.add(parent);
        } else {
            parent = null;
        }
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

    public Collection<AttributeResource> getChildren() {
        return children;
    }

    /* links */

    /**
     * Get attribute link
     * @return link
     */
    @JsonIgnore
    public Link getLink() {
        return self;
    }

    /**
     * Get vocabulary link
     * @return link to vocabulary
     */
    @JsonIgnore
    public Link getVocabulary() {
        return vocabulary;
    }

    /**
     * Get parent attribute link
     * @return link to parent attribute
     */
    @JsonIgnore
    public Link getParent() {
        return parent;
    }
}
