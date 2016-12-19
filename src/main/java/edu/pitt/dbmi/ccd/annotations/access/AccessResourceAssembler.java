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
package edu.pitt.dbmi.ccd.annotations.access;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import edu.pitt.dbmi.ccd.db.entity.Access;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Assembles Access into AccessResource
 *
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Component
public class AccessResourceAssembler extends ResourceAssemblerSupport<Access, AccessResource> {

    private final AccessLinks accessLinks;

    @Autowired(required = true)
    public AccessResourceAssembler(AccessLinks accessLinks) {
        super(AccessController.class, AccessResource.class);
        this.accessLinks = accessLinks;
    }

    /**
     * Convert Access to AccessResource
     *
     * @param access entity
     * @return resource
     */
    @Override
    public AccessResource toResource(Access access) throws IllegalArgumentException {
        Assert.notNull(access);
        AccessResource resource = createResourceWithId(access.getId(), access);
        return resource;
    }

    /**
     * Convert Accesses to AccessResources
     *
     * @param accesses entities
     * @return list of resources
     */
    @Override
    public List<AccessResource> toResources(Iterable<? extends Access> accesses) throws IllegalArgumentException {
        // Assert accesses is not empty
        Assert.isTrue(accesses.iterator().hasNext());
        return StreamSupport.stream(accesses.spliterator(), false)
                .map(this::toResource)
                .collect(Collectors.toList());
    }

    /**
     * Instantiate AccessResource with non-default constructor
     *
     * @param access entity
     * @return resource
     */
    @Override
    protected AccessResource instantiateResource(Access access) throws IllegalArgumentException {
        Assert.notNull(access);
        try {
            return BeanUtils.instantiateClass(AccessResource.class.getConstructor(Access.class), access);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
            return new AccessResource();
        }
    }
}
