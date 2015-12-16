package edu.pitt.dbmi.ccd.anno.resources;

import java.util.Collection;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.core.EmbeddedWrapper;
import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.hateoas.Link;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * Page with embedded resources
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
public class EmbeddedResourcePage<R extends ResourceSupport> extends PagedResources<R> {
    
    private static final EmbeddedWrappers wrapper = new EmbeddedWrappers(true);
    
    // content (embedded)
    protected Resources<EmbeddedWrapper> embedded;

    // links
    private final Link self;

    // page metadata
    private final PageMetadata pageMetadata;

    /**
     * create new ResourcePage
     * @param  content  embedded content
     * @param  pageable page request information
     * @param  self     self link
     * @param  links    (optional) additional links
     * @return          generated ResourcePage
     */
    public EmbeddedResourcePage(Collection<R> resources, PageMetadata pageMetadata, Link self, Link... links) {
        this.embed(resources);
        this.pageMetadata = pageMetadata;
        this.self = self;
        this.add(self);
        this.add(links);
    }

    /**
     * get embedded content
     * @return embedded content
     */
    @JsonProperty("_embedded")
    @JsonInclude(Include.ALWAYS)
    @JsonUnwrapped
    public Resources<EmbeddedWrapper> getEmbedded() {
        return embedded;
    }

    public void embed(Collection<R> resources) {
        embedded = new Resources<EmbeddedWrapper>(resources.stream()
                                                            .map(r -> wrapper.wrap(r))
                                                            .collect(Collectors.toList()));
    }

    /**
     * get page link
     * @return self link
     */
    @JsonIgnore
    public Link getLink() {
        return self;
    }

    /**
     * get page metadata
     * @return page metadata
     */
    @JsonProperty("page")
    public PageMetadata getPageMetadata() {
        return pageMetadata;
    }
}