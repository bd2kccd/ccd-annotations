package edu.pitt.dbmi.ccd.anno.user;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.core.Relation;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import edu.pitt.dbmi.ccd.db.entity.UserAccount;
import edu.pitt.dbmi.ccd.db.entity.Person;

/**
 * User DTO with self link
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
@Relation(value="user", collectionRelation="users")
public class UserResource extends ResourceSupport {

    // content
    private final String username;
    private final String firstName;
    private final String middleName;
    private final String lastName;
    private final String email;
    private final String description;
    private final String webPage;
    private final String picturePath;

    // links
    private final Link self;

    /**
     * Generate new UserResource with self link
     * @param  user content
     * @param  links links to include (optional)
     * @return       generated UserResource
     */
    public UserResource(UserAccount user, Link... links) {
        final Person person = user.getPerson();
        this.username = user.getUsername();
        this.firstName = person.getFirstName();
        this.middleName = person.getMiddleName();
        this.lastName = person.getLastName();
        this.email = person.getEmail();
        this.description = person.getDescription();
        this.webPage = person.getWebPage();
        this.picturePath = person.getPicturePath();
        this.self = linkTo(methodOn(UserController.class).getUser(username)).withSelfRel();
        this.add(self);
        this.add(new Link(webPage, "website"));
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
     * get first name + last name
     * @return name
     */
    public String getName() {
        final String first = getFirstName();
        final String last = getLastName();
        return String.format("%s %s", first, last);
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
    public String getDescription() {
        if (description == null) {
            return "";
        } else {
            return description;
        }
    }

    /**
     * get web site
     * @return web site
     */
    @JsonProperty("website")
    public String getWebPage() {
        if (webPage == null) {
            return "";
        } else {
            return webPage;
        }
    }

    /**
     * get picture path
     * @return picture path
     */
    @JsonIgnore
    public String getPicturePath() {
        return picturePath;
    }

    /**
     * get user link
     * @return link
     */
    @JsonIgnore
    public Link getLink() {
        return self;
    }
}
