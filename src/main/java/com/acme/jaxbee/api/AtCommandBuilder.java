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
import com.acme.jaxbee.api.core.TxFrameBuilder;

/**
 * The type At command builder.
 */
public class AtCommandBuilder implements TxFrameBuilder {
    private byte frameId;
    private byte[] command;
    private byte[] parameter;

    /**
     * Instantiates a new AT command builder.
     */
    public AtCommandBuilder() {
        frameId = 0;
        command = null;
        parameter = null;
    }

    /**
     * Sets frame id.
     *
     * @param frameId the frame id
     * @return the frame id
     */
    public AtCommandBuilder setFrameId(final byte frameId) {
        this.frameId = frameId;
        return this;
    }

    /**
     * Sets command.
     *
     * @param command the command
     * @return the command
     */
    public AtCommandBuilder setCommand(final byte[] command) {
        this.command = command;
        return this;
    }

    /**
     * Sets parameter.
     *
     * @param parameter the parameter
     * @return the parameter
     */
    public AtCommandBuilder setParameter(byte[] parameter) {
        this.parameter = parameter;
        return this;
    }

    @Override
    public TxFrame build() throws XBeeException {
        if (command != null) {
            return new AtCommand()
                .setCommand(command)
                .setParameter(parameter)
                .setFrameId(frameId);
        } else {
            throw new XBeeException("Invalid command");
        }
    }
}
