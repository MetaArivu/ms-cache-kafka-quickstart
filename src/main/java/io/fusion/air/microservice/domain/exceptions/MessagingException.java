package io.fusion.air.microservice.domain.exceptions;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class MessagingException extends AbstractServiceException {

    /**
     * Messaging Exception
     * @param _msg
     * @param _e
     */
    public MessagingException(String _msg, Exception _e) {
        super(_msg, _e);
    }
}
