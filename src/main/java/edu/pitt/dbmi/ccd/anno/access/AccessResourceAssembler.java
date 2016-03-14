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

package edu.pitt.dbmi.ccd.anno.access;

import java.util.List;
import java.util.stream.StreamSupport;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.util.Assert;
import edu.pitt.dbmi.ccd.db.entity.Access;

/**
 * Assembles Access + AccessData into AccessResource
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Component
public class AccessResourceAssembler extends ResourceAssemblerSupport<Access, AccessResource> {

    private final AccessLinks accessLinks;

    @Autowired(required=true)
    public AccessResourceAssembler(AccessLinks accessLinks) {
        super(AccessController.class, AccessResource.class);
        this.accessLinks = accessLinks;
    }

    /**
     * Convert Access to AccessResource
     * @param  access entity
     * @return        resource
     */
    @Override
    public AccessResource toResource(Access access) {
        Assert.notNull(access);
        AccessResource resource = createResourceWithId(access.getName(), access);
        return resource;
    }

    /**
     * convert Accesses to AccessResources
     * @param  accesses entities
     * @return             list of resources
     */
    @Override
    public List<AccessResource> toResources(Iterable<? extends Access> accesses) {
        Assert.isTrue(accesses.iterator().hasNext());
        return StreamSupport.stream(accesses.spliterator(), false)
                                .map(this::toResource)
                                .collect(Collectors.toList());
    }

    /**
     * Instantiate AccessResource with non-default constructor
     * @param  access entity
     * @return            resource
     */
    @Override
    protected AccessResource instantiateResource(Access access) {
        Assert.notNull(access);
        try {
            return BeanUtils.instantiateClass(AccessResource.class.getConstructor(Access.class), access);
        } catch(NoSuchMethodException nsme) {
            nsme.printStackTrace();
            return new AccessResource();
        }
    }
}
