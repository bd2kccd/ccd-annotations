package edu.pitt.dbmi.ccd.anno.error;

/**
 * Mark Silvis (marksilvis@pitt.edu)
 */
public class UploadNotFoundException extends NotFoundException {

    private static final String UPLOAD = "upload";
    private static final String ID = "id";
    public UploadNotFoundException(Long id) {
        super(UPLOAD, ID, id);
    }
}
