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

    private static final Long ID = 1L;
    private static final Long NONE = -1L;

    @Autowired
    private GroupRestService groupRestService;

    @Test
    public void findById() {
        final Group group = groupRestService.findById(ID);
        assertEquals(ID, group.getId());
    }

    @Test(expected = GroupNotFoundException.class)
    public void findByIdNotFound() {
        final Group group = groupRestService.findById(NONE);
    }

}
