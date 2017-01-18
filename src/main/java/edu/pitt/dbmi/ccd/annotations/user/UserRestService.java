package edu.pitt.dbmi.ccd.annotations.user;

import javax.validation.constraints.NotNull;

import edu.pitt.dbmi.ccd.annotations.error.NotAnAdminException;
import edu.pitt.dbmi.ccd.annotations.error.UserNotFoundException;
import edu.pitt.dbmi.ccd.db.entity.UserAccount;
import edu.pitt.dbmi.ccd.db.entity.UserRole;
import edu.pitt.dbmi.ccd.db.service.PersonService;
import edu.pitt.dbmi.ccd.db.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;

/**
 * @author Mark Silvis (marksilvis@pitt.edu))
 */
public class UserRestService {

    private final UserAccountService accountService;
    private final PersonService personService;

    @Autowired(required = true)
    public UserRestService(UserAccountService accountService, PersonService personService) {
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
    public UserAccount findByEmail(final String email) throws UserNotFoundException {
        final UserAccount user = accountService.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("email", email);
        } else {
            return user;
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
