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

import com.acme.jaxbee.XBeeException;
import com.acme.jaxbee.api.core.TxFrame;
import com.acme.jaxbee.api.core.TxFrameBuilder;

public class RemoteAtCommandBuilder implements TxFrameBuilder {
    private byte frameId;
    private long destinationAddress64;
    private short destinationAddress16;
    private byte options;
    private byte[] command;
    private byte[] parameter;

    /**
     * Instantiates a new Remote at command builder.
     */
    public RemoteAtCommandBuilder() {
        frameId = 0;
        destinationAddress64 = 0;
        destinationAddress16 = 0;
        options = 0;
        parameter = null;
    }

    /**
     * Sets frame id.
     *
     * @param frameId the frame id
     * @return the frame id
     */
    public RemoteAtCommandBuilder setFrameId(byte frameId) {
        this.frameId = frameId;
        return this;
    }

    /**
     * Sets destination address 64.
     *
     * @param destinationAddress64 the destination address 64
     * @return the destination address 64
     */
    public RemoteAtCommandBuilder setDestinationAddress64(long destinationAddress64) {
        this.destinationAddress64 = destinationAddress64;
        return this;
    }

    /**
     * Sets destination address 16.
     *
     * @param destinationAddress16 the destination address 16
     * @return the destination address 16
     */
    public RemoteAtCommandBuilder setDestinationAddress16(short destinationAddress16) {
        this.destinationAddress16 = destinationAddress16;
        return this;
    }

    /**
     * Sets options.
     *
     * @param options the options
     * @return the options
     */
    public RemoteAtCommandBuilder setOptions(byte options) {
        this.options = options;
        return this;
    }

    /**
     * Sets command.
     *
     * @param command the command
     * @return the command
     */
    public RemoteAtCommandBuilder setCommand(byte[] command) {
        this.command = command;
        return this;
    }

    /**
     * Sets parameter.
     *
     * @param parameter the parameter
     * @return the parameter
     */
    public RemoteAtCommandBuilder setParameter(byte[] parameter) {
        this.parameter = parameter;
        return this;
    }

    @Override
    public TxFrame build() throws XBeeException {
        if (command != null) {
            return new RemoteAtCommand()
                .setDestinationAddress64(destinationAddress64)
                .setDestinationAddress16(destinationAddress16)
                .setOptions(options)
                .setCommand(command)
                .setParameter(parameter)
                .setFrameId(frameId);
        } else {
            throw new XBeeException("Invalid AT command");
        }
    }
}
