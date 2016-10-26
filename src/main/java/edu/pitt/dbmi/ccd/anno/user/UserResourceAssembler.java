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

package edu.pitt.dbmi.ccd.anno.user;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import edu.pitt.dbmi.ccd.db.entity.UserAccount;

/**
 * Assembles UserAccount + Person into UserResource
 *
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Component
public class UserResourceAssembler extends ResourceAssemblerSupport<UserAccount, UserResource> {

    private final UserLinks userLinks;
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    @Autowired(required = true)
    public UserResourceAssembler(UserLinks userLinks) {
        super(UserController.class, UserResource.class);
        this.userLinks = userLinks;
    }

    /**
     * Convert UserAccount + Person to UserResource
     *
     * @param account entity
     * @return resource
     */
    @Override
    public UserResource toResource(UserAccount account) throws IllegalArgumentException {
        Assert.notNull(account);
        final String encoded = base64Encoder.encodeToString(account.getAccountId().getBytes());
        UserResource resource = createResourceWithId(encoded, account);
        resource.add(userLinks.annotations(account));
        resource.add(userLinks.uploads(account));
        resource.add(userLinks.groups(account));
        return resource;
    }

    /**
     * Convert UserAccounts + Persons to UserResources
     *
     * @param accounts entities
     * @return List of resources
     */
    @Override
    public List<UserResource> toResources(Iterable<? extends UserAccount> accounts) throws IllegalArgumentException {
        // Assert accounts is not empty
        Assert.isTrue(accounts.iterator().hasNext());
        return StreamSupport.stream(accounts.spliterator(), false)
                .map(this::toResource)
                .collect(Collectors.toList());
    }

    /**
     * Instantiate UserResource with no-default constructor
     *
     * @param account entity
     * @return resource
     */
    @Override
    protected UserResource instantiateResource(UserAccount account) throws IllegalArgumentException {
        Assert.notNull(account);
        try {
            return BeanUtils.instantiateClass(UserResource.class.getConstructor(UserAccount.class), account);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
            return new UserResource();
        }
    }
}
