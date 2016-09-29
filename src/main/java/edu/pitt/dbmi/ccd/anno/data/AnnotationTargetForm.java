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

import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import edu.pitt.dbmi.ccd.db.entity.AnnotationTarget;

/**
 * Upload entity POST request
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
public class AnnotationTargetForm {

    @NotBlank(message="Title cannot be empty")
    @Size(max=255, message="Title must be fewer than 255 characters")
    private String title;

    @URL(message="Address must be a valid URL")
    @NotBlank(message="Address cannot be empty")
    @Size(max=2083, message="Address must be fewer than 2083 characters")
    private String address;

    public AnnotationTargetForm() { }

    public AnnotationTargetForm(String title, String address) {
        this.title = title;
        this.address = address;
    }

    /**
     * Get title
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set title
     * @param title title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get address
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set address
     * @param address address
     */
    public void setAddress(String address) {
        this.address = address;
    }
}
