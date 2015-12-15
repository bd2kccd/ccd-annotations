package edu.pitt.dbmi.ccd.anno.group;

import java.util.Collection;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import static org.springframework.data.domain.Sort.Order.*;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EmbeddedWrapper;
import org.springframework.hateoas.core.EmbeddedWrappers;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
// import edu.pitt.dbmi.ccd.anno.resourceUtil.EmbeddedResources;
import edu.pitt.dbmi.ccd.db.entity.Group;
/**
 * Page of GroupResources
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
public class GroupResourcesPage extends PagedResources<GroupResource> {

    // content (embedded)
    private final Collection<GroupResource> content = new ArrayList<GroupResource>(0);

    // links
    private final Link self;

    // page metadata
    private final PageMetadata pageMetadata;

    // sort parameters
    // private final Collection<Sort> sort = new ArrayList<Sort>(0);

    private final Sort sort;

    // private final PagedResources<GroupResource> pagedTest;

    /**
     * Generate a new page of GroupResources
     * @param  content   embedded content
     * @param  pageable  page request information
     * @param  self      self link
     * @param  links     additional links
     * @return           generated page
     */
    public GroupResourcesPage(Collection<Group> content, Pageable pageable, Link self, Link... links) {
        this.content.addAll(content.stream()
                            .map(g -> new GroupResource(g))
                            .collect(Collectors.toSet()));
        this.pageMetadata = new PageMetadata(pageable.getPageSize(), pageable.getPageNumber(), content.size());
        this.sort = pageable.getSort();
        this.self = self;

        this.wrap(content, pageMetadata);
        this.add(self);
        this.add(links);
        // this.pagedTest
    }

    /**
     * get embedded content
     * @return embedded content
     */
    @JsonInclude(Include.ALWAYS)
    @JsonProperty("_embedded")
    public Collection<GroupResource> getContent() {
        return content;
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
    public PageMetadata getMetadata() {
        return pageMetadata;
    }

    /**
     * get content sorting information
     * @return sorting
     */
    // public Sort getSort() {
    //     return sort;
    // }

    // @JsonProperty("sortProperty")
    // @JsonInclude(Include.NON_NULL)
    // public Collection<String> getSortProperty() {
    //     Collection<String> p = new ArrayList<>(0);
    //     for (Sort.Order o : sort) {
    //         p.add(o.getProperty());
    //     }
    //     if (p.size() == 0) {
    //         p.add("");
    //     }
    //     return p;
    // }

    // @JsonProperty("sortDirection")
    // @JsonInclude(Include.NON_NULL)
    // public Collection<String> getSortDirection() {
    //     Collection<String> p = new ArrayList<>(0);
    //     for (Sort.Order o : sort) {
    //         p.add(o.getDirection().toString());
    //     }
    //     if (p.size() == 0) {
    //         p.add("");
    //     }
    //     return p;
    // }
}
