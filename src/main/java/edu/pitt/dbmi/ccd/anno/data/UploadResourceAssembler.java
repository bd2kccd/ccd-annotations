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

package edu.pitt.dbmi.ccd.anno.data;

import java.util.List;
import java.util.stream.StreamSupport;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.util.Assert;
import edu.pitt.dbmi.ccd.db.entity.Upload;
import edu.pitt.dbmi.ccd.anno.user.UserLinks;

/**
 * Assembles Upload into UploadResource
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Component
public class UploadResourceAssembler extends ResourceAssemblerSupport<Upload, UploadResource> {

    private final UploadLinks uploadLinks;
    private final UserLinks userLinks;

    @Autowired(required=true)
    public UploadResourceAssembler(UploadLinks uploadLinks, UserLinks userLinks) {
        super(UploadController.class, UploadResource.class);
        this.uploadLinks = uploadLinks;
        this.userLinks = userLinks;
    }

    /**
     * convert Upload to UploadResource
     * @param  upload entity
     * @return        resource
     */
    @Override
    public UploadResource toResource(Upload upload) {
        Assert.notNull(upload);
        UploadResource resource = createResourceWithId(upload.getId(), upload);
        resource.add(userLinks.user(upload.getUploader()));
        resource.add(uploadLinks.annotations(upload));
        return resource;
    }

    /**
     * convert Uploads to UploadResources
     * @param  uploads entities
     * @return        List of resources
     */
    @Override
    public List<UploadResource> toResources(Iterable<? extends Upload> uploads) {
        Assert.isTrue(uploads.iterator().hasNext());
        return StreamSupport.stream(uploads.spliterator(), false)
                            .map(this::toResource)
                            .collect(Collectors.toList());
    }

    /**
     * Instantiate UploadResource with non-default constructor
     * @param  upload entity
     * @return        resource
     */
    @Override
    protected UploadResource instantiateResource(Upload upload) {
        Assert.notNull(upload);
        try {
            return BeanUtils.instantiateClass(UploadResource.class.getConstructor(Upload.class), upload);
        } catch(NoSuchMethodException nsme) {
            nsme.printStackTrace();
            return new UploadResource();
        }
    }
}
