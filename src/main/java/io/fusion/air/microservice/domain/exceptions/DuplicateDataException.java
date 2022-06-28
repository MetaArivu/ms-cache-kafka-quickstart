package io.fusion.air.microservice.domain.exceptions;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class DuplicateDataException extends DatabaseException {

    /**
     * Database Exception
     * @param _msg
     * @param _e
     */
    public DuplicateDataException(String _msg, Exception _e) {
        super(_msg, _e);
    }
}
