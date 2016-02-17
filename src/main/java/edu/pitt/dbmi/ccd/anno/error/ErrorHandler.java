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

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.data.mapping.PropertyReferenceException;
import edu.pitt.dbmi.ccd.db.error.NotFoundException;

// logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mark Silvis (marksilvis@pitt.edu) 
 */
@ControllerAdvice
public final class ErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);

    private static final String FORBIDDEN_MESSAGE = "Insufficient permission";

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResource handleNotFoundException(NotFoundException ex, HttpServletRequest req) {
        LOGGER.error(ex.getMessage());
        return new ErrorResource(HttpStatus.NOT_FOUND, ex.getMessage(), req);
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResource handleForbiddenException(ForbiddenException ex, HttpServletRequest req) {
        LOGGER.info(ex.getMessage());
        return new ErrorResource(HttpStatus.FORBIDDEN, FORBIDDEN_MESSAGE, req);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResource handlePropertyReferenceException(PropertyReferenceException ex, HttpServletRequest req) {
        LOGGER.error(ex.getMessage());
        return new ErrorResource(HttpStatus.BAD_REQUEST, ex.getMessage(), req);
    }

}