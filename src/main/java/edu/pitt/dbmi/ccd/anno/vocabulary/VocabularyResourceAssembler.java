/*
 * Copyright (C) 2015 University of Pittsburgh.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package edu.pitt.dbmi.ccd.anno.vocabulary;

import edu.pitt.dbmi.ccd.db.entity.Vocabulary;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Assembles Vocabulary into VocabularyResource
 *
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Component
public class VocabularyResourceAssembler extends ResourceAssemblerSupport<Vocabulary, VocabularyResource> {

    private final VocabularyLinks vocabularyLinks;

    @Autowired(required = true)
    public VocabularyResourceAssembler(VocabularyLinks vocabularyLinks) {
        super(VocabularyController.class, VocabularyResource.class);
        this.vocabularyLinks = vocabularyLinks;
    }

    /**
     * convert Vocabulary to VocabularyResource
     *
     * @param vocabulary entity
     * @return resource
     */
    @Override
    public VocabularyResource toResource(Vocabulary vocabulary) throws IllegalArgumentException {
        Assert.notNull(vocabulary);
        VocabularyResource resource = createResourceWithId(vocabulary.getId(), vocabulary);
        resource.add(vocabularyLinks.attributes(vocabulary));
        resource.add(vocabularyLinks.annotations(vocabulary));
        return resource;
    }

    /**
     * convert Vocabularies to VocabularyResources
     *
     * @param vocabularies entities
     * @return List of resources
     */
    @Override
    public List<VocabularyResource> toResources(Iterable<? extends Vocabulary> vocabularies) throws IllegalArgumentException {
        // Assert vocabularies is not empty
        Assert.isTrue(vocabularies.iterator().hasNext());
        return StreamSupport.stream(vocabularies.spliterator(), false)
                .map(this::toResource)
                .collect(Collectors.toList());
    }

    /**
     * Instantiate VocabularyResource with non-default constructor
     *
     * @param vocabulary entity
     * @return resource
     */
    @Override
    protected VocabularyResource instantiateResource(Vocabulary vocabulary) throws IllegalArgumentException {
        Assert.notNull(vocabulary);
        try {
            return BeanUtils.instantiateClass(VocabularyResource.class.getConstructor(Vocabulary.class), vocabulary);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
            return new VocabularyResource();
        }
    }
}
