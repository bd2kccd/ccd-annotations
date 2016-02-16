package edu.pitt.dbmi.ccd.anno.error;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


public final class ErrorResource {
    
    private final Date timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;

    /**
     * Constructor
     * @param  timestamp timestamp of error
     * @param  status    HTTP status code
     * @param  error     HTTP error
     * @param  message   error message (nullable)
     * @param  path      request path
     * @return           new error
     */
    public ErrorResource(Date timestamp, int status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    @JsonInclude(Include.NON_NULL)
    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public String getPath() {
        return path;
    }
}