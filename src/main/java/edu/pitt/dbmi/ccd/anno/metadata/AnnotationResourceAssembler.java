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
import java.util.stream.StreamSupport;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.util.Assert;
import edu.pitt.dbmi.ccd.db.entity.Annotation;

/**
 * Assembles Annotation + AnnotationData into AnnotationResource
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Component
public class AnnotationResourceAssembler extends ResourceAssemblerSupport<Annotation, AnnotationResource> {

    public AnnotationResourceAssembler() {
        super(AnnotationController.class, AnnotationResource.class);
    }

    /**
     * convert Annotation to AnnotationResource
     * @param  annotation entity
     * @return            resource
     */
    @Override
    public AnnotationResource toResource(Annotation annotation) {
        Assert.notNull(annotation);
        AnnotationResource resource = createResourceWithId(annotation.getId(), annotation);
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
