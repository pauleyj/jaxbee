package com.acme.jaxbee;

import com.acme.jaxbee.api.IRxFrameFactory;
import com.acme.jaxbee.api.RxFrame;
import com.acme.jaxbee.api.at.AtCommandResponseFrameFactory;

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
        addFrameFactory((byte)0x88, new AtCommandResponseFrameFactory());
    }

    public RxFrame newRxFrameForApiId(final byte apiId) throws XBeeException {
        if(factories.containsKey(apiId)) {
            return factories.get(apiId).newFrame();
        } else {
            throw new XBeeException("Unknown API frame ID");
        }
    }

    public void addFrameFactory(final byte apiId, final IRxFrameFactory factory) {
        factories.put(apiId, factory);
    }
}
