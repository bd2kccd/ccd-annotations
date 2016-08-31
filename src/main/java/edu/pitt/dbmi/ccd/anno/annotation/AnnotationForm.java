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

package edu.pitt.dbmi.ccd.anno.annotation;

import java.util.List;
import java.util.ArrayList;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import org.hibernate.validator.constraints.NotBlank;
import edu.pitt.dbmi.ccd.db.entity.UserAccount;
import edu.pitt.dbmi.ccd.anno.annotation.data.AnnotationDataForm;
import edu.pitt.dbmi.ccd.db.validation.Name;

/**
 * Annotation entity POST request
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
public final class AnnotationForm {

    @NotNull(message="Must specify annotation target")
    private Long target;

    private Long parent;

    @NotBlank(message="Must specify id of access control level: [PUBLIC, GROUP, PRIVATE]")
    private Long access;

    private Long group;

    @NotBlank(message="Must specify vocabulary")
    private Long vocabulary;

    @Size(min=1, message="At least one data item required")
    private List<AnnotationDataForm> data;

    public AnnotationForm() { }

    public AnnotationForm(Long target, Long vocabulary, Long access, Long group, List<AnnotationDataForm> data) {
        this.target = target;
        this.vocabulary = vocabulary;
        this.access = access;
        this.group = group;
        this.data = new ArrayList<>(data);
        this.parent = null;
    }

    public AnnotationForm(Long target, Long vocabulary, Long access, Long group, List<AnnotationDataForm> data, Long parent) {
        this(target, vocabulary, access, group, data);
        this.parent = parent;
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

    public Long getAccess() {
        return access;
    }

    public void setAccess(Long access) {
        this.access = access;
    }

    public Long getGroup() {
        return group;
    }

    public void setGroup(Long group) {
        this.group = group;
    }

    public Long getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(Long vocabulary) {
        this.vocabulary = vocabulary;
    }

    public List<AnnotationDataForm> getData() {
        return data;
    }

    public void setData(List<AnnotationDataForm> data) {
        this.data = data;
    }
}
