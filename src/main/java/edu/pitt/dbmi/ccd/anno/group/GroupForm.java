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

package edu.pitt.dbmi.ccd.anno.group;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import edu.pitt.dbmi.ccd.db.validation.Name;

/**
 * Group entity POST request
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
public class GroupForm {

    @NotBlank(message="Name cannot be empty")
    @Size(min=4, max=128, message="Name must be between 4 and 128 characters")
    @Name
    private String name;

    @NotBlank(message="Description cannot be empty")
    @Size(max=500, message="Description must be fewer than 500 characters")
    private String description;

    public GroupForm() { }

    public GroupForm(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Get name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Set name
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set description
     * @param description description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
