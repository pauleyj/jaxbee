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

package com.acme.jaxbee.api.at;

import com.acme.jaxbee.XBee;
import com.acme.jaxbee.XBeeException;
import com.acme.jaxbee.api.TxFrame;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class RemoteAtCommand extends TxFrame {
    /**
     * The constant FRAME_TYPE.
     */
    public static final byte FRAME_TYPE = 0x17;
    private static final byte ADDRESS64_LENGTH = 0x08;
    private static final byte ADDRESS16_LENGTH = 0x02;
    private static final byte OPTIONS_LENGTH = 0x01;
    private static final byte AT_COMMAND_LENGTH = 0x02;


    private long destinationAddress64;
    private short destinationAddress16;
    private byte options;
    private byte[] command;
    private byte[] parameter;

    /**
     * Instantiates a new Remote at command.
     */
    public RemoteAtCommand() {
        destinationAddress64 = XBee.BROADCAST_ADDRESS_64;
        destinationAddress16 = XBee.BROADCAST_ADDRESS_16;
        options = 0x00;
        command = null;
        parameter = null;
    }

    /**
     * Gets destination address 64.
     *
     * @return the destination address 64
     */
    public long getDestinationAddress64() {
        return destinationAddress64;
    }

    /**
     * Sets destination address 64.
     *
     * @param destinationAddress64 the destination address 64
     * @return the destination address 64
     */
    public RemoteAtCommand setDestinationAddress64(long destinationAddress64) {
        this.destinationAddress64 = destinationAddress64;
        return this;
    }

    /**
     * Gets destination address 16.
     *
     * @return the destination address 16
     */
    public short getDestinationAddress16() {
        return destinationAddress16;
    }

    /**
     * Sets destination address 16.
     *
     * @param destinationAddress16 the destination address 16
     * @return the destination address 16
     */
    public RemoteAtCommand setDestinationAddress16(short destinationAddress16) {
        this.destinationAddress16 = destinationAddress16;
        return this;
    }

    /**
     * Gets options.
     *
     * @return the options
     */
    public byte getOptions() {
        return options;
    }

    /**
     * Sets options.
     *
     * @param options the options
     * @return the options
     */
    public RemoteAtCommand setOptions(byte options) {
        this.options = options;
        return this;
    }

    /**
     * Get command.
     *
     * @return the byte [ ]
     */
    public byte[] getCommand() {
        return command;
    }

    /**
     * Sets command.
     *
     * @param command the command
     * @return the command
     * @throws XBeeException the x bee exception
     */
    public RemoteAtCommand setCommand(byte[] command) throws XBeeException {
        if (command == null || command.length != AT_COMMAND_LENGTH) {
            throw new XBeeException("Invalid AT command");
        }
        this.command = Arrays.copyOf(command, command.length);
        return this;
    }

    /**
     * Get parameter.
     *
     * @return the byte [ ]
     */
    public byte[] getParameter() {
        return parameter;
    }

    /**
     * Sets parameter.
     *
     * @param parameter the parameter
     * @return the parameter
     */
    public RemoteAtCommand setParameter(byte[] parameter) {
        if (parameter != null && parameter.length > 0) {
            this.parameter = Arrays.copyOf(parameter, parameter.length);
        }
        return this;
    }

    private int getPrameterLength() {
        return (parameter != null) ? parameter.length : 0;
    }

    @Override
    public byte getFrameType() {
        return FRAME_TYPE;
    }

    @Override
    public byte[] toBytes() {
        // capacity = frame type + frame id + at command + parameter
        final int capacity = API_FRAME_TYPE_LENGTH + API_FRAME_ID_LENGTH + ADDRESS64_LENGTH + ADDRESS16_LENGTH + OPTIONS_LENGTH + AT_COMMAND_LENGTH + getPrameterLength();
        final ByteBuffer buffer =
            ByteBuffer.allocate(capacity)
                      .put(FRAME_TYPE)
                      .put(getFrameId())
                      .putLong(destinationAddress64)
                      .putShort(destinationAddress16)
                      .put(options)
                      .put(command);
        if (parameter != null) {
            buffer.put(parameter);
        }
        return buffer.array();
    }
}
