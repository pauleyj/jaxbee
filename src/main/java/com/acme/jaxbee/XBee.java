package com.acme.jaxbee;

import com.acme.jaxbee.api.RxFrame;

/**
 * Created by pauleyj on 10/4/14.
 */
public class XBee {

    private RxFrameFactory rxFrameFactory;
    private RxFrame current;

    public XBee(final RxFrameFactory rxFrameFactory) {
        this.rxFrameFactory = rxFrameFactory;
    }


    public void rx(final byte b) {
        try {
            current = rxFrameFactory.newRxFrameForApiId(b);
        } catch (XBeeException e) {
            // notify listener of invalid frame rx
        }
    }
}
