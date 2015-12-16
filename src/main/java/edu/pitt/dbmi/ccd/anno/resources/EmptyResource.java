package edu.pitt.dbmi.ccd.anno.resources;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Link;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * Empty resource for use on pages with no content (just links)
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
public class EmptyResource extends ResourceSupport {
    
    public EmptyResource(Link... links) {
        this.add(links);
    }
}