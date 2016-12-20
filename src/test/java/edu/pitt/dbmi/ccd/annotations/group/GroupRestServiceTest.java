package edu.pitt.dbmi.ccd.annotations.group;

import static org.junit.Assert.assertEquals;

import edu.pitt.dbmi.ccd.annotations.error.GroupNotFoundException;
import edu.pitt.dbmi.ccd.db.entity.Group;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Mark Silvis (marksilvis@pitt.edu))
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GroupRestServiceTest {

    @Autowired
    private GroupRestService groupRestService;

    @Test
    public void findById() {
        final Long id = 1L;
        final Group group = groupRestService.findById(id);
        assertEquals((Long) 1L, group.getId());
    }

    @Test(expected = GroupNotFoundException.class)
    public void findByIdNotFound() {
        final Long id = -1L;
        final Group group = groupRestService.findById(id);
    }

}
