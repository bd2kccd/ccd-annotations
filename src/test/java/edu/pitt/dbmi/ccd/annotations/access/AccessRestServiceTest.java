package edu.pitt.dbmi.ccd.annotations.access;

import static org.junit.Assert.assertEquals;

import edu.pitt.dbmi.ccd.annotations.error.AccessNotFoundException;
import edu.pitt.dbmi.ccd.db.entity.Access;
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
public class AccessRestServiceTest {

    private static final Long ID = 1L;
    private static final Long NONE = -1L;

    @Autowired
    private AccessRestService accessRestService;

    @Test
    public void findById() {
        final Access access = accessRestService.findById(ID);
        assertEquals(ID, access.getId());
    }

    @Test(expected = AccessNotFoundException.class)
    public void findByIdNotFound() {
        final Access access = accessRestService.findById(NONE);
    }

}
