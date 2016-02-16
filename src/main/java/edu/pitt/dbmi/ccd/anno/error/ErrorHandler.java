package edu.pitt.dbmi.ccd.anno.error;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import edu.pitt.dbmi.ccd.db.error.NotFoundException;

// logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class handles the exceptions thrown by our REST API.
 *
 * @author Petri Kainulainen
 */
@ControllerAdvice
public final class ErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);

    // @Autowired
    // public ErrorHandler(MessageSource messageSource) {
    //     this.messageSource = messageSource;
    // }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResource handleNotFound(HttpServletRequest req, NotFoundException ex) {
        LOGGER.error(ex.toString());

        return new ErrorResource(new Date(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), ex.toString(), req.getRequestURI());
    }
}