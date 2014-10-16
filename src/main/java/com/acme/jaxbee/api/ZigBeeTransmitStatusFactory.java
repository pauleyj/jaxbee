package com.acme.jaxbee.api;

import com.acme.jaxbee.api.core.RxFrame;
import com.acme.jaxbee.api.core.RxFrameFactory;

/**
 * Created by edwardsb on 10/14/14.
 */
public class ZigBeeTransmitStatusFactory implements RxFrameFactory {
    @Override
    public RxFrame newFrame() {
        return new ZigBeeTransmitStatus();
    }
}
