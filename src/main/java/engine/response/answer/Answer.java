package engine.response.answer;

import core.response.answers.BaseAnswer;
import core.response.answers.Meta;

/**
 * Class for create standard response object
 * @param <D> type of data field
 */
public class Answer<D> extends BaseAnswer {
    // Response data property
    private D data;

    /**
     * Constructor for create response by only status
     * @param status value of status property
     */
    public Answer(String status) {
        super(status);
    }

    /**
     * Constructor for create response by status & message
     * @param status value of status property
     * @param message value of message property
     */
    public Answer(String status, String message) {
        super(status, message);
    }

    /**
     * Constructor for create response by status, message & data properties
     * @param status value of status property
     * @param message value of message property
     * @param data value of data property
     */
    public Answer(String status, String message, D data) {
        super(status, message);
        this.data = data;
    }

    /**
     * Constructor for create response by all data
     * @param status value of status property
     * @param message value of message property
     * @param data value of data property
     * @param meta value of meta property
     */
    public Answer(String status, String message, D data, Meta meta) {
        super(status, message, meta);
        this.data = data;
    }

    /**
     * Getter for data property
     * @return value of data property
     */
    public D getData() {
        return data;
    }
}
