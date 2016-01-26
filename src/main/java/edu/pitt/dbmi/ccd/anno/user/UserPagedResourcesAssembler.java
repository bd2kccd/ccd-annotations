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

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceAssembler;
import edu.pitt.dbmi.ccd.db.entity.UserAccount;

/**
 * Assembles page of UserResources
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Component
public class UserPagedResourcesAssembler extends PagedResourcesAssembler<UserAccount> {

    private final UserLinks userLinks;

    /**
     * Create new PagedResourcesAssembler for UserAccount entity
     * @return UserPagedResourcesAssembler
     */
    @Autowired(required=true)
    public UserPagedResourcesAssembler(UserLinks userLinks) {
        super(null, null);
        this.userLinks = userLinks;
    }

    /**
     * Create PagedResources of user resources
     * @param  page      page of entities
     * @param  assembler resource assembler
     * @param  request   request data
     * @return           PagedResources of user resources
     */
    public PagedResources<UserResource> toResource(Page<UserAccount> page, ResourceAssembler<UserAccount, UserResource> assembler, HttpServletRequest request) {
        final Link self = userLinks.getRequestLink(request);
        return this.toResource(page, assembler, self);
    }
}
