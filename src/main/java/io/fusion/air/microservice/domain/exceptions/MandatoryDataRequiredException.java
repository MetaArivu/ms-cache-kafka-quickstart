package io.fusion.air.microservice.domain.exceptions;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class MandatoryDataRequiredException extends BusinessServiceException {

    /**
     * Business Service Exception
     * @param _msg
     * @param _e
     */
    public MandatoryDataRequiredException(String _msg, Exception _e) {
        super(_msg, _e);
    }
}
