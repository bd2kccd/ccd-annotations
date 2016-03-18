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

package edu.pitt.dbmi.ccd.anno.annotation.data;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.Set;
import java.util.List;
import java.util.stream.StreamSupport;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.util.Assert;
import edu.pitt.dbmi.ccd.db.entity.AnnotationData;
import edu.pitt.dbmi.ccd.anno.annotation.AnnotationController;
import edu.pitt.dbmi.ccd.anno.vocabulary.VocabularyLinks;

/**
 * Assembles AnnotationData into AnnotationDataResource
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Component
public class AnnotationDataResourceAssembler extends ResourceAssemblerSupport<AnnotationData, AnnotationDataResource> {

    private final VocabularyLinks vocabularyLinks;

    @Autowired(required=true)
    public AnnotationDataResourceAssembler(VocabularyLinks vocabularyLinks) {
        super(AnnotationController.class, AnnotationDataResource.class);
        this.vocabularyLinks = vocabularyLinks;
    }

    /**
     * convert AnnotationData to AnnotationDataResource
     * @param  annotation entity
     * @return            resource
     */
    @Override
    public AnnotationDataResource toResource(AnnotationData data) {
        AnnotationDataResource resource = createResourceWithId(data.getId(), data);
        Set<AnnotationDataResource> children = data.getChildren().stream()
                                                                 .map(this::toResource)
                                                                 .collect(Collectors.toSet());
        resource.addSubData(children);
        if (data.getAttribute() != null) {
            resource.add(vocabularyLinks.attribute(data.getAttribute().getVocabulary(), data.getAttribute()));
        }
        return resource;
    }

    /**
     * convert AnnotationDatas to AnnotationDataResources
     * @param  annotations entities
     * @return              List of resources
     */
    @Override
    public List<AnnotationDataResource> toResources(Iterable<? extends AnnotationData> annotations) {
        Assert.isTrue(annotations.iterator().hasNext());
        return StreamSupport.stream(annotations.spliterator(), false)
                            .map(this::toResource)
                            .collect(Collectors.toList());
    }

    /**
     * Creates a new annotation resource with a correct self link
     * Added vocabulary name to self link
     * 
     * @param entity must not be {@literal null}.
     * @param id     must not be {@literal null}.
     * @return       resource
     */
    @Override
    protected AnnotationDataResource createResourceWithId(Object id, AnnotationData entity, Object... parameters) {
        Assert.notNull(entity);
        Assert.notNull(id);

        AnnotationDataResource instance = instantiateResource(entity);
        instance.add(linkTo(AnnotationController.class, parameters).slash(entity.getAnnotation().getId()+"/data").slash(id).withSelfRel());
        return instance;
    }

    /**
     * Instantiate AnnotationDataResource with non-default constructor
     * @param  annotation entity
     * @return            resource
     */
    @Override
    protected AnnotationDataResource instantiateResource(AnnotationData annotation) {
        Assert.notNull(annotation);
        try {
            return BeanUtils.instantiateClass(AnnotationDataResource.class.getConstructor(AnnotationData.class), annotation);
        } catch(NoSuchMethodException ex) {
            ex.printStackTrace();
            return new AnnotationDataResource();
        }
    }
}
