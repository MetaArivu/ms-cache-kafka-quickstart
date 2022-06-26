package io.fusion.air.microservice.domain.exceptions;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class BusinessServiceException extends AbstractServiceException {

    /**
     * Business Service Exception
     * @param _msg
     * @param _e
     */
    public BusinessServiceException(String _msg, Exception _e) {
        super(_msg, _e);
    }
}
