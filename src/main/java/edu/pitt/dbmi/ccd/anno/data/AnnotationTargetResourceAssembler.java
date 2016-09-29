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

package edu.pitt.dbmi.ccd.anno.data;

import java.util.List;
import java.util.stream.StreamSupport;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.util.Assert;
import edu.pitt.dbmi.ccd.db.entity.AnnotationTarget;
import edu.pitt.dbmi.ccd.anno.user.UserLinks;

/**
 * Assembles AnnotationTarget into AnnotationTargetResource
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Component
public class AnnotationTargetResourceAssembler extends ResourceAssemblerSupport<AnnotationTarget, AnnotationTargetResource> {

    private final AnnotationTargetLinks annotationTargetLinks;
    private final UserLinks userLinks;

    @Autowired(required=true)
    public AnnotationTargetResourceAssembler(AnnotationTargetLinks annotationTargetLinks, UserLinks userLinks) {
        super(AnnotationTargetController.class, AnnotationTargetResource.class);
        this.annotationTargetLinks = annotationTargetLinks;
        this.userLinks = userLinks;
    }

    /**
     * convert AnnotationTarget to AnnotationTargetResource
     * @param  target entity
     * @return        resource
     */
    @Override
    public AnnotationTargetResource toResource(AnnotationTarget target) throws IllegalArgumentException {
        Assert.notNull(target);
        AnnotationTargetResource resource = createResourceWithId(target.getId(), target);
        resource.add(userLinks.user(target.getUser()));
        resource.add(annotationTargetLinks.annotations(target));
        return resource;
    }

    /**
     * convert AnnotationTargets to AnnotationTargetResources
     * @param  targets entities
     * @return        List of resources
     */
    @Override
    public List<AnnotationTargetResource> toResources(Iterable<? extends AnnotationTarget> targets) throws IllegalArgumentException {
        // Assert targets is not empty
        Assert.isTrue(targets.iterator().hasNext());
        return StreamSupport.stream(targets.spliterator(), false)
                            .map(this::toResource)
                            .collect(Collectors.toList());
    }

    /**
     * Instantiate AnnotationTargetResource with non-default constructor
     * @param  target entity
     * @return        resource
     */
    @Override
    protected AnnotationTargetResource instantiateResource(AnnotationTarget target) throws IllegalArgumentException {
        Assert.notNull(target);
        try {
            return BeanUtils.instantiateClass(AnnotationTargetResource.class.getConstructor(AnnotationTarget.class), target);
        } catch(NoSuchMethodException ex) {
            ex.printStackTrace();
            return new AnnotationTargetResource();
        }
    }
}
