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

import java.util.Optional;
import java.util.List;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Link;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import edu.pitt.dbmi.ccd.db.entity.UserAccount;
import edu.pitt.dbmi.ccd.db.entity.Person;
import edu.pitt.dbmi.ccd.db.entity.UserRole;
import edu.pitt.dbmi.ccd.db.service.UserAccountService;
import edu.pitt.dbmi.ccd.db.service.PersonService;

// logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for User endpoints
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@RestController
@ExposesResourceFor(UserResource.class)
@RequestMapping(value=UserLinks.INDEX)
public class UserController {
    
    // loggers
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    // servlet
    private final HttpServletRequest request;

    // services and components
    private final UserLinks userLinks;
    private final UserAccountService accountService;
    private final PersonService personService;
    private final UserResourceAssembler assembler;
    private final UserPagedResourcesAssembler pageAssembler;

    @Autowired(required=true)
    public UserController(
            HttpServletRequest request,
            UserLinks userLinks,
            UserAccountService accountService,
            PersonService personService,
            UserResourceAssembler assembler,
            UserPagedResourcesAssembler pageAssembler) {
        
        this.request = request;
        this.userLinks = userLinks;
        this.accountService = accountService;
        this.personService = personService;
        this.assembler = assembler;
        this.pageAssembler = pageAssembler;
    }

    /* GET requests */

    /**
     * Get all users (if ADMIN)
     * @param  principal current authenticated user
     * @param  pageable  page request
     * @return           page of users
     */
    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<PagedResources<UserResource>> users(@AuthenticationPrincipal UserAccount principal, Pageable pageable) {
        final PagedResources<UserResource> pagedResources;
        if (principal.getRoles()
                     .stream()
                     .anyMatch(r -> r.getName().equalsIgnoreCase("ADMIN"))) {
            try {
                final Page<UserAccount> page = accountService.findAll(pageable);
                pagedResources = pageAssembler.toResource(page, assembler, request);
            } catch (PropertyReferenceException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            pagedResources = pageAssembler.toResource(emptyPage(), assembler, request);
        }
        pagedResources.add(userLinks.search());
        return new ResponseEntity<>(pagedResources, HttpStatus.OK);
    }

    /**
     * Get user by username
     * @param username username
     * @return         user if found
     *                 404 if not
     */
    @RequestMapping(value=UserLinks.USER, method=RequestMethod.GET)
    public ResponseEntity<UserResource> getUser(@PathVariable String username) {
        final Optional<UserAccount> account = accountService.findByUsername(username);
        if (account.isPresent()) {
            final UserResource resource = assembler.toResource(account.get());
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * User search page
     * @param email user's email address
     */
    @RequestMapping(value=UserLinks.SEARCH, method=RequestMethod.GET)
    public ResponseEntity<UserResource> search(@RequestParam String email) {
        Optional<UserAccount> account = accountService.findByEmail(email);
        if (account.isPresent()) {
            final UserResource resource = assembler.toResource(account.get());
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
 
    /**
     * @return empty page of resources
     */
    protected Page<UserAccount> emptyPage() {
        return new PageImpl<UserAccount>(Collections.<UserAccount>emptyList(), null, 0);
    }
}