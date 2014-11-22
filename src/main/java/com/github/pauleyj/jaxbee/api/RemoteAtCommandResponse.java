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

public class RemoteAtCommandResponse extends RxFrame {
    private static final byte AT_COMMAND_LENGTH = 0x02;
    private static final byte ADDRESS64_LENGTH = 0x08;
    private static final byte ADDRESS16_LENGTH = 0x02;
    private static final byte BUFFER_ALLOCATION_CHUNK_SIZE = 0x04;

    enum State {
        FRAME_ID,
        ADDRESS64,
        ADDRESS16,
        AT_COMMAND,
        COMMAND_STATUS,
        COMMAND_DATA,
    }

    /**
     * The constant FRAME_TYPE.
     */
    public static final byte FRAME_TYPE = (byte) 0x97;

    /**
     * The enum Status.
     */
    public static enum Status {
        OK,
        ERROR,
        INVALID_COMMAND,
        INVALID_PARAMETER,
        REMOTE_COMMAND_TX_FAILURE,
        UNDEFINED;

        /**
         * From status.
         *
         * @param value the value
         * @return the status
         */
        static Status from(byte value) {
            switch (value) {
                case 0:
                    return OK;
                case 1:
                    return ERROR;
                case 2:
                    return INVALID_COMMAND;
                case 3:
                    return INVALID_PARAMETER;
                case 4:
                    return REMOTE_COMMAND_TX_FAILURE;
                default:
                    return UNDEFINED;
            }
        }
    }

    private byte index;
    private long sourceAddress64;
    private short sourceAddress16;
    private ByteBuffer command;
    private byte status;
    private ByteBuffer data;
    private State state;

    /**
     * Instantiates a new Remote at command response.
     */
    public RemoteAtCommandResponse() {
        index = 0;
        sourceAddress64 = Long.MIN_VALUE;
        sourceAddress16 = Short.MIN_VALUE;
        command = ByteBuffer.allocate(2);
        status = Byte.MIN_VALUE;
        data = null;
        state = State.FRAME_ID;
    }

    /**
     * Gets source address 64.
     *
     * @return the source address 64
     */
    public long getSourceAddress64() {
        return sourceAddress64;
    }

    /**
     * Gets source address 16.
     *
     * @return the source address 16
     */
    public short getSourceAddress16() {
        return sourceAddress16;
    }

    /**
     * Get command.
     *
     * @return the byte [ ]
     */
    public byte[] getCommand() {
        return command.array();
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public Status getStatus() {
        return Status.from(status);
    }

    /**
     * Get data.
     *
     * @return the byte [ ]
     */
    public byte[] getData() {
        byte[] buffer = null;
        if ( data != null ) {
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
        if ( State.FRAME_ID == state ) {
            handleStateFrameId(b);
        } else if ( State.ADDRESS64 == state ) {
            handleStateAddress64(b);
        } else if ( State.ADDRESS16 == state ) {
            handleStateAddress16(b);
        } else if ( State.AT_COMMAND == state ) {
            handleStateAtCommand(b);
        } else if ( State.COMMAND_STATUS == state ) {
            handleStateCommandStatus(b);
        } else if ( State.COMMAND_DATA == state ) {
            handleStateCommandData(b);
        }
    }

    private void handleStateCommandData(byte b) {
        if ( data == null ) {
            data = ByteBuffer.allocate(BUFFER_ALLOCATION_CHUNK_SIZE);
        }
        if ( data.position() == data.capacity() ) {
            final byte[] buffer = Arrays.copyOf(data.array(), data.capacity());
            data = ByteBuffer.allocate(buffer.length + BUFFER_ALLOCATION_CHUNK_SIZE).put(buffer);
        }
        data.put(b);
    }

    private void handleStateCommandStatus(byte b) {
        status = b;
        index = 0;
        state = State.COMMAND_DATA;
    }

    private void handleStateAtCommand(byte b) {
        command.put(b);
        if (++index == AT_COMMAND_LENGTH) {
            index = 0;
            state = State.COMMAND_STATUS;
        }
    }

    private void handleStateAddress16(byte b) {
        sourceAddress16 = ByteBuffer.allocate(ADDRESS16_LENGTH).putShort(0, sourceAddress16).put(index++, b).getShort();
        if (index == ADDRESS16_LENGTH) {
            index = 0;
            state = State.AT_COMMAND;
        }
    }

    private void handleStateAddress64(byte b) {
        sourceAddress64 = ByteBuffer.allocate(ADDRESS64_LENGTH).putLong(0, sourceAddress64).put(index++, b).getLong();
        if (index == ADDRESS64_LENGTH) {
            index = 0;
            state = State.ADDRESS16;
        }
    }

    private void handleStateFrameId(byte b) {
        setFrameId(b);
        index = 0;
        state = State.ADDRESS64;
    }

    @Override
    public String toString() {
        return new StringBuffer()
            .append('{')
            .append("\"frameId\" : ").append(getFrameId()).append(", ")
            .append("\"sourceAddress64\" : ").append(String.format("0x%08x", sourceAddress64)).append(", ")
            .append("\"sourceAddress16\" : ").append(String.format("0x%02x", sourceAddress16)).append(", ")
            .append("\"command\" : ").append('"').append(new String(command.array())).append('"').append(", ")
            .append("\"status\" : ").append('"').append(getStatus()).append('"').append(", ")
            .append("\"data\" : ").append('"').append(new String(getData())).append('"')
            .append('}')
            .toString();
    }
}
