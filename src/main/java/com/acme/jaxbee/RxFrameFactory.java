package com.acme.jaxbee;

import com.acme.jaxbee.api.IRxFrameFactory;
import com.acme.jaxbee.api.RxFrame;
import com.acme.jaxbee.api.at.AtCommandResponse;
import com.acme.jaxbee.api.at.AtCommandResponseFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pauleyj on 10/4/14.
 */
public class RxFrameFactory {
    private final Map<Byte, IRxFrameFactory> factories;

    public RxFrameFactory() {
        factories = new HashMap<>();
        //
        // add basic rx frame factories
        addRxFrameFactory(AtCommandResponse.FRAME_TYPE, new AtCommandResponseFactory());
    }

    public RxFrame newRxFrameForApiId(final byte apiId) throws XBeeException {
        if(factories.containsKey(apiId)) {
            return factories.get(apiId).newFrame();
        } else {
            throw new XBeeException("Unknown API frame ID");
        }
    }

    public void addRxFrameFactory(final byte apiId, final IRxFrameFactory factory) {
        factories.put(apiId, factory);
    }
}
