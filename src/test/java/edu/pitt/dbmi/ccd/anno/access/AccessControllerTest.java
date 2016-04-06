package edu.pitt.dbmi.ccd.anno.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.pitt.dbmi.ccd.anno.CCDAnnoApplication;
import edu.pitt.dbmi.ccd.anno.error.NotFoundException;

/**
 * Mark Silvis (marksilvis@pitt.edu)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CCDAnnoApplication.class)
public class AccessControllerTest {

    @Autowired
    private AccessController accessController;

    private final Pageable pageable = new PageRequest(0, 3);

    @Test
    public void accesses() {
        PagedResources<AccessResource> pagedResources= accessController.accesses(pageable);
        Collection<AccessResource> accessResources = pagedResources.getContent();
        assertEquals(3, accessResources.size());
    }

    @Test
    public void access() {
        final String name = "PUBLIC";
        AccessResource accessResource = accessController.access(name);
        assertNotNull(accessResource);
        assertEquals(name, accessResource.getName());
    }

    @Test(expected = NotFoundException.class)
    public void accessNotFound() {
        accessController.access("DOES NOT EXIST");
    }
}