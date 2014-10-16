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

import com.acme.jaxbee.api.core.XBeeException;
import com.acme.jaxbee.api.core.TxFrame;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * The type At command.
 */
public class AtCommand extends TxFrame {
    /**
     * API identifier for AT command
     */
    public static final byte FRAME_TYPE = (byte) 0x08;
    private static final byte AT_COMMAND_LENGTH = (byte) 0x02;

    private byte[] command;
    private byte[] parameter;

    /**
     * Instantiates a new At command.
     */
    public AtCommand() {
        command = null;
        parameter = null;
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
    public AtCommand setCommand(final byte[] command) throws XBeeException {
        if (command == null || command.length != AT_COMMAND_LENGTH) {
            throw new XBeeException("Invalid AT command");
        }
        this.command = Arrays.copyOf(command, command.length);
        return this;
    }

    private int getPrameterLength() {
        return (parameter != null) ? parameter.length : 0;
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
    public AtCommand setParameter(byte[] parameter) {
        if (parameter != null && parameter.length > 0) {
            this.parameter = Arrays.copyOf(parameter, parameter.length);
        }
        return this;
    }

    @Override
    public byte getFrameType() {
        return FRAME_TYPE;
    }

    @Override
    public byte[] toBytes() throws XBeeException {
        if (command == null || command.length != AT_COMMAND_LENGTH) {
            throw new XBeeException("Invalid AT command");
        }
        // capacity = frame type + frame id + at command + parameter
        final int capacity = API_FRAME_TYPE_LENGTH + API_FRAME_ID_LENGTH + AT_COMMAND_LENGTH + getPrameterLength();
        final ByteBuffer buffer =
            ByteBuffer.allocate(capacity)
                      .put(FRAME_TYPE)
                      .put(getFrameId())
                      .put(command);
        if (parameter != null) {
            buffer.put(parameter);
        }
        return buffer.array();
    }
}
