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
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import edu.pitt.dbmi.ccd.anno.error.ForbiddenException;
import edu.pitt.dbmi.ccd.anno.error.NotFoundException;
import edu.pitt.dbmi.ccd.anno.error.UserNotFoundException;
import edu.pitt.dbmi.ccd.anno.group.GroupPagedResourcesAssembler;
import edu.pitt.dbmi.ccd.anno.group.GroupResource;
import edu.pitt.dbmi.ccd.anno.group.GroupResourceAssembler;
import edu.pitt.dbmi.ccd.db.entity.Group;
import edu.pitt.dbmi.ccd.db.entity.UserAccount;
import edu.pitt.dbmi.ccd.db.entity.UserRole;
import edu.pitt.dbmi.ccd.db.service.GroupService;
import edu.pitt.dbmi.ccd.db.service.UserAccountService;

/**
 * Controller for User endpoints
 *
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@RestController
@ExposesResourceFor(UserResource.class)
@RequestMapping(value = UserLinks.INDEX)
public class UserController {

    // loggers
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    // servlet
    private final HttpServletRequest request;

    // services and components
    private final UserLinks userLinks;
    private final UserAccountService accountService;
    private final GroupService groupService;
    private final UserResourceAssembler assembler;
    private final UserPagedResourcesAssembler pageAssembler;
    private final GroupResourceAssembler groupAssembler;
    private final GroupPagedResourcesAssembler groupPageAssembler;

    // dependencies
    private static final Base64.Decoder base64Decoder = Base64.getUrlDecoder();

    @Autowired(required = true)
    public UserController(
            HttpServletRequest request,
            UserLinks userLinks,
            UserAccountService accountService,
            GroupService groupService,
            UserResourceAssembler assembler,
            UserPagedResourcesAssembler pageAssembler,
            GroupResourceAssembler groupAssembler,
            GroupPagedResourcesAssembler groupPageAssembler) {
        this.request = request;
        this.userLinks = userLinks;
        this.accountService = accountService;
        this.groupService = groupService;
        this.assembler = assembler;
        this.pageAssembler = pageAssembler;
        this.groupAssembler = groupAssembler;
        this.groupPageAssembler = groupPageAssembler;
    }

    /* GET requests */

    /**
     * Get all users (if ADMIN)
     *
     * @param principal current authenticated user
     * @param pageable  page request
     * @return page of users
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResourceSupport users(@AuthenticationPrincipal UserAccount principal, Pageable pageable) {
        if (principal.getRoles().stream()
                .map(UserRole::getName)
                .anyMatch(r -> r.equalsIgnoreCase("ADMIN"))) {
            Page<UserAccount> page = accountService.findAll(pageable);
            final PagedResources<UserResource> pagedResources = pageAssembler.toResource(page, assembler, request);
            pagedResources.add(userLinks.search());
            return pagedResources;
        } else {
            ResourceSupport resource = new ResourceSupport();
            resource.add(userLinks.search());
            return resource;
        }
    }

    /**
     * Get user by account id
     *
     * @param id URL safe, base64 encoded user account id
     * @return user
     */
    @RequestMapping(value = UserLinks.USER, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResourceSupport getUser(@PathVariable String id) throws NotFoundException {
        String decoded = new String(base64Decoder.decode(id.getBytes()));
        UserAccount account = accountService.findByAccount(decoded).orElseThrow(() -> new UserNotFoundException(id));
        final UserResource resource = assembler.toResource(account);
        return resource;
    }

    /**
     * Get user's group
     *
     * @param id URL safe, base64 encoded user  accountid
     * @return groups
     */
    @RequestMapping(value = UserLinks.GROUPS, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PagedResources<GroupResource> groups(
            @AuthenticationPrincipal UserAccount principal,
            @PathVariable String id,
            @RequestParam(value = "requests", required = false, defaultValue = "false") boolean requests,
            @RequestParam(value = "moderator", required = false, defaultValue = "false") boolean moderator,
            @PageableDefault(size = 20, sort = {"name"}) Pageable pageable)
            throws NotFoundException, ForbiddenException {
        String decoded = new String(base64Decoder.decode(id.getBytes()));
        if (principal.getAccount().equals(decoded)) {
            UserAccount account = accountService.findByAccount(decoded).orElseThrow(() -> new UserNotFoundException(id));
            final Page<Group> page;
            if (requests) {
                // get groups for which user is requesting access
                page = groupService.findByRequester(account, pageable);
            } else if (moderator) {
                // get groups for which user is a moderator
                page = groupService.findByModerator(account, pageable);
            } else {
                // get all user's groups
                page = groupService.findByMember(account, pageable);
            }
            final PagedResources<GroupResource> pagedResources = groupPageAssembler.toResource(page, groupAssembler, request);
            return pagedResources;
        } else {
            throw new ForbiddenException(principal, request);
        }
    }

    /**
     * User search page
     *
     * @param email user's email address
     * @return user
     */
    @RequestMapping(value = UserLinks.SEARCH, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserResource search(@RequestParam String email) {
        UserAccount account = accountService.findByEmail(email).orElseThrow(() -> new UserNotFoundException("email", email));
        final UserResource resource = assembler.toResource(account);
        return resource;
    }
}
