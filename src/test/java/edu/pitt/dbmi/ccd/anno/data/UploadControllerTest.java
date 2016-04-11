package edu.pitt.dbmi.ccd.anno.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.pitt.dbmi.ccd.anno.CCDAnnoApplication;
import edu.pitt.dbmi.ccd.db.entity.Upload;
import edu.pitt.dbmi.ccd.db.entity.UserAccount;
import edu.pitt.dbmi.ccd.db.service.UploadService;
import edu.pitt.dbmi.ccd.db.service.UserAccountService;

/**
 * Mark Silvis (marksilvis@pitt.edu)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CCDAnnoApplication.class)
public class UploadControllerTest {

    @Autowired
    private UploadController uploadController;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private UserAccountService userAccountService;

    private final Pageable pageable = new PageRequest(0, 1);

    @Test
    @Ignore
    public void uploads() {
        PagedResources<UploadResource> pagedResources = uploadController.uploads(null, null, pageable);
        Collection<UploadResource> uploadResources = pagedResources.getContent();
        assertEquals(1, uploadResources.size());
    }

    @Test
    @Ignore
    public void uploadsFilter() {
        // matches one
        PagedResources<UploadResource> pagedResources = uploadController.uploads(null, "url", pageable);
        Collection<UploadResource> uploadResources = pagedResources.getContent();
        assertEquals(1, uploadResources.size());

        // matches none
        pagedResources = uploadController.uploads(null, "file", pageable);
        uploadResources = pagedResources.getContent();
        assertEquals(0, uploadResources.size());
    }

    @Test
    @Ignore
    public void upload() {
        final Long id = 1L;
        UploadResource uploadResource = uploadController.upload(id);
        assertNotNull(uploadResource);
        assertEquals(id, uploadResource.getIdentifier());
    }

    @Test
    @Ignore
    public void search() {
        // matches one
        final String search = "Biomedical";
        PagedResources<UploadResource> pagedResources = uploadController.search(null, null, search, null, pageable);
        Collection<UploadResource> uploadResources = pagedResources.getContent();
        assertEquals(1, uploadResources.size());

        // matches none
        pagedResources = uploadController.search(null, null, null, search, pageable);
        uploadResources = pagedResources.getContent();
        assertEquals(0, uploadResources.size());
    }

    @Test
    @Ignore
    public void newUpload() {
        UserAccount userAccount = userAccountService.findById(1L).get();
        UploadForm form = new UploadForm("Test", "twitter.com");
        UploadResource uploadResource = uploadController.create(userAccount, form);
        assertNotNull(uploadResource.getId());

        UploadResource foundUpload = uploadController.upload(uploadResource.getIdentifier());
        assertEquals(uploadResource.getIdentifier(), foundUpload.getIdentifier());

        Upload upload = uploadService.findById(uploadResource.getIdentifier()).get();
        uploadService.delete(upload);
    }
}