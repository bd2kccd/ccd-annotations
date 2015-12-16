package edu.pitt.dbmi.ccd.anno.index;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.PagedResources;
import edu.pitt.dbmi.ccd.db.entity.Group;
import edu.pitt.dbmi.ccd.db.service.GroupService;
import edu.pitt.dbmi.ccd.anno.group.GroupResource;
import edu.pitt.dbmi.ccd.anno.group.GroupController;

// logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@RestController
@RequestMapping(value="/")
public class IndexController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    private final IndexResourceAssembler assembler;

    @Autowired(required=true)
    public IndexController(IndexResourceAssembler assembler) {
        this.assembler = assembler;
    }

    // application index
    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<IndexResource> index() {
        return new ResponseEntity<IndexResource>(assembler.buildIndex(), HttpStatus.OK);
    }
}