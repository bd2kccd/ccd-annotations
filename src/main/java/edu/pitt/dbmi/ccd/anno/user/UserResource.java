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

package edu.pitt.dbmi.ccd.anno.user;

import org.springframework.hateoas.core.Relation;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Link;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import edu.pitt.dbmi.ccd.db.entity.UserAccount;
import edu.pitt.dbmi.ccd.db.entity.Person;

/**
 * Combines UserAccount and Person entities into DTO representation
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Relation(value="user", collectionRelation="users")
@JsonPropertyOrder({"username", "name", "email", "description", "website"})
public final class UserResource extends ResourceSupport {

    // content
    private final String username;
    private final String firstName;
    private final String middleName;
    private final String lastName;
    private final String email;
    private final String description;
    private final String webPage;
    private final String picturePath;
    private final String role;

    /**
     * Empty constructor
     * @return UserResource with empty/null variables
     */
    protected UserResource() {
        this.username = "";
        this.firstName = "";
        this.middleName = "";
        this.lastName = "";
        this.email = "";
        this.description = "";
        this.webPage = "";
        this.picturePath = "";
        this.role = "";
    }

    /**
     * Constructor
     * @param  user content
     * @return      new UserResource
     */
    public UserResource(UserAccount user) {
        this.username = user.getUsername();

        final Person person = user.getPerson();
        this.firstName = person.getFirstName();
        this.middleName = person.getMiddleName();
        this.lastName = person.getLastName();
        this.email = person.getEmail();
        this.description = person.getDescription();
        this.webPage = person.getWebPage();
        this.picturePath = person.getPicturePath();
        this.role = user.getRole().getName();
    }

    /**
     * Constructor
     * @param user  content
     * @param links (optional) links to include
     * @return      new UserResource
     */
    public UserResource(UserAccount user, Link... links) {
        this(user);
        this.add(links);
    }

    /**
     * get username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * get first name
     * @return first name
     */
    @JsonIgnore
    public String getFirstName() {
        return firstName;
    }

    /**
     * get middle name
     * @return middle name
     */
    @JsonIgnore
    public String getMiddleName() {
        return middleName;
    }

    /**
     * get last name
     * @return last name
     */
    @JsonIgnore
    public String getLastName() {
        return lastName;
    }

    /**
     * get first name + (if exists) middle name + last name
     * @return name
     */
    @JsonProperty("name")
    public String getFullName() {
        if (middleName == null) {
            return String.format("%s %s", firstName, lastName);
        } else {
            return String.format("%s %s %s", firstName, middleName, lastName);
        }
    }

    /**
     * get email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * get description
     * @return return description
     */
    @JsonInclude(Include.NON_NULL)
    public String getDescription() {
        return description;
    }

    /**
     * get web site
     * @return web site
     */
    @JsonProperty("website")
    @JsonInclude(Include.NON_NULL)
    public String getWebPage() {
        return webPage;
    }

    /**
     * get picture path
     * @return picture path
     */
    @JsonIgnore
    public String getPicturePath() {
        return picturePath;
    }

    @JsonIgnore
    public String getRole() {
        return role;
    }
}
