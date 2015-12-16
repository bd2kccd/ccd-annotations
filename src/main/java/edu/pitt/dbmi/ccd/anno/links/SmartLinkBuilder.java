package edu.pitt.dbmi.ccd.anno.links;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;

public class SmartLinkBuilder {

    public SmartLinkBuilder() { }

    public Link fromRequest(HttpServletRequest request, String rel) {
        final String url = request.getRequestURL().toString();
        final String query = request.getQueryString();
        if (query != null) {
            return new Link(String.format("%s?%s", url, query)).withRel(rel);
        } else {
            return new Link(url).withRel(rel);
        }
    }

    public Link templatedLink(Link link, String param, String... params) {
        String rel = link.getRel();
        StringBuilder template = new StringBuilder();
        template.append(link.getHref());
        template.append("{?");
        template.append(param);
        for (String p : params) {
            template.append(String.format(",%s", p));
        }
        template.append("}");
        return new Link(template.toString(), rel);
    }

    public Link templatedLinkPageable(Link link, String param, String... params) {
        String rel = link.getRel();
        StringBuilder template = new StringBuilder();
        template.append(link.getHref());
        template.append("{?");
        template.append(param);
        for (String p : params) {
            template.append(String.format(",%s", p));
        }
        template.append(String.format(",%s,%s,%s", "page", "size", "sort"));
        template.append("}");
        return new Link(template.toString(), rel);
    }
}