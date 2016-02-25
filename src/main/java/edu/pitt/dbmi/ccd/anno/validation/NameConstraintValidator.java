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

package edu.pitt.dbmi.ccd.anno.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Entity name validation
 * 
 * @author Mark Silvis (marksilvis@pitt.edu)
 */
public class NameConstraintValidator implements ConstraintValidator<Name, String> {

    private static final String NAME_PATTERN = "[a-zA-Z][a-zA-Z0-9\\-_]+[a-zA-Z0-9]";

    @Override
    public void initialize(Name String) { }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext ctx) {
        if (name == null) {
            return false;
        } else {
            return name.matches(NAME_PATTERN);
        }
    }
}