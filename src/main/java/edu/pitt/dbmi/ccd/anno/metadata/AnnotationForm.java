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
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import edu.pitt.dbmi.ccd.db.entity.Annotation;
import edu.pitt.dbmi.ccd.db.entity.AnnotationData;
import edu.pitt.dbmi.ccd.db.entity.UserAccount;
import edu.pitt.dbmi.ccd.db.entity.Access;
import edu.pitt.dbmi.ccd.db.entity.Group;
import edu.pitt.dbmi.ccd.db.entity.Vocabulary;

/**
 * Group entity POST request
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
public class AnnotationForm {

    @NotNull
    private UserAccount user;

    @NotNull
    private Access access;

    private Group group;

    @NotNull
    private Vocabulary vocabulary;

    @Size(min=1)
    private Set<AnnotationData> data;

    public AnnotationForm() { }

    public AnnotationForm(UserAccount user, Access access, Vocabulary vocabulary, Set<AnnotationData> data) {
        this.user = user;
        this.access = access;
        this.group = null;
        this.vocabulary = vocabulary;
        this.data = new HashSet<>(data);
    }

    public AnnotationForm(UserAccount user, Access access, Group group, Vocabulary vocabulary, Set<AnnotationData> data) {
        this.user = user;
        this.acces = access;
        this.group = group;
        this.vocabulary = vocabulary;
        this.data = new HashSet<>(data);
    }

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }

    public Access getAccess() {
        return access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Vocabulary getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(Vocabulary vocabulary) {
        this.vocabulary = vocabulary;
    }

    public Set<AnnotationData> getData() {
        return data;
    }

    public void setData(Set<AnnotationData> data) {
        this.data = data;
    }

    // @Override
    // public String toString() {
    //     // return String.format("Group [name: %s, description: %s]", name, description);
    //     return "";
    // }

    // private String formatName(String name) {
    //     return name.trim().replaceAll("\\s+", "_").replaceAll("_+", "_");
    // }
}
