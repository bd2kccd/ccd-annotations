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

package edu.pitt.dbmi.ccd.anno.metadata;

import java.util.Set;
import java.util.HashSet;
import java.util.Date;
import java.util.Collection;

/**
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
public class MetaResponse {
    private Long id;

    private Date created;

    private Date modified;

    private Long userId;

    private String access;

    private Long groupId;

    private String vocab;

    private Long targetId;

    private Long parentAnnoId;

    private Set<MetadataResponse> data = new HashSet<>(0);

    public MetaResponse() { }

    public MetaResponse(Long id, Date created, Date modified, Long userId, String access, Long groupId,
        String vocab, Long targetId, Long parentAnnoId) {
        this.id = id;
        this.created = created;
        this.modified = modified;
        this.userId = userId;
        this.access = access;
        this.groupId = groupId;
        this.vocab = vocab;
        this.targetId = targetId;
        this.parentAnnoId = parentAnnoId;
    }

    public void addData(Collection<MetadataResponse> data) {
        this.data.addAll(data);
    }


}