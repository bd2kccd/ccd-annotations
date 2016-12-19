package edu.pitt.dbmi.ccd.annotations.error;

/**
 * Mark Silvis (marksilvis@pitt.edu)
 */
public class AnnotationTargetNotFoundException extends NotFoundException {

    private static final String UPLOAD = "upload";
    private static final String ID = "id";

    public AnnotationTargetNotFoundException(Long id) {
        super(UPLOAD, ID, id);
    }
}
