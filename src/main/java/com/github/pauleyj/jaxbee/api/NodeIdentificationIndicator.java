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

public class NodeIdentificationIndicator extends RxFrame {
    private static final byte ADDRESS64_LENGTH = 0x08;
    private static final byte ADDRESS16_LENGTH = 0x02;
    private static final byte PROFILE_ID_LENGTH = 0x02;
    private static final byte MANUFACTURE_ID_LENGTH = 0x02;
    private static final byte BUFFER_ALLOCATION_CHUNK_SIZE = 0x04;

    public static byte FRAME_TYPE = (byte) 0x95;
    public static enum DeviceType {
        COORDINATOR,
        ROUTER,
        END_DEVICE,
        UNDEFINED;

        static DeviceType from(byte value) {
            switch (value) {
                case 0:
                    return COORDINATOR;
                case 1:
                    return ROUTER;
                case 2:
                    return END_DEVICE;
                default:
                    return UNDEFINED;
            }
        }
    }

    public static enum SourceEvent {
        NODE_IDENTIFICATION,
        JOIN_EVENT,
        POWER_CYCLE,
        UNDEFINED;

        static SourceEvent from(byte value) {
            switch (value) {
                case 1:
                    return NODE_IDENTIFICATION;
                case 2:
                    return JOIN_EVENT;
                case 3:
                    return POWER_CYCLE;
                default:
                    return UNDEFINED;
            }
        }
    }

    enum State {
        SOURCE_ADDR_64,
        SOURCE_ADDR_16,
        RECEIVE_OPTS,
        TX_SOURCE_ADDR_16,
        TX_SOURCE_ADDR_64,
        NI,
        PARENT_ADDR_16,
        DEVICE_TYPE,
        SOURCE_EVENT,
        PROFILE_ID,
        MANUFACTURE_ID,
        DONE
    }

    private long sourceAddress64;
    private short sourceAddress16;
    private byte receiveOptions;
    private short transmitterSourceAddress16;
    private long transmitterSourceAddress64;
    private ByteBuffer nodeIdentifier;
    private short parentAddress16;
    private byte deviceType;
    private byte sourceEvent;
    private ByteBuffer profileId;
    private ByteBuffer manufactureId;
    private State state;
    private byte index;

    public NodeIdentificationIndicator() {
        sourceAddress64 = Long.MIN_VALUE;
        sourceAddress16 = Short.MIN_VALUE;
        receiveOptions = Byte.MIN_VALUE;
        transmitterSourceAddress16 = Short.MIN_VALUE;
        transmitterSourceAddress64 = Long.MIN_VALUE;
        nodeIdentifier = null;
        parentAddress16 = Short.MIN_VALUE;
        deviceType = Byte.MIN_VALUE;
        sourceEvent = Byte.MIN_VALUE;
        profileId = ByteBuffer.allocate(2);
        manufactureId = ByteBuffer.allocate(2);
        state = State.SOURCE_ADDR_64;
        setFrameId((byte) 0x00);
        index = 0;
    }

    public long getSourceAddress64() {
        return sourceAddress64;
    }

    public short getSourceAddress16() {
        return sourceAddress16;
    }

    public byte getReceiveOptions() {
        return receiveOptions;
    }

    public short getTransmitterSourceAddress16() {
        return transmitterSourceAddress16;
    }

    public long getTransmitterSourceAddress64() {
        return transmitterSourceAddress64;
    }

    public byte[] getNodeIdentifier() {
        byte[] buffer = null;
        if (nodeIdentifier != null) {
            buffer = new byte[nodeIdentifier.position()];
            nodeIdentifier.rewind();
            nodeIdentifier.get(buffer, 0, buffer.length);
        }
        return buffer;
    }

    public short getParentAddress16() {
        return parentAddress16;
    }

    public DeviceType getDeviceType() {
        return DeviceType.from(deviceType);
    }

    public SourceEvent getSourceEvent() {
        return SourceEvent.from(sourceEvent);
    }

    public byte[] getProfileId() {
        return profileId.array();
    }

    public byte[] getManufactureId() {
        return manufactureId.array();
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
        if (State.SOURCE_ADDR_64 == state) {
            handleStateSourceAddr64(b);
        } else if (State.SOURCE_ADDR_16 == state) {
            handleStateSourceAddr16(b);
        } else if (State.RECEIVE_OPTS == state) {
            handleStateReceiveOpts(b);
        } else if (State.TX_SOURCE_ADDR_16 == state) {
            handleStateTxSourceAddr16(b);
        } else if (State.TX_SOURCE_ADDR_64 == state) {
            handleStateTxSourceAddr64(b);
        } else if (State.NI == state) {
            handleStateNodeIdentifier(b);
        } else if (State.PARENT_ADDR_16 == state) {
            handleStateParentAddr16(b);
        } else if (State.DEVICE_TYPE == state) {
            handleStateDeviceType(b);
        } else if (State.SOURCE_EVENT == state) {
            handleStateSourceEvent(b);
        } else if (State.PROFILE_ID == state) {
            handleStateProfileId(b);
        } else if (State.MANUFACTURE_ID == state) {
            handleStateManufactureId(b);
        } else {
            // no-op
        }
    }

