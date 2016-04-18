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

import javax.servlet.http.HttpServletRequest;
import edu.pitt.dbmi.ccd.db.entity.Group;
import edu.pitt.dbmi.ccd.db.entity.UserAccount;

/**
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
public final class NotAMemberException extends RuntimeException {
    private static final String MESSAGE = "User %s must be a member of Group %s to be upgraded to moderator";

    private final String username;
    private final String group;

    public NotAMemberException(Group group, UserAccount user) {
        super();
        this.group = group.getName();
        this.username = user.getUsername();
    }

    @Override
    public String getMessage() {
        return String.format(MESSAGE, username, group);
    }
}