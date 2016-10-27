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

package edu.pitt.dbmi.ccd.anno.group;

import static edu.pitt.dbmi.ccd.anno.util.ControllerUtils.formatParam;
import static org.springframework.util.StringUtils.isEmpty;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.pitt.dbmi.ccd.anno.error.ForbiddenException;
import edu.pitt.dbmi.ccd.anno.error.GroupNotFoundException;
import edu.pitt.dbmi.ccd.anno.error.NotAMemberException;
import edu.pitt.dbmi.ccd.anno.error.NotFoundException;
import edu.pitt.dbmi.ccd.anno.error.UserNotFoundException;
import edu.pitt.dbmi.ccd.anno.user.UserPagedResourcesAssembler;
import edu.pitt.dbmi.ccd.anno.user.UserResource;
import edu.pitt.dbmi.ccd.anno.user.UserResourceAssembler;
import edu.pitt.dbmi.ccd.db.entity.Group;
import edu.pitt.dbmi.ccd.db.entity.UserAccount;
import edu.pitt.dbmi.ccd.db.service.GroupService;
import edu.pitt.dbmi.ccd.db.service.UserAccountService;
import edu.pitt.dbmi.ccd.security.userDetails.UserAccountDetails;

/**
 * Controller for Group endpoints
 *
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@RestController
@ExposesResourceFor(GroupResource.class)
@RequestMapping(value = GroupLinks.INDEX)
public class GroupController {

    // loggers
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupController.class);

    // servlet
    private final HttpServletRequest request;

    // services and components
    private final GroupLinks groupLinks;
    private final GroupService groupService;
    private final UserAccountService userService;
    private final GroupResourceAssembler assembler;
    private final GroupPagedResourcesAssembler pageAssembler;
    private final UserResourceAssembler userAssembler;
    private final UserPagedResourcesAssembler userPageAssembler;

    @Autowired
    public GroupController(
            HttpServletRequest request,
            GroupLinks groupLinks,
            GroupService groupService,
            UserAccountService userService,
            GroupResourceAssembler assembler,
            GroupPagedResourcesAssembler pageAssembler,
            UserResourceAssembler userAssembler,
            UserPagedResourcesAssembler userPageAssembler) {
        this.request = request;
        this.groupLinks = groupLinks;
        this.groupService = groupService;
        this.userService = userService;
        this.assembler = assembler;
        this.pageAssembler = pageAssembler;
        this.userAssembler = userAssembler;
        this.userPageAssembler = userPageAssembler;
    }

    /* GET requests */

    /**
     * Get all groups
     *
     * @param pageable page request
     * @return page of groups
     */
    // @CrossOrigin
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PagedResources<GroupResource> groups(@PageableDefault(size = 20, sort = {"name"}) Pageable pageable) {
        final Page<Group> page = groupService.findAll(pageable);
        final PagedResources<GroupResource> pagedResources = pageAssembler.toResource(page, assembler, request);
        pagedResources.add(groupLinks.search());
        return pagedResources;
    }

    /**
     * Get single group
     *
     * @param id group id
     * @return group
     */
    @RequestMapping(value = GroupLinks.GROUP, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GroupResource group(@PathVariable Long id) throws NotFoundException {
        final Group group = groupService.findById(id);
        if (group == null) {
            throw new GroupNotFoundException(id);
        }
        final GroupResource resource = assembler.toResource(group);
        return resource;
    }

    /**
     * Get group mods
     *
     * @param id group id
     * @return mods
     */
    @RequestMapping(value = GroupLinks.MODS, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PagedResources<UserResource> mods(@AuthenticationPrincipal UserAccountDetails principal, @PathVariable Long id, @PageableDefault(size = 20, sort = {"username"}) Pageable pageable) throws NotFoundException {
        final UserAccount requester = principal.getUserAccount();
        final Group group = groupService.findById(id);
        if (group == null) {
            throw new GroupNotFoundException(id);
        }
        if (Stream.concat(group.getMembers().stream(), group.getModerators().stream())
                .map(UserAccount::getId)
                .anyMatch(u -> u.equals(requester.getId()))) {
            final Page<UserAccount> page = userService.findByGroupModeration(group, pageable);
            final PagedResources<UserResource> pagedResources = userPageAssembler.toResource(page, userAssembler, request);
            return pagedResources;
        } else {
            throw new ForbiddenException(requester, request);
        }
    }

    /**
     * Get group members
     *
     * @param id group id
     * @return members
     */
    @RequestMapping(value = GroupLinks.MEMBERS, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PagedResources<UserResource> members(@AuthenticationPrincipal UserAccountDetails principal, @PathVariable Long id, @PageableDefault(size = 20, sort = {"username"}) Pageable pageable) throws NotFoundException {
        final UserAccount requester = principal.getUserAccount();
        final Group group = groupService.findById(id);
        if (group == null) {
            throw new GroupNotFoundException(id);
        }if (Stream.concat(group.getMembers().stream(), group.getModerators().stream())
                .map(UserAccount::getId)
                .anyMatch(u -> u.equals(requester.getId()))) {
            final Page<UserAccount> page = userService.findByGroupMembership(group, pageable);
            final PagedResources<UserResource> pagedResources = userPageAssembler.toResource(page, userAssembler, request);
            return pagedResources;
        } else {
            throw new ForbiddenException(requester, request);
        }
    }

    /**
     * Get group requests
     *
     * @param id group id
     * @return requesters
     */
    @RequestMapping(value = GroupLinks.REQUESTS, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Resources<UserResource> requests(@AuthenticationPrincipal UserAccountDetails principal, @PathVariable Long id, @PageableDefault(size = 20, sort = {"username"}) Pageable pageable) throws NotFoundException {
        final UserAccount requester = principal.getUserAccount();
        final Group group = groupService.findById(id);
        if (group == null) {
            throw new GroupNotFoundException(id);
        }
        if (group.getModerators()
                .stream()
                .map(UserAccount::getId)
                .anyMatch(u -> u.equals(requester.getId()))) {
            final Page<UserAccount> page = userService.findByGroupRequests(group, pageable);
            final PagedResources<UserResource> pagedResources = userPageAssembler.toResource(page, userAssembler, request);
            return pagedResources;
        } else {
            throw new ForbiddenException(requester, request);
        }
    }

    /**
     * Search for groups
     *
     * @param query    search terms (nullable)
     * @param not      negated search terms (nullable)
     * @param pageable page request
     * @return page of groups matching parameters
     */
    @RequestMapping(value = GroupLinks.SEARCH, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PagedResources<GroupResource> search(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "not", required = false) String not,
            Pageable pageable) {
        final Set<String> matches = (query != null)
                ? new HashSet<>(formatParam(query))
                : null;
        final Set<String> nots = (not != null)
                ? new HashSet<>(formatParam(not))
                : null;
        final Page<Group> page = groupService.search(matches, nots, pageable);
        final PagedResources<GroupResource> pagedResources = pageAssembler.toResource(page, assembler, request);
        return pagedResources;
    }

    /* POST requests */

    /**
     * Create new group with name and description
     * Creator is added to list of administrators
     *
     * @param principal requester
     * @param form      group data
     * @return new group resource
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GroupResource create(@AuthenticationPrincipal UserAccountDetails principal, @RequestBody @Valid GroupForm form) throws DuplicateKeyException {
        final UserAccount requester = principal.getUserAccount();
        final String name = form.getName();
        final String description = form.getDescription();

        // throw exception if non-unique name
        if (groupService.findByName(name) != null) {
            throw new DuplicateKeyException("Group already exists with name: " + name);
        }
        Group group = new Group(name, description);
        group.addMember(requester);
        group.addModerator(requester);
        group = groupService.save(group);
        GroupResource resource = assembler.toResource(group);
        return resource;
    }

    @RequestMapping(value = GroupLinks.REQUESTS, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void requestResponse(
            @AuthenticationPrincipal UserAccountDetails principal,
            @PathVariable String name,
            @RequestParam(value = "accept", required = false) String accept,
            @RequestParam(value = "deny", required = false) String deny,
            @RequestParam(value = "mod", required = false) Boolean mod)
            throws NotFoundException {
        final UserAccount requester = principal.getUserAccount();
        final Group group = groupService.findByName(name);
        if (group == null) {
            throw new GroupNotFoundException(name);
        }
        if (group.getModerators()
                .stream()
                .map(UserAccount::getId)
                .anyMatch(u -> u.equals(requester.getId()))) {
            if (!isEmpty(accept)) {
                final UserAccount user = userService.findByUsername(accept);
                if (user == null) {
                    throw new UserNotFoundException(accept);
                }
                if (group.hasRequester(user)) {
                    group.removeRequester(user);
                    group.addMember(user);
                    if (mod != null && mod) {
                        group.addModerator(user);
                    }
                    groupService.save(group);
                }
            }

            if (!isEmpty(deny)) {
                final UserAccount user = userService.findByUsername(accept);
                if (user == null) {
                    throw new UserNotFoundException(accept);
                }
                if (group.hasRequester(user)) {
                    group.removeRequester(user);
                    groupService.save(group);
                }
            }
        } else {
            throw new ForbiddenException(requester, request);
        }
    }

    @RequestMapping(value = GroupLinks.JOIN, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void join(@AuthenticationPrincipal UserAccountDetails principal, @PathVariable String name) throws NotFoundException {
        final UserAccount requester = principal.getUserAccount();
        final Group group = groupService.findByName(name);
        if (group == null) {
            throw new GroupNotFoundException(name);
        }         if (Stream.concat(group.getModerators().stream(), group.getMembers().stream())
                .map(UserAccount::getId)
                .noneMatch(u -> u.equals(requester.getId()))) {
            group.addRequester(requester);
            groupService.save(group);
        }
    }

    @RequestMapping(value = GroupLinks.LEAVE, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void leave(@AuthenticationPrincipal UserAccountDetails principal, @PathVariable String name) throws NotFoundException {
        UserAccount requester = principal.getUserAccount();
        final Group group = groupService.findByName(name);
        if (group == null) {
            throw new GroupNotFoundException(name);
        }         // remove if moderator
        group.getModerators()
                .stream()
                .filter(u -> u.getId().equals(requester.getId()))
                .findFirst()
                .ifPresent(u -> group.removeModerator(u));

        // remove if member
        group.getMembers()
                .stream()
                .filter(u -> u.getId().equals(requester.getId()))
                .findFirst()
                .ifPresent(u -> group.removeMember(u));

        // remove if requester
        group.getRequesters()
                .stream()
                .filter(u -> u.getId().equals(requester.getId()))
                .findFirst()
                .ifPresent(u -> group.removeRequester(u));

        groupService.save(group);
    }

    @RequestMapping(value = GroupLinks.MODS, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void makeMod(@AuthenticationPrincipal UserAccountDetails principal, @PathVariable String name, @RequestParam(value = "user", required = true) String user) throws NotFoundException {
        UserAccount requester = principal.getUserAccount();
        final Group group = groupService.findByName(name);
        if (group == null) {
            throw new GroupNotFoundException(name);
        }
        if (group.getModerators()
                .stream()
                .map(UserAccount::getId)
                .anyMatch(u -> u.equals(requester.getId()))) {
            final UserAccount mod = userService.findByUsername(user);
            if (user == null) {
                throw new UserNotFoundException(user);
            }
            if (group.hasMember(mod)) {
                group.addModerator(mod);
                groupService.save(group);
            } else {
                throw new NotAMemberException(group, mod);
            }
        } else {
            throw new ForbiddenException(requester, request);
        }
    }

    /* PUT requests */
//
//    /**
//     * Alias for newGroup with HTTP method PUT
//     * @param  principal requester
//     * @param  form      group data
//     * @return           new group resource
//     */
//    @RequestMapping(method=RequestMethod.PUT)
//    @ResponseStatus(HttpStatus.CREATED)
//    @ResponseBody
//    public GroupResource newGroupPUT(@AuthenticationPrincipal UserAccount principal, @RequestBody @Valid GroupForm form) {
//        return create(principal, form);
//    }

    /**
     * Edit group
     *
     * @param name group name
     * @return group
     */
    @RequestMapping(value = GroupLinks.GROUP, method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GroupResource editGroup(@AuthenticationPrincipal UserAccountDetails principal, @PathVariable String name, @RequestBody @Valid GroupForm form) throws NotFoundException, DuplicateKeyException, ForbiddenException, ConstraintViolationException {
        UserAccount requester = principal.getUserAccount();
        final Group group = groupService.findByName(name);
        if (group == null) {
            throw new GroupNotFoundException(name);
        }
        if (group.getModerators()
                .stream()
                .map(UserAccount::getId)
                .anyMatch(u -> u.equals(requester.getId()))) {
            final String newName = form.getName();
            final String newDescription = form.getDescription();
            if (!group.getName().equalsIgnoreCase(newName)) {
                if (groupService.findByName(newName) != null)
                    throw new DuplicateKeyException("Group already exists with name: " + newName);
                }
                group.setName(newName);
            if (!group.getDescription().equalsIgnoreCase(newDescription)) {
                group.setDescription(newDescription);
            }
            final Group updated = groupService.save(group);
            final GroupResource resource = assembler.toResource(updated);
            return resource;
        } else {
            throw new ForbiddenException(requester, request);
        }
    }
//
//    @RequestMapping(value=GroupLinks.MODS, method=RequestMethod.PUT)
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public void makeModPUT(@AuthenticationPrincipal UserAccount principal, @PathVariable String name, @RequestParam(value="user", required=true) String user) {
//        makeMod(principal, name, user);
//    }

    /* PATCH requests */

    /**
     * Patch group
     *
     * @param principal [description]
     * @param name      [description]
     * @param form      [description]
     * @return [description]
     */
    @RequestMapping(value = GroupLinks.GROUP, method = RequestMethod.PATCH)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GroupResource patchGroup(@AuthenticationPrincipal UserAccountDetails principal, @PathVariable String name, @RequestBody GroupForm form) throws NotFoundException, DuplicateKeyException, ConstraintViolationException {
        UserAccount requester = principal.getUserAccount();
        final Group group = groupService.findByName(name);
        if (group == null) {
            throw new GroupNotFoundException(name);
        }
        if (group.getModerators()
                .stream()
                .map(UserAccount::getId)
                .anyMatch(u -> u.equals(requester.getId()))) {
            final String newName = form.getName();
            final String newDescription = form.getDescription();
            if (!isEmpty(newName) && !group.getName().equalsIgnoreCase(newName)) {
                if (groupService.findByName(newName) != null) {
                    throw new DuplicateKeyException("Group already exists with name: " + newName);
                }
                group.setName(newName);
            }
            if (!isEmpty(newDescription) && !group.getDescription().equalsIgnoreCase(newDescription)) {
                group.setDescription(newDescription);
            }
            final Group patched = groupService.save(group);
            final GroupResource resource = assembler.toResource(patched);
            return resource;
        } else {
            throw new ForbiddenException(requester, request);
        }
    }
}
