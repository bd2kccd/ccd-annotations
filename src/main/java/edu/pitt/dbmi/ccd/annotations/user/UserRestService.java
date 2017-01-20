package edu.pitt.dbmi.ccd.annotations.user;

import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import edu.pitt.dbmi.ccd.annotations.error.ForbiddenException;
import edu.pitt.dbmi.ccd.annotations.error.NotAnAdminException;
import edu.pitt.dbmi.ccd.annotations.error.UserNotFoundException;
import edu.pitt.dbmi.ccd.db.entity.Group;
import edu.pitt.dbmi.ccd.db.entity.UserAccount;
import edu.pitt.dbmi.ccd.db.entity.UserRole;
import edu.pitt.dbmi.ccd.db.service.PersonService;
import edu.pitt.dbmi.ccd.db.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author Mark Silvis (marksilvis@pitt.edu))
 */
@Service
public class UserRestService {

    // servlet
    private final HttpServletRequest request;

    // services & components
    private final UserAccountService accountService;
    private final PersonService personService;

    @Autowired(required = true)
    public UserRestService(HttpServletRequest request, UserAccountService accountService, PersonService personService) {
        this.request = request;
        this.accountService = accountService;
        this.personService = personService;
    }

    @NotNull
    public UserAccount findById(final Long id) throws UserNotFoundException {
        final UserAccount user = accountService.findById(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        } else {
            return user;
        }
    }

    @NotNull
    public UserAccount findByAccountId(final String accountId) throws UserNotFoundException {
        final UserAccount user = accountService.findByAccountId(accountId);
        if (user == null) {
            throw new UserNotFoundException(accountId);
        } else {
            return user;
        }
    }

    @NotNull
    public UserAccount findByUsername(final String username) throws UserNotFoundException {
        final UserAccount user = accountService.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("username", username);
        } else {
            return user;
        }
    }

    @NotNull
    public UserAccount findByEmail(final String email) throws UserNotFoundException {
        final UserAccount user = accountService.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("email", email);
        } else {
            return user;
        }
    }

    @NotNull
    public Page<UserAccount> findByGroupMembership(UserAccount requester, Group group, Pageable pageable) throws ForbiddenException {
        if (Stream.concat(group.getMembers().stream(), group.getModerators().stream())
                .map(UserAccount::getId)
                .anyMatch(userId -> userId.equals(requester.getId()))) {
            return accountService.findByGroupMembership(group, pageable);
        } else {
            throw new ForbiddenException(requester, request);
        }
    }

    @NotNull
    public Page<UserAccount> findByGroupModeration(UserAccount requester, Group group, Pageable pageable) throws ForbiddenException {
        if (Stream.concat(group.getMembers().stream(), group.getModerators().stream())
                .map(UserAccount::getId)
                .anyMatch(userId -> userId.equals(requester.getId()))) {
            return accountService.findByGroupModeration(group, pageable);
        } else {
            throw new ForbiddenException(requester, request);
        }
    }

    @NotNull
    public Page<UserAccount> findByGroupRequests(UserAccount requester, Group group, Pageable pageable) throws ForbiddenException {
        if (group.getModerators().stream()
                .map(UserAccount::getId)
                .anyMatch(userId -> userId.equals(requester.getId()))) {
            return accountService.findByGroupRequests(group, pageable);
        } else {
            throw new ForbiddenException(requester, request);
        }
    }

    @NotNull
    public Page<UserAccount> findAll(final UserAccount requester, final Pageable pageable) throws NotAnAdminException {
        if (requester.getUserRoles().stream()
                .map(UserRole::getName)
                .anyMatch(role -> role.equalsIgnoreCase("ADMIN"))) {
            return accountService.findAll(pageable);
        } else {
            throw new NotAnAdminException(requester);
        }
    }

}
