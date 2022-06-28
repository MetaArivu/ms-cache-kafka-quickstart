package io.fusion.air.microservice.domain.exceptions;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class UnableToSaveException extends DatabaseException {

    /**
     * Database Exception
     * @param _msg
     * @param _e
     */
    public UnableToSaveException(String _msg, Exception _e) {
        super(_msg, _e);
    }
}
