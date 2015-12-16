package edu.pitt.dbmi.ccd.anno.index;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.EntityLinks;
import edu.pitt.dbmi.ccd.anno.group.GroupResource;
import edu.pitt.dbmi.ccd.anno.user.UserResource;
import edu.pitt.dbmi.ccd.anno.vocabulary.VocabularyResource;

@Service
public class IndexResourceAssembler {

   // Message to display
   private static final String message = "CCD Annotations Application v0.2.0 (beta)";

   private final RelProvider relProvider;
   private final EntityLinks entityLinks;
   
   @Autowired
   public IndexResourceAssembler(RelProvider relProvider, EntityLinks entityLinks) {
      this.relProvider = relProvider;
      this.entityLinks = entityLinks;
   }

   // Index with links to each resource
   public IndexResource buildIndex() {
      final IndexResource resource = new IndexResource(message,
            entityLinks.linkToCollectionResource(GroupResource.class).withRel(relProvider.getCollectionResourceRelFor(GroupResource.class)),
            entityLinks.linkToCollectionResource(UserResource.class).withRel(relProvider.getCollectionResourceRelFor(UserResource.class)),
            entityLinks.linkToCollectionResource(VocabularyResource.class).withRel(relProvider.getCollectionResourceRelFor(VocabularyResource.class))
      );
      return resource;
   }
}