package com.acme.jaxbee.api;

import com.acme.jaxbee.XBeeException;

/**
 * Created by pauleyj on 10/4/14.
 */
public interface ITxFrameBuilder {
    TxFrame build() throws XBeeException;
}
