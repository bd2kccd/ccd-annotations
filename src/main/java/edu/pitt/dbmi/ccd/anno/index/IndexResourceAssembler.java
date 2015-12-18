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

package edu.pitt.dbmi.ccd.anno.index;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.EntityLinks;
import edu.pitt.dbmi.ccd.anno.group.GroupResource;
import edu.pitt.dbmi.ccd.anno.user.UserResource;
import edu.pitt.dbmi.ccd.anno.vocabulary.VocabularyResource;

@Component
public final class IndexResourceAssembler {

   // Message to display
   private static final String message = "CCD Annotations Application v0.2.0 (beta)";

   private final RelProvider relProvider;
   private final EntityLinks entityLinks;
   
   @Autowired(required=true)
   public IndexResourceAssembler(RelProvider relProvider, EntityLinks entityLinks) {
      this.relProvider = relProvider;
      this.entityLinks = entityLinks;
   }

   /**
    * Build index
    * @return index
    */
   public IndexResource buildIndex() {
      final IndexResource resource = new IndexResource(message,
            entityLinks.linkToCollectionResource(GroupResource.class).withRel(relProvider.getCollectionResourceRelFor(GroupResource.class)),
            entityLinks.linkToCollectionResource(UserResource.class).withRel(relProvider.getCollectionResourceRelFor(UserResource.class)),
            entityLinks.linkToCollectionResource(VocabularyResource.class).withRel(relProvider.getCollectionResourceRelFor(VocabularyResource.class))
      );
      return resource;
   }
}