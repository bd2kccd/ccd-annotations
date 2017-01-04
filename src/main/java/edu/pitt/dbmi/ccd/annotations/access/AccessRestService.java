package edu.pitt.dbmi.ccd.annotations.access;

import javax.validation.constraints.NotNull;

import edu.pitt.dbmi.ccd.annotations.error.AccessNotFoundException;
import edu.pitt.dbmi.ccd.db.entity.Access;
import edu.pitt.dbmi.ccd.db.service.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author Mark Silvis (marksilvis@pitt.edu))
 */
@Service
public class AccessRestService {

    private final AccessService accessService;

    @Autowired(required = true)
    public AccessRestService(AccessService accessService) {
        this.accessService = accessService;
    }

    @NotNull
    public Access save(final Access access) {
        return accessService.save(access);
    }

    @NotNull
    public Access findById(final Long id) throws AccessNotFoundException {
        final Access access = accessService.findById(id);
        if (access == null) {
            throw new AccessNotFoundException(id);
        }
        return access;
    }

    @NotNull
    public Access findByName(final String name) throws AccessNotFoundException {
        final Access access = accessService.findByName(name);
        if (access == null) {
            throw new AccessNotFoundException(name);
        }
        return access;
    }

    @NotNull
    public Page<Access> findAll(final Pageable pageable) {
        return accessService.findAll(pageable);
    }

    public void delete(final Access access) {
        accessService.delete(access);
    }
}