    private void handleStateManufactureId(byte b) {
        manufactureId.put(b);
        if (++index == MANUFACTURE_ID_LENGTH) {
            index = 0;
            state = State.DONE;
        }
    }

    private void handleStateProfileId(byte b) {
        profileId.put(b);
        if (++index == PROFILE_ID_LENGTH) {
            index = 0;
            state = State.MANUFACTURE_ID;
        }
    }

    private void handleStateSourceEvent(byte b) {
        sourceEvent = b;
        index = 0;
        state = State.PROFILE_ID;
    }

    private void handleStateDeviceType(byte b) {
        deviceType = b;
        index = 0;
        state = State.SOURCE_EVENT;
    }

    private void handleStateParentAddr16(byte b) {
        parentAddress16 = ByteBuffer.allocate(ADDRESS16_LENGTH).putShort(0, parentAddress16).put(index++, b).getShort();
        if (index == ADDRESS16_LENGTH) {
            index = 0;
            state = State.DEVICE_TYPE;
        }
    }

    private void handleStateNodeIdentifier(byte b) {
        if (0x00 == b) {
            // this state is terminated by a "null" byte (0x00)
            index = 0;
            state = State.PARENT_ADDR_16;
        } else {
            if (nodeIdentifier == null) {
                nodeIdentifier = ByteBuffer.allocate(BUFFER_ALLOCATION_CHUNK_SIZE);
            }
            if (nodeIdentifier.position() == nodeIdentifier.capacity()) {
                final byte[] buffer = Arrays.copyOf(nodeIdentifier.array(), nodeIdentifier.capacity());
                nodeIdentifier = ByteBuffer.allocate(buffer.length + BUFFER_ALLOCATION_CHUNK_SIZE).put(buffer);
            }
            nodeIdentifier.put(b);
        }
    }

    private void handleStateTxSourceAddr64(byte b) {
        transmitterSourceAddress64 = ByteBuffer.allocate(ADDRESS64_LENGTH).putLong(0, transmitterSourceAddress64).put(index++, b).getLong();
        if (index == ADDRESS64_LENGTH) {
            index = 0;
            state = State.NI;
        }
    }

    private void handleStateTxSourceAddr16(byte b) {
        transmitterSourceAddress16 = ByteBuffer.allocate(ADDRESS16_LENGTH).putShort(0, transmitterSourceAddress16).put(index++, b).getShort();
        if (index == ADDRESS16_LENGTH) {
            index = 0;
            state = State.TX_SOURCE_ADDR_64;
        }
    }

    private void handleStateReceiveOpts(byte b) {
        receiveOptions = b;
        index = 0;
        state = State.TX_SOURCE_ADDR_16;
    }

    private void handleStateSourceAddr16(byte b) {
        sourceAddress16 = ByteBuffer.allocate(ADDRESS16_LENGTH).putShort(0, sourceAddress16).put(index++, b).getShort();
        if (index == ADDRESS16_LENGTH) {
            index = 0;
            state = State.RECEIVE_OPTS;
        }
    }

    private void handleStateSourceAddr64(byte b) {
        sourceAddress64 = ByteBuffer.allocate(ADDRESS64_LENGTH).putLong(0, sourceAddress64).put(index++, b).getLong();
        if (index == ADDRESS64_LENGTH) {
            index = 0;
            state = State.SOURCE_ADDR_16;
        }
    }

    @Override
    public String toString() {
        byte[] profileId = getProfileId();
        byte[] manufactureId = getManufactureId();
        return new StringBuffer()
            .append('{')
            .append("\"sourceAddress64\" : ").append(String.format("0x%08x", sourceAddress64)).append(", ")
            .append("\"sourceAddress16\" : ").append(String.format("0x%02x", sourceAddress16)).append(", ")
            .append("\"receiveOptions\" : ").append(String.format("0x%02x", receiveOptions)).append(", ")
            .append("\"txSourceAddress16\" : ").append(String.format("0x%02x", transmitterSourceAddress16)).append(", ")
            .append("\"txSourceAddress64\" : ").append(String.format("0x%08x", transmitterSourceAddress64)).append(", ")
            .append("\"nodeIdentifier\" : ").append('"').append(new String(getNodeIdentifier())).append('"').append(", ")
            .append("\"parentAddress16\" : ").append(String.format("0x%02x", parentAddress16)).append(", ")
            .append("\"deviceType\" : ").append('"').append(getDeviceType()).append('"').append(", ")
            .append("\"sourceEvent\" : ").append('"').append(getSourceEvent()).append('"').append(", ")
            .append("\"profileId\" : ").append('[').append(String.format("0x%02x, 0x%02x", profileId[0], profileId[1])).append(']').append(", ")
            .append("\"manufactureId\" : ").append('[').append(String.format("0x%02x, 0x%02x", manufactureId[0], manufactureId[1])).append(']')
            .append('}')
            .toString();
    }
}
