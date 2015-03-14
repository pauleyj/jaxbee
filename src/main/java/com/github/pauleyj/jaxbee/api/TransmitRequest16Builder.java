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

package com.github.pauleyj.jaxbee.api;

import com.github.pauleyj.jaxbee.api.core.TxFrame;
import com.github.pauleyj.jaxbee.api.core.TxFrameBuilder;
import com.github.pauleyj.jaxbee.api.core.XBeeException;

/**
 * Created by pauleyj on 10/21/14.
 */
public class TransmitRequest16Builder implements TxFrameBuilder<TransmitRequest16> {
    private byte frameId;
    private short destinationAddress16;
    private byte options;
    private byte[] data;

    public TransmitRequest16Builder() {
        destinationAddress16 = XBeeConstants.BROADCAST_ADDRESS_16;
        options = 0;
        data = null;
    }

    public TransmitRequest16Builder setFrameId(byte frameId) {
        this.frameId = frameId;
        return this;
    }

    public TransmitRequest16Builder setDestinationAddress16(short destinationAddress16) {
        this.destinationAddress16 = destinationAddress16;
        return this;
    }

    public TransmitRequest16Builder setOptions(byte options) {
        this.options = options;
        return this;
    }

    public TransmitRequest16Builder setData(byte[] data) throws XBeeException {
        if (data == null || data.length == 0 || data.length > TransmitRequest16.MAX_DATA_BYTES) {
            throw new XBeeException("Invalid data payload");
        }
        this.data = data;
        return this;
    }

    @Override
    public TransmitRequest16 build() throws XBeeException {
        if (data == null || data.length == 0 || data.length > TransmitRequest16.MAX_DATA_BYTES) {
            throw new XBeeException("Invalid data payload");
        }
        return new TransmitRequest16()
            .setDestinationAddress16(destinationAddress16)
            .setOptions(options)
            .setData(data)
            .setFrameId(frameId);
    }
}
