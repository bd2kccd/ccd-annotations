package edu.pitt.dbmi.ccd.anno.index;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Link;

/**
 * Group entity DTO representation with self link
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
public class IndexResource extends ResourceSupport {

    private final String message;

    /**
     * generate index
     * @param  message message to display
     * @param  links   (optional) links to include
     * @return         index
     */
    public IndexResource(String message, Link... links) {
        this.message = message;
        this.add(links);
    }

    /**
     * Get message
     * @return message
     */
    public String getMessage() {
        return message;
    }
}
