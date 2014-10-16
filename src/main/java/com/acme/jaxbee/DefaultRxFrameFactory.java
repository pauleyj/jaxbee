/*
 * Copyright 2014 John C. Pauley
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.acme.jaxbee;

import com.acme.jaxbee.api.*;
import com.acme.jaxbee.api.core.RxFrame;
import com.acme.jaxbee.api.core.RxFrameFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Default rx frame factory.
 */
class DefaultRxFrameFactory implements XBeeRxFrameFactory {
    private final Map<Byte, RxFrameFactory> factories;

    /**
     * Instantiates a new Default rx frame factory.
     */
    public DefaultRxFrameFactory() {
        factories = new HashMap<>();
        //
        // add basic rx frame factories
        addRxFrameFactoryForApiId(AtCommandResponse.FRAME_TYPE, new AtCommandResponseFactory());
        addRxFrameFactoryForApiId(RemoteAtCommandResponse.FRAME_TYPE, new RemoteAtCommandResponseFactory());
        addRxFrameFactoryForApiId(ModemStatus.FRAME_TYPE, new ModemStatusFactory());
        addRxFrameFactoryForApiId(TxStatus.FRAME_TYPE, new TxStatusFactory());
        addRxFrameFactoryForApiId(NodeIdentificationIndicator.FRAME_TYPE, new NodeIdentificationIndicatorFactory());
        addRxFrameFactoryForApiId(ZigBeeTransmitStatus.FRAME_TYPE, new ZigBeeTransmitStatusFactory());
    }

    public RxFrame newRxFrameForApiId(final byte apiId) throws XBeeException {
        if(factories.containsKey(apiId)) {
            return factories.get(apiId).newFrame();
        } else {
            throw new XBeeException("Unknown API frame ID");
        }
    }

    public void addRxFrameFactoryForApiId(final byte apiId, final RxFrameFactory factory) {
        factories.put(apiId, factory);
    }
}
