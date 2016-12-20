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
package edu.pitt.dbmi.ccd.annotations.index;

import javax.servlet.http.HttpServletRequest;

import edu.pitt.dbmi.ccd.annotations.access.AccessLinks;
import edu.pitt.dbmi.ccd.annotations.annotation.AnnotationLinks;
import edu.pitt.dbmi.ccd.annotations.data.AnnotationTargetLinks;
import edu.pitt.dbmi.ccd.annotations.group.GroupLinks;
import edu.pitt.dbmi.ccd.annotations.user.UserLinks;
import edu.pitt.dbmi.ccd.annotations.vocabulary.VocabularyLinks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public final class IndexResourceAssembler {

    // Actuator endpoint refs
    private static final String HEALTH = "health";
    private static final String INFO = "info";
    private static final String METRICS = "metrics";
    private static final String TRACE = "trace";

    // Message to display
    private final String message;

    // components
    private final AccessLinks accessLinks;
    private final AnnotationLinks annotationLinks;
    private final AnnotationTargetLinks annotationTargetLinks;
    private final GroupLinks groupLinks;
    private final UserLinks userLinks;
    private final VocabularyLinks vocabularyLinks;

    @Autowired(required = true)
    public IndexResourceAssembler(
            @Value("${info.app.description ?: CCD Annotations}") String description,
            @Value("${info.app.version ?: }") String version,
            AccessLinks accessLinks,
            AnnotationLinks annotationLinks,
            AnnotationTargetLinks annotationTargetLinks,
            GroupLinks groupLinks,
            UserLinks userLinks,
            VocabularyLinks vocabularyLinks) {
        this.message = buildMessage(description, version);
        this.accessLinks = accessLinks;
        this.annotationLinks = annotationLinks;
        this.annotationTargetLinks = annotationTargetLinks;
        this.groupLinks = groupLinks;
        this.userLinks = userLinks;
        this.vocabularyLinks = vocabularyLinks;
    }

    /**
     * Build index
     *
     * @return index
     */
    public IndexResource buildIndex(HttpServletRequest request) {
        final IndexResource resource = new IndexResource(
                message,
                accessLinks.accesses(),
                annotationLinks.annotations(),
                annotationTargetLinks.targets(),
                groupLinks.groups(),
                healthLink(request),
                infoLink(request),
                metricsLink(request),
                userLinks.users(),
                vocabularyLinks.vocabularies()
        );
        return resource;
    }

    /**
     *
     * @param description description of ccd-annotations
     * @param version ccd-annotations version (vX.Y.Z)
     * @return welcome message
     */
    private String buildMessage(final String description, final String version) {
        return description + " " + version;
    }

    /**
     *
     * @param request http request
     * @param ref actuator endpoint hateoas reference
     * @return link to resource
     */
    private Link actuatorLink(HttpServletRequest request, String ref) {
        return new Link(request.getRequestURL().append(ref).toString(), ref);
    }

    /**
     *
     * @param request http request
     * @return link to actuator health endpoint
     */
    private Link healthLink(HttpServletRequest request) {
        return actuatorLink(request, HEALTH);
    }

    /**
     *
     * @param request http request
     * @return link to actuator info endpoint
     */
    private Link infoLink(HttpServletRequest request) {
        return actuatorLink(request, INFO);
    }

    /**
     *
     * @param request http request
     * @return link to actuator metrics endpoint
     */
    private Link metricsLink(HttpServletRequest request) {
        return actuatorLink(request, METRICS);
    }

    /**
     *
     * @param request http request
     * @return link to actuator trace endpoint
     */
    private Link traceLink(HttpServletRequest request) {
        return actuatorLink(request, TRACE);
    }
}
