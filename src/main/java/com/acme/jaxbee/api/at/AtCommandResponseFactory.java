package com.acme.jaxbee.api.at;

import com.acme.jaxbee.api.IRxFrameFactory;
import com.acme.jaxbee.api.RxFrame;

/**
 * Created by pauleyj on 10/4/14.
 */
public class AtCommandResponseFactory implements IRxFrameFactory {
    @Override
    public RxFrame newFrame() {
        return new AtCommandResponse();
    }
}
