package io.fusion.air.microservice.domain.exceptions;

import org.hibernate.service.spi.ServiceException;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class InputDataException extends BusinessServiceException {

    /**
     * Business Service Exception
     * @param _msg
     * @param _e
     */
    public InputDataException(String _msg, Exception _e) {
        super(_msg, _e);
    }
}
