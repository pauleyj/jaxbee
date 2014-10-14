package com.acme.jaxbee.api.status;

import com.acme.jaxbee.api.RxFrame;
import com.acme.jaxbee.api.RxFrameFactory;

/**
 * Created by edwardsb on 10/14/14.
 */
public class TransmitStatusFactory implements RxFrameFactory {
    @Override
    public RxFrame newFrame() {
        return new TransmitStatus();
    }
}
