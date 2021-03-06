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
import com.github.pauleyj.jaxbee.api.core.XBeeException;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class TransmitRequest16 extends TxFrame<TransmitRequest16> {
    public static final byte FRAME_TYPE = 0x01;
    public static final int MAX_DATA_BYTES = 100;
    private static final byte ADDRESS16_LENGTH = 0x02;
    private static final byte OPTIONS_LENGTH = 0x01;

    private short destinationAddress16;
    private byte options;
    private byte[] data;

    public TransmitRequest16() {
        destinationAddress16 = XBeeConstants.BROADCAST_ADDRESS_16;
        options = 0;
        data = null;
    }

    public short getDestinationAddress16() {
        return destinationAddress16;
    }

    public TransmitRequest16 setDestinationAddress16(short destinationAddress16) {
        this.destinationAddress16 = destinationAddress16;
        return this;
    }

    public byte getOptions() {
        return options;
    }

    public TransmitRequest16 setOptions(byte options) {
        this.options = options;
        return this;
    }

    public byte[] getData() {
        return data;
    }

    public TransmitRequest16 setData(byte[] data) throws XBeeException {
        if (data == null) {
            throw new XBeeException("Null data not allowed");
        }
        if (data.length > MAX_DATA_BYTES) {
            throw new XBeeException("Data exceeds max buffer length of " + MAX_DATA_BYTES + " bytes");
        }
        this.data = Arrays.copyOf(data, data.length);
        return this;
    }

    private int getDataBytesSize() {
        int length = 0;
        if (data != null) {
            length = data.length;
        }
        return length;
    }

    @Override
    public byte getFrameType() {
        return FRAME_TYPE;
    }

    @Override
    public byte[] toBytes() {
        final int capacity = API_FRAME_TYPE_LENGTH + API_FRAME_ID_LENGTH + ADDRESS16_LENGTH + OPTIONS_LENGTH + getDataBytesSize();
        final ByteBuffer buffer =
            ByteBuffer.allocate(capacity)
                      .put(FRAME_TYPE)
                      .put(getFrameId())
                      .putShort(destinationAddress16)
                      .put(options);
        if (data != null && data.length > 0) {
            buffer.put(data);
        }
        return buffer.array();
    }
}
