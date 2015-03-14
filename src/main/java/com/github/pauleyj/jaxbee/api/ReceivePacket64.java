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

public class ReceivePacket64 extends RxFrame<ReceivePacket64> {
    private static final byte ADDRESS64_LENGTH = 0x08;
    private static final byte BUFFER_ALLOCATION_CHUNK_SIZE = 0x04;
    public static final byte FRAME_TYPE = (byte) 0x80;

    enum State {
        ADDRESS64,
        RSSI,
        OPTIONS,
        RECEIVE_DATA,
    }

    private long sourceAddress64;
    private byte rssi;
    private byte options;
    private ByteBuffer data;
    private State state;
    private byte index;

    public ReceivePacket64() {
        sourceAddress64 = Long.MIN_VALUE;
        rssi = 0x00;
        options = Byte.MIN_VALUE;
        data = null;
        state = State.ADDRESS64;
    }

    public long getSourceAddress64() {
        return sourceAddress64;
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
        if (State.ADDRESS64 == state) {
            handleStateAddress64(b);
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

    private void handleStateAddress64(byte b) {
        sourceAddress64 = ByteBuffer.allocate(ADDRESS64_LENGTH).putLong(0, sourceAddress64).put(index++, b).getLong();
        if (index == ADDRESS64_LENGTH) {
            index = 0;
            state = State.RSSI;
        }
    }

    @Override
    public String toString() {
        return new StringBuffer()
            .append('{')
            .append("\"sourceAddress64\" : ").append(String.format("0x%08x", sourceAddress64)).append(", ")
            .append("\"rssi\" : ").append(String.format("0x%02x", rssi)).append(", ")
            .append("\"options\" : ").append(options).append(", ")
            .append("\"data\" : ").append('"').append(new String(getData())).append('"')
            .append('}')
            .toString();
    }
}
