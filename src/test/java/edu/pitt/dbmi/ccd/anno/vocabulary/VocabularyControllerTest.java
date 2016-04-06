package edu.pitt.dbmi.ccd.anno.vocabulary;

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
import edu.pitt.dbmi.ccd.anno.vocabulary.attribute.AttributeResource;

/**
 * Mark Silvis (marksilvis@pitt.edu)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CCDAnnoApplication.class)
public class VocabularyControllerTest {

    @Autowired
    private VocabularyController vocabularyController;

    private final Pageable pageable = new PageRequest(0, 1);

    @Test
    public void vocabularies() {
        PagedResources<VocabularyResource> pagedResources = vocabularyController.vocabularies(pageable);
        Collection<VocabularyResource> vocabularyResources = pagedResources.getContent();
        assertEquals(1, vocabularyResources.size());
    }

    @Test
    public void vocabulary() {
        final String name = "Plaintext";
        VocabularyResource vocabularyResource = vocabularyController.vocabulary(name);
        assertNotNull(vocabularyResource);
        assertEquals(name, vocabularyResource.getName());
    }

    @Test
    public void attributes() {
        final String name = "Plaintext";
        PagedResources<AttributeResource> pagedResources = vocabularyController.attributes(name, null, null, null, pageable);
        Collection<AttributeResource> attributeResources = pagedResources.getContent();
        assertEquals(1, attributeResources.size());
    }

    @Test
    public void attributesFilter() {
        final String name = "Plaintext";
        final String attributeName = "text";
        final String level = "DOES NOT EXIST";

        // match one
        PagedResources<AttributeResource> pagedResources = vocabularyController.attributes(name, null, attributeName, null, pageable);
        Collection<AttributeResource> attributeResources = pagedResources.getContent();
        assertEquals(1, attributeResources.size());

        // match none
        pagedResources = vocabularyController.attributes(name, level, attributeName, null, pageable);
        attributeResources = pagedResources.getContent();
        assertEquals(0, attributeResources.size());
    }

    @Test
    public void attribute() {
        final String name = "Plaintext";
        final Long id = 1L;
        AttributeResource attributeResource = vocabularyController.attribute(name, id);
        assertNotNull(attributeResource);
        assertEquals(id, attributeResource.getIdentifer());
    }

    @Test
    public void search() {
        // matches
        final String search = "text";
        PagedResources<VocabularyResource> pagedResources = vocabularyController.search(  search, null,   pageable);
        Collection<VocabularyResource> vocabularyResources = pagedResources.getContent();
        assertEquals(1, vocabularyResources.size());

        // does not match
        pagedResources = vocabularyController.search(null, search, pageable);
        vocabularyResources = pagedResources.getContent();
        assertEquals(0, vocabularyResources.size());
    }

    @Test(expected = NotFoundException.class)
    public void vocabularyNotFound() {
        vocabularyController.vocabulary("DOES NOT EXIST");
    }

    @Test(expected = NotFoundException.class)
    public void attributesVocabularyNotFound() {
        vocabularyController.attributes("DOES NOT EXIST", null, null, null, pageable);
    }

    @Test(expected = NotFoundException.class)
    public void attributeVocabularyNotFound() {
        vocabularyController.attribute("DOES NOT EXIST", 1L);
    }

    @Test(expected = NotFoundException.class)
    public void attributeNotFound() {
        final String name = "Plaintext";
        vocabularyController.attribute(name, 2L);
    }
}