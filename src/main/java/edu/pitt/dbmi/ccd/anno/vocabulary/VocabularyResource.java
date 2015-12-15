package edu.pitt.dbmi.ccd.anno.vocabulary;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.core.Relation;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.pitt.dbmi.ccd.db.entity.Vocabulary;
import edu.pitt.dbmi.ccd.db.entity.Attribute;

/**
 * Vocabulary DTO with self link
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Relation(value="vocabulary", collectionRelation="vocabularies")
public class VocabularyResource extends ResourceSupport {

    // content
    private final String name;
    private final String description;

    // links
    private final Link self;
    private final Link attributes;

    /**
     * Generate new VocabularyResource with self link
     * @param  vocab content
     * @param  links links to include (optional)
     * @return       generated VocabularyResource
     */
    public VocabularyResource(Vocabulary vocab, Link... links) {
        this.name = vocab.getName();
        this.description = vocab.getDescription();
        this.self = linkTo(methodOn(VocabularyController.class).getVocabulary(name)).withSelfRel();
        this.attributes = linkTo(methodOn(VocabularyController.class).getAttributes(name)).withRel("attributes");
        this.add(self, attributes);
        this.add(links);
    }

    /* content */

    /**
     * Get vocabulary name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get vocabulary description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /* links */

    /**
     * Get vocabulary link
     * @return link
     */
    @JsonIgnore
    public Link getLink() {
        return self;
    }

    /**
     * Get attributes link
     * @return link to attributes
     */
    @JsonIgnore
    public Link getAttributes() {
        return attributes;
    }
}
