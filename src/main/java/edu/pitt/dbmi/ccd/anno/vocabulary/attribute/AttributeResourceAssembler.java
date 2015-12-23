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

package edu.pitt.dbmi.ccd.anno.vocabulary.attribute;

import java.util.List;
import java.util.stream.StreamSupport;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.util.Assert;
import edu.pitt.dbmi.ccd.db.entity.Attribute;
import edu.pitt.dbmi.ccd.db.entity.Vocabulary;
import edu.pitt.dbmi.ccd.anno.vocabulary.VocabularyLinks;

/**
 * Assembles Attribute into AttributeResource
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Component
public class AttributeResourceAssembler extends ResourceAssemblerSupport<Attribute, AttributeResource> {

    private final VocabularyLinks vocabLinks;
    private final AttributeLinks attributeLinks;

    @Autowired(required=true)
    public AttributeResourceAssembler(VocabularyLinks vocabLinks, AttributeLinks attributeLinks) {
        super(AttributeController.class, AttributeResource.class);
        this.vocabLinks = vocabLinks;
        this.attributeLinks = attributeLinks;
    }

    /**
     * convert Attribute to AttributeResource
     * @param  attribute entity
     * @return            resource
     */
    @Override
    public AttributeResource toResource(Attribute attribute) {
        Assert.notNull(attribute);
        Vocabulary vocabulary = attribute.getVocabulary();
        Attribute parent = attribute.getParent();
        AttributeResource resource = createResourceWithId(attribute.getInnerId(), attribute, vocabulary.getName());
        resource.add(vocabLinks.vocabulary(vocabulary));
        if (parent != null) {
            resource.add(attributeLinks.attribute(parent));
        }
        return resource;
    }

    /**
     * convert Attributes to AttributeResources
     * @param  attributes entities
     * @return              List of resources
     */
    @Override
    public List<AttributeResource> toResources(Iterable<? extends Attribute> attributes) {
        Assert.isTrue(attributes.iterator().hasNext());
        return StreamSupport.stream(attributes.spliterator(), false)
                                .map(this::toResource)
                                .collect(Collectors.toList());
    }

    /**
     * Instantiate AttributeResource with non-default constructor
     * @param  attribute entity
     * @return            resource
     */
    @Override
    protected AttributeResource instantiateResource(Attribute attribute) {
        Assert.notNull(attribute);
        try {
            return BeanUtils.instantiateClass(AttributeResource.class.getConstructor(Attribute.class), attribute);
        } catch(NoSuchMethodException nsme) {
            nsme.printStackTrace();
            return new AttributeResource();
        }
    }
}
