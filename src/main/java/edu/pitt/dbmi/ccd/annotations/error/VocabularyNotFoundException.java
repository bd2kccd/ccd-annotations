package edu.pitt.dbmi.ccd.annotations.error;

/**
 * Mark Silvis (marksilvis@pitt.edu)
 */
public class VocabularyNotFoundException extends NotFoundException {

    private static final String VOCAB = "Vocabulary";
    private static final String NAME = "name";
    private static final String ID = "id";

    public VocabularyNotFoundException(String name) {
        super(VOCAB, NAME, name);
    }

    public VocabularyNotFoundException(Long id) {
        super(VOCAB, ID, id);
    }
}
