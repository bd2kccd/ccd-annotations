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

package edu.pitt.dbmi.ccd.anno.upload;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceAssembler;
import edu.pitt.dbmi.ccd.db.entity.Upload;

/**
 * Assembles page of UploadResources
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Component
public class UploadPagedResourcesAssembler extends PagedResourcesAssembler<Upload> {

    private final UploadLinks uploadLinks;

    /**
     * Create new PagedResourcesAssembler for Upload entity
     * @return UploadPagedResourcesAssembler
     */
    @Autowired(required=true)
    public UploadPagedResourcesAssembler(UploadLinks uploadLinks) {
        super(null, null);
        this.uploadLinks = uploadLinks;
    }

    /**
     * Create PagedResources of upload resources
     * @param  page      page of entities
     * @param  assembler resource assembler
     * @param  request   request data
     * @return           PagedResources of upload resources
     */
    public PagedResources<UploadResource> toResource(Page<Upload> page, ResourceAssembler<Upload, UploadResource> assembler, HttpServletRequest request) {
        final Link self = uploadLinks.getRequestLink(request);
        return this.toResource(page, assembler, self);
    }
}
