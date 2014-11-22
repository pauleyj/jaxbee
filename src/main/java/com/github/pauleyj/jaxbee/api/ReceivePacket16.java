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

import com.github.pauleyj.jaxbee.api.core.RxFrame;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ReceivePacket16 extends RxFrame {
    private static final byte ADDRESS16_LENGTH = 0x02;
    private static final byte BUFFER_ALLOCATION_CHUNK_SIZE = 0x04;
    public static final byte FRAME_TYPE = (byte) 0x81;

    enum State {
        ADDRESS16,
        RSSI,
        OPTIONS,
        RECEIVE_DATA,
    }

    private short sourceAddress16;
    private byte rssi;
    private byte options;
    private ByteBuffer data;
    private State state;
    private byte index;

    public ReceivePacket16() {
        sourceAddress16 = Short.MIN_VALUE;
        rssi = 0x00;
        options = Byte.MIN_VALUE;
        data = null;
        state = State.ADDRESS16;
    }

    public short getSourceAddress16() {
        return sourceAddress16;
    }

    public byte getRssi() {
        return rssi;
    }

    public byte getOptions() {
        return options;
    }

    public byte[] getData() {
        byte[] buffer = null;
        if (data != null) {
            buffer = new byte[data.position()];
            data.rewind();
            data.get(buffer, 0, buffer.length);
        }
        return buffer;
    }

    State getState() {
        return state;
    }

    @Override
    public byte getFrameType() {
        return FRAME_TYPE;
    }

    @Override
    public void receive(byte b) {
        if (State.ADDRESS16 == state) {
            handleStateAddress16(b);
        } else if (State.RSSI == state) {
            handleStateRssi(b);
        } else if (State.OPTIONS == state) {
            handleStateOptions(b);
        } else if (State.RECEIVE_DATA == state) {
            handleStateReceiveData(b);
        }
    }

    private void handleStateReceiveData(byte b) {
        if (data == null) {
            data = ByteBuffer.allocate(BUFFER_ALLOCATION_CHUNK_SIZE);
        }
        if (data.position() == data.capacity()) {
            final byte[] buffer = Arrays.copyOf(data.array(), data.capacity());
            data = ByteBuffer.allocate(buffer.length + BUFFER_ALLOCATION_CHUNK_SIZE).put(buffer);
        }
        data.put(b);
    }

    private void handleStateOptions(byte b) {
        options = b;
        index = 0;
        state = State.RECEIVE_DATA;
    }

    private void handleStateRssi(byte b) {
        rssi = b;
        index = 0;
        state = State.OPTIONS;
    }

    private void handleStateAddress16(byte b) {
        sourceAddress16 = ByteBuffer.allocate(ADDRESS16_LENGTH).putShort(0, sourceAddress16).put(index++, b).getShort();
        if (index == ADDRESS16_LENGTH) {
            index = 0;
            state = State.RSSI;
        }
    }

    @Override
    public String toString() {
        return new StringBuffer()
            .append('{')
            .append("\"sourceAddress16\" : ").append(String.format("0x%04x", sourceAddress16)).append(", ")
            .append("\"rssi\" : ").append(String.format("0x%02x", rssi)).append(", ")
            .append("\"options\" : ").append(options).append(", ")
            .append("\"data\" : ").append('"').append(new String(getData())).append('"')
            .append('}')
            .toString();
    }
}
