package com.acme.jaxbee.api;

import com.acme.jaxbee.api.core.RxFrame;
import com.acme.jaxbee.api.core.RxFrameFactory;

public class ZigBeeTransmitStatusFactory implements RxFrameFactory {
    @Override
    public RxFrame newFrame() {
        return new ZigBeeTransmitStatus();
    }
}
