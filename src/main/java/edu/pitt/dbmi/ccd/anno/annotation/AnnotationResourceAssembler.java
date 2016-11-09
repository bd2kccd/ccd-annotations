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
package edu.pitt.dbmi.ccd.anno.annotation;

import edu.pitt.dbmi.ccd.anno.annotation.data.AnnotationDataResource;
import edu.pitt.dbmi.ccd.anno.annotation.data.AnnotationDataResourceAssembler;
import edu.pitt.dbmi.ccd.anno.data.AnnotationTargetLinks;
import edu.pitt.dbmi.ccd.anno.group.GroupLinks;
import edu.pitt.dbmi.ccd.anno.user.UserLinks;
import edu.pitt.dbmi.ccd.anno.vocabulary.VocabularyLinks;
import edu.pitt.dbmi.ccd.anno.vocabulary.VocabularyResourceAssembler;
import edu.pitt.dbmi.ccd.anno.vocabulary.attribute.AttributeLinks;
import edu.pitt.dbmi.ccd.db.entity.Annotation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Assembles Annotation + AnnotationData into AnnotationResource
 *
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Component
public class AnnotationResourceAssembler extends ResourceAssemblerSupport<Annotation, AnnotationResource> {

    @Autowired
    AnnotationDataResourceAssembler dataAssembler;

    private final AnnotationLinks annotationLinks;
    private final AttributeLinks attributeLinks;
    private final AnnotationTargetLinks annotationTargetLinks;
    private final UserLinks userLinks;
    private final GroupLinks groupLinks;
    private final VocabularyLinks vocabularyLinks;
    private final VocabularyResourceAssembler vocabularyResourceAssembler;

    @Autowired(required = true)
    public AnnotationResourceAssembler(AnnotationLinks annotationLinks, AttributeLinks attributeLinks, AnnotationTargetLinks annotationTargetLinks, UserLinks userLinks, GroupLinks groupLinks, VocabularyLinks vocabularyLinks, VocabularyResourceAssembler vocabularyResourceAssembler) {
        super(AnnotationController.class, AnnotationResource.class);
        this.annotationLinks = annotationLinks;
        this.attributeLinks = attributeLinks;
        this.annotationTargetLinks = annotationTargetLinks;
        this.userLinks = userLinks;
        this.groupLinks = groupLinks;
        this.vocabularyLinks = vocabularyLinks;
        this.vocabularyResourceAssembler = vocabularyResourceAssembler;
    }

    /**
     * Convert Annotation to AnnotationResource
     *
     * @param annotation entity
     * @return resource
     */
    @Override
    public AnnotationResource toResource(Annotation annotation) throws IllegalArgumentException {
        Assert.notNull(annotation);
        AnnotationResource resource = createResourceWithId(annotation.getId(), annotation);
        Set<AnnotationDataResource> data = annotation.getData()
                .stream()
                .filter(d -> d.getParent() == null)
                .map(dataAssembler::toResource)
                .collect(Collectors.toSet());
        resource.addData(data);
        if (annotation.getChildren().size() > 0) {
            resource.add(annotationLinks.children(annotation));
        }
        resource.add(annotationTargetLinks.target(annotation.getTarget()));
        if (annotation.getUser().getAccountId() != null) {
            resource.add(userLinks.user(annotation.getUser()));
        }
        if (annotation.getGroup() != null) {
            resource.add(groupLinks.group(annotation.getGroup()));
        }
        resource.add(vocabularyLinks.vocabulary(annotation.getVocabulary()));
        if (annotation.getParent() != null) {
            resource.add(annotationLinks.parent(annotation));
        }
        resource.setVocabularyResource(vocabularyResourceAssembler.toResource(annotation.getVocabulary()));
        return resource;
    }

    /**
     * convert Annotations to AnnotationResources
     *
     * @param annotations entities
     * @return list of resources
     */
    @Override
    public List<AnnotationResource> toResources(Iterable<? extends Annotation> annotations) throws IllegalArgumentException {
        // Assert annotations is not empty
        Assert.isTrue(annotations.iterator().hasNext());
        return StreamSupport.stream(annotations.spliterator(), false)
                .map(this::toResource)
                .collect(Collectors.toList());
    }

    /**
     * Instantiate AnnotationResource with non-default constructor
     *
     * @param annotation entity
     * @return resource
     */
    @Override
    protected AnnotationResource instantiateResource(Annotation annotation) throws IllegalArgumentException {
        Assert.notNull(annotation);
        try {
            return BeanUtils.instantiateClass(AnnotationResource.class.getConstructor(Annotation.class), annotation);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
            return new AnnotationResource();
        }
    }
}
