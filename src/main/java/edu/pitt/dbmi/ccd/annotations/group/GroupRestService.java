package edu.pitt.dbmi.ccd.annotations.group;

import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotNull;

import com.google.common.collect.Sets;
import edu.pitt.dbmi.ccd.annotations.error.GroupNotFoundException;
import edu.pitt.dbmi.ccd.db.entity.Group;
import edu.pitt.dbmi.ccd.db.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author Mark Silvis (marksilvis@pitt.edu))
 */
@Service
public class GroupRestService {

    private final GroupService groupService;

    @Autowired(required = true)
    public GroupRestService(GroupService groupService) {
        this.groupService = groupService;
    }

    @NotNull
    public Group save(final Group group) {
        return groupService.save(group);
    }

    @NotNull
    public Group findById(final Long id) throws GroupNotFoundException {
        final Group group = groupService.findById(id);
        if (group == null) {
            throw new GroupNotFoundException(id);
        }
        return group;
    }

    @NotNull
    public Group findByName(final String name) throws GroupNotFoundException {
        final Group group = groupService.findByName(name);
        if (group == null) {
            throw new GroupNotFoundException(name);
        }
        return group;
    }

    @NotNull
    public Page<Group> findAll(final Pageable pageable) {
        return groupService.findAll(pageable);
    }

    @NotNull
    public Page<Group> search(final Specification<Group> groupSpecification, final Pageable pageable) {
        return groupService.search(groupSpecification, pageable);
    }

    public void delete(final Group group) throws IllegalArgumentException {
        groupService.delete(group);
    }
}
