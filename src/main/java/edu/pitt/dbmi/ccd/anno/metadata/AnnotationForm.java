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
import org.hibernate.validator.constraints.NotBlank;
import edu.pitt.dbmi.ccd.db.entity.UserAccount;
import edu.pitt.dbmi.ccd.db.validation.Name;

/**
 * Group entity POST request
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
public class AnnotationForm {

    @NotNull
    private Long target;

    private Long parent = null;

    @NotBlank
    private String access;

    private String group = null;

    @NotBlank
    private String vocabulary;

    @Size(min=1)
    private Set<AnnotationDataForm> data;

    public AnnotationForm() { }

    public AnnotationForm(Long target, String access, String vocabulary, Set<AnnotationDataForm> data) {
        this.target = target;
        this.access = access;
        this.vocabulary = vocabulary;
        this.data = new HashSet<>(data);
    }

    public AnnotationForm(Long target, Long parent, String access, String vocabulary, Set<AnnotationDataForm> data) {
        this(target, access, vocabulary, data);
        this.parent = parent;
    }

    public AnnotationForm(Long target, String access, String group, String vocabulary, Set<AnnotationDataForm> data) {
        this(target, access, vocabulary, data);
        this.group = group;
    }

    public AnnotationForm(Long target, Long parent, String access, String group, String vocabulary, Set<AnnotationDataForm> data) {
        this(target, access, vocabulary, data);
        this.parent = parent;
        this.group = group;
    }

    public Long getTarget() {
        return target;
    }

    public void setTarget(Long target) {
        this.target = target;
    }

    public Long getParent() {
        return parent;
    }   

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(String vocabulary) {
        this.vocabulary = vocabulary;
    }

    public Set<AnnotationDataForm> getData() {
        return data;
    }

    public void setData(Set<AnnotationDataForm> data) {
        this.data = data;
    }
}
