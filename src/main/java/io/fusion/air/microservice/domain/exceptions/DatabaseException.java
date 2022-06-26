package io.fusion.air.microservice.domain.exceptions;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class DatabaseException extends AbstractServiceException {

    /**
     * Database Exception
     * @param _msg
     * @param _e
     */
    public DatabaseException(String _msg, Exception _e) {
        super(_msg, _e);
    }
}
