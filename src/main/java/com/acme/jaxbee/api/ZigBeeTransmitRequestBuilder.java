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

package com.acme.jaxbee.api;

import com.acme.jaxbee.api.core.TxFrame;
import com.acme.jaxbee.api.core.TxFrameBuilder;
import com.acme.jaxbee.api.core.XBeeException;

public class ZigBeeTransmitRequestBuilder implements TxFrameBuilder {

    private byte frameId;
    private long destinationAddress64;
    private short destinationAddress16;
    private byte broadcastRadius;
    private byte options;
    private byte[] data;

    public ZigBeeTransmitRequestBuilder() {
        frameId = 0;
        destinationAddress64 = XBeeConstants.BROADCAST_ADDRESS_64;
        destinationAddress16 = XBeeConstants.BROADCAST_ADDRESS_16;
        broadcastRadius = 0;
        options = 0;
        data = null;
    }
    public ZigBeeTransmitRequestBuilder setFrameId(byte frameId) {
        this.frameId = frameId;
        return this;
    }

    public ZigBeeTransmitRequestBuilder setDestinationAddress64(long destinationAddress64) {
        this.destinationAddress64 = destinationAddress64;
        return this;
    }

    public ZigBeeTransmitRequestBuilder setDestinationAddress16(short destinationAddress16) {
        this.destinationAddress16 = destinationAddress16;
        return this;
    }

    public ZigBeeTransmitRequestBuilder setBroadcastRadius(byte broadcastRadius) {
        this.broadcastRadius = broadcastRadius;
        return this;
    }

    public ZigBeeTransmitRequestBuilder setOptions(byte options) {
        this.options = options;
        return this;
    }

    public ZigBeeTransmitRequestBuilder setData(byte[] data) throws XBeeException {
        if (data == null || data.length == 0 || data.length > ZigBeeTransmitRequest.MAX_DATA_BYTES) {
            throw new XBeeException("Invalid data payload");
        }
        this.data = data;
        return this;
    }



    @Override
    public TxFrame build() throws XBeeException {
        return new ZigBeeTransmitRequest()
            .setDestinationAddress64(destinationAddress64)
            .setDestinationAddress16(destinationAddress16)
            .setBroadcastRadius(broadcastRadius)
            .setOptions(options)
            .setData(data)
            .setFrameId(frameId);
    }
}
