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

package edu.pitt.dbmi.ccd.anno.metadata;

import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.util.Assert;
import edu.pitt.dbmi.ccd.db.entity.Annotation;
import edu.pitt.dbmi.ccd.db.entity.AnnotationData;
import edu.pitt.dbmi.ccd.anno.vocabulary.attribute.AttributeLinks;
import edu.pitt.dbmi.ccd.anno.data.UploadLinks;
import edu.pitt.dbmi.ccd.anno.user.UserLinks;
import edu.pitt.dbmi.ccd.anno.group.GroupLinks;
import edu.pitt.dbmi.ccd.anno.vocabulary.VocabularyLinks;

/**
 * Assembles Annotation + AnnotationData into AnnotationResource
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Component
public class AnnotationResourceAssembler extends ResourceAssemblerSupport<Annotation, AnnotationResource> {

    private final AnnotationLinks annotationLinks;
    private final AttributeLinks attributeLinks;
    private final UploadLinks uploadLinks;
    private final UserLinks userLinks;
    private final GroupLinks groupLinks;
    private final VocabularyLinks vocabularyLinks;

    @Autowired(required=true)
    public AnnotationResourceAssembler(AnnotationLinks annotationLinks, AttributeLinks attributeLinks, UploadLinks uploadLinks, UserLinks userLinks, GroupLinks groupLinks, VocabularyLinks vocabularyLinks) {
        super(AnnotationController.class, AnnotationResource.class);
        this.annotationLinks = annotationLinks;
        this.attributeLinks = attributeLinks;
        this.uploadLinks = uploadLinks;
        this.userLinks = userLinks;
        this.groupLinks = groupLinks;
        this.vocabularyLinks = vocabularyLinks;
    }

    /**
     * Convert Annotation to AnnotationResource
     * @param  annotation entity
     * @return            resource
     */
    @Override
    public AnnotationResource toResource(Annotation annotation) {
        Assert.notNull(annotation);
        AnnotationResource resource = createResourceWithId(annotation.getId(), annotation);
        Set<AnnotationDataResource> data = annotation.getData().stream()
                                                .filter(d -> d.getParent() == null)
                                                .map(this::toDataResource)
                                                .collect(Collectors.toSet());
        resource.addData(data);
        resource.add(annotationLinks.children(annotation));
        resource.add(uploadLinks.upload(annotation.getTarget()));
        resource.add(userLinks.user(annotation.getUser()));
        if (annotation.getGroup() != null) {
            resource.add(groupLinks.group(annotation.getGroup()));
        }
        resource.add(vocabularyLinks.vocabulary(annotation.getVocabulary()));
        if (annotation.getParent() != null) {
            resource.add(annotationLinks.parent(annotation));
        }
        return resource;
    }

    /**
     * Convert AnnotationData to AnnotationDataResource
     * @param  data entity
     * @return      resource
     */
    public AnnotationDataResource toDataResource(AnnotationData data) {
        AnnotationDataResource resource = new AnnotationDataResource(data);
        Set<AnnotationDataResource> children = data.getChildren().stream()
                                                                 .map(this::toDataResource)
                                                                 .collect(Collectors.toSet());
        resource.addChildren(children);
        resource.add(annotationLinks.annotationDataSelf(data));
        if (data.getAttribute() != null) {
            resource.add(attributeLinks.attribute(data.getAttribute()));
        }
        return resource;
    }

    /**
     * convert Annotations to AnnotationResources
     * @param  annotations entities
     * @return             list of resources
     */
    @Override
    public List<AnnotationResource> toResources(Iterable<? extends Annotation> annotations) {
        Assert.isTrue(annotations.iterator().hasNext());
        return StreamSupport.stream(annotations.spliterator(), false)
                                .map(this::toResource)
                                .collect(Collectors.toList());
    }

    /**
     * Instantiate AnnotationResource with non-default constructor
     * @param  annotation entity
     * @return            resource
     */
    @Override
    protected AnnotationResource instantiateResource(Annotation annotation) {
        Assert.notNull(annotation);
        try {
            return BeanUtils.instantiateClass(AnnotationResource.class.getConstructor(Annotation.class), annotation);
        } catch(NoSuchMethodException nsme) {
            nsme.printStackTrace();
            return new AnnotationResource();
        }
    }
}
