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

/**
 * The type At command response.
 */
public class AtCommandResponse extends RxFrame<AtCommandResponse> {
    private static final byte AT_COMMAND_LENGTH = 0x02;
    private static final byte BUFFER_ALLOCATION_CHUNK_SIZE = 0x04;

    enum State {
        FRAME_ID,
        AT_COMMAND,
        COMMAND_STATUS,
        COMMAND_DATA
    }

    /**
     * The constant FRAME_TYPE.
     */
    public static final byte FRAME_TYPE = (byte) 0x88;

    /**
     * The enum Status.
     */
    public static enum Status {
        OK,
        ERROR,
        INVALID_COMMAND,
        INVALID_PARAMETER,
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
                default:
                    return UNDEFINED;
            }
        }
    }

    private byte index;
    private ByteBuffer command;
    private byte status;
    private ByteBuffer data;
    private State state;

    /**
     * Instantiates a new At command response.
     */
    public AtCommandResponse() {
        index = 0;
        command = ByteBuffer.allocate(2);
        status = Byte.MIN_VALUE;
        data = null;
        state = State.FRAME_ID;
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
        if (State.FRAME_ID == state) {
            handleStateFrameId(b);
        } else if (State.AT_COMMAND == state) {
            handleStateAtCommand(b);
        } else if (State.COMMAND_STATUS == state) {
            handleStateCommandStatus(b);
        } else if (State.COMMAND_DATA == state) {
            handleStateCommandData(b);
        }
    }

    private void handleStateCommandData(byte b) {
        if (data == null) {
            data = ByteBuffer.allocate(BUFFER_ALLOCATION_CHUNK_SIZE);
        }
        if (data.position() == data.capacity()) {
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

    private void handleStateFrameId(byte b) {
        setFrameId(b);
        index = 0;
        state = State.AT_COMMAND;
    }

    @Override
    public String toString() {
        return new StringBuffer()
            .append('{')
            .append("\"frame_id\" : ").append(getFrameId()).append(", ")
            .append("\"command\" : ").append('"').append(new String(command.array())).append('"').append(", ")
            .append("\"status\" : ").append('"').append(getStatus()).append('"').append(", ")
            .append("\"data\" : ").append('"').append(new String(getData())).append('"')
            .append('}')
            .toString();
    }
}
