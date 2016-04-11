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

package edu.pitt.dbmi.ccd.anno.error;

import edu.pitt.dbmi.ccd.db.entity.Access;

/**
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
public final class AccessUpdateException extends RuntimeException {

    private static final String LOWER_ACCESS_MESSAGE = "Access %s cannot be set to more restrictive access level %s";
    private static final String NO_GROUP_MESSAGE = "Group required to set access to 'GROUP'";

    private final String message;

    public AccessUpdateException() {
        super();
        // backspace character to remove format literals
        this.message = String.format(LOWER_ACCESS_MESSAGE, "\b", "\b");
    }

    /**
     * Constructor
     */
    public AccessUpdateException(Access original, Access change) {
        super();
        this.message = String.format(LOWER_ACCESS_MESSAGE, original.getName(), change.getName());
    }

    /**
     * Constructor
     * @param  groupMissing return exception for missing group
     */
    public AccessUpdateException(boolean groupMissing) {
        super();
        if (groupMissing) {
            this.message = NO_GROUP_MESSAGE;
        } else {
            this.message = String.format(LOWER_ACCESS_MESSAGE, "\b", "\b");
        }
    }

    /**
     * Get message
     * @return message
     */
    @Override
    public String getMessage() {
        return message;
    }

}
