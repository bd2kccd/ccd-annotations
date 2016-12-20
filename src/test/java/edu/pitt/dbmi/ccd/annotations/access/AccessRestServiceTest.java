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

    @Autowired
    private AccessRestService accessRestService;

    @Test
    public void findById() {
        final Long id = 1L;
        final Access access = accessRestService.findById(id);
        assertEquals((Long) 1L, access.getId());
    }

    @Test(expected = AccessNotFoundException.class)
    public void findByIdNotFound() {
        final Long id = -1L;
        final Access access = accessRestService.findById(id);
    }

}
