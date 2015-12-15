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
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.PagedResources;
import edu.pitt.dbmi.ccd.db.entity.UserAccount;
import edu.pitt.dbmi.ccd.db.entity.Person;
import edu.pitt.dbmi.ccd.db.service.UserAccountService;
import edu.pitt.dbmi.ccd.db.service.PersonService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@RestController
@RequestMapping(value="users")
public class UserController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserAccountService accountService;
    private final PersonService personService;

    @Autowired(required=true)
    public UserController(UserAccountService accountService,
                          PersonService personService) {
        this.accountService = accountService;
        this.personService = personService;
    }

    // Get user by username
    @RequestMapping(value="/{username}", method=RequestMethod.GET)
    public ResponseEntity<UserResource> getUser(@PathVariable String username) {
        final UserAccount account = accountService.findByUsername(username);
        final UserResource resource;
        if (account != null) {
            resource = new UserResource(account);
            return new ResponseEntity<UserResource>(resource, HttpStatus.OK);
        } else {
            return new ResponseEntity<UserResource>(HttpStatus.NOT_FOUND);
        }
    }
}