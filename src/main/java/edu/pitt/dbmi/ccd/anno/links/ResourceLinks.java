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

package edu.pitt.dbmi.ccd.anno.links;

import javax.servlet.http.HttpServletRequest;
import org.springframework.hateoas.Link;

/**
 * Interface for defining links for resources
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
public interface ResourceLinks {

    Link self();

    Link search();

    // get link of requested URL
    default Link getRequestLink(HttpServletRequest request) {
        final StringBuffer url = request.getRequestURL();
        final String query = request.getQueryString();
        if (query == null) {
            return new Link(url.toString(), Link.REL_SELF);
        } else {
            return new Link(url.append("?").append(query).toString(), Link.REL_SELF);
        }
    }
}