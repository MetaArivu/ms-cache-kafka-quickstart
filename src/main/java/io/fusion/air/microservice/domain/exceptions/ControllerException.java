package io.fusion.air.microservice.domain.exceptions;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class ControllerException extends AbstractServiceException {

    /**
     * Controller Exception
     * @param _msg
     * @param _e
     */
    public ControllerException(String _msg, Exception _e) {
        super(_msg, _e);
    }
}
