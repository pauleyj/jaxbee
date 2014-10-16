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

import com.acme.jaxbee.XBee;
import com.acme.jaxbee.XBeeException;
import com.acme.jaxbee.api.core.TxFrame;
import com.acme.jaxbee.api.core.TxFrameBuilder;

public class TransmitRequest64Builder implements TxFrameBuilder {

    private byte frameId;
    private long destinationAddress64;
    private byte options;
    private byte[] data;

    public TransmitRequest64Builder() {
        frameId = 0;
        destinationAddress64 = XBee.BROADCAST_ADDRESS_64;
        options = 0;
        data = null;
    }

    public TransmitRequest64Builder setFrameId(byte frameId) {
        this.frameId = frameId;
        return this;
    }

    public TransmitRequest64Builder setDestinationAddress64(long destinationAddress64) {
        this.destinationAddress64 = destinationAddress64;
        return this;
    }

    public TransmitRequest64Builder setOptions(byte options) {
        this.options = options;
        return this;
    }

    public TransmitRequest64Builder setData(byte[] data) throws XBeeException {
        if (data == null || data.length == 0 || data.length > TransmitRequest64.MAX_DATA_BYTES) {
            throw new XBeeException("Invalid data payload");
        }
        this.data = data;
        return this;
    }

    @Override
    public TxFrame build() throws XBeeException {
        if (data == null || data.length == 0 || data.length > TransmitRequest64.MAX_DATA_BYTES) {
            throw new XBeeException("Invalid data payload");
        }
        return new TransmitRequest64()
            .setDestinationAddress64(destinationAddress64)
            .setOptions(options)
            .setData(data)
            .setFrameId(frameId);
    }
}
