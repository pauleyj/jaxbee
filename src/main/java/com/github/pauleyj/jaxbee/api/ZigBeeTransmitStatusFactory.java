package com.github.pauleyj.jaxbee.api;

import com.github.pauleyj.jaxbee.api.core.RxFrame;
import com.github.pauleyj.jaxbee.api.core.RxFrameFactory;

public class ZigBeeTransmitStatusFactory implements RxFrameFactory<ZigBeeTransmitStatus> {
    @Override
    public ZigBeeTransmitStatus newFrame() {
        return new ZigBeeTransmitStatus();
    }
}
