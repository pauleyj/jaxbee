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

import com.acme.jaxbee.XBeeException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class TestAtCommandBuilder {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void apiType() throws XBeeException {
        final byte[] command = Commands.NI;

        AtCommandBuilder builder = new AtCommandBuilder()
            .setCommand(command);

        assertThat(builder.build().getFrameType(), is(equalTo(AtCommand.FRAME_TYPE)));
    }

    @Test
    public void frameId() throws XBeeException {
        final byte frameId = (byte) 0x01;
        final byte[] command = Commands.NI;

        AtCommandBuilder builder = new AtCommandBuilder()
            .setFrameId(frameId)
            .setCommand(command);

        assertThat(builder.build().getFrameId(), is(equalTo(frameId)));
    }

    @Test
    public void command() throws XBeeException {
        final byte[] command = Commands.NI;

        AtCommandBuilder builder = new AtCommandBuilder()
            .setCommand(command);

        assertThat(((AtCommand) builder.build()).getCommand(), is(equalTo(command)));
    }

    @Test
    public void commandRequired() throws XBeeException {
        exception.expect(XBeeException.class);
        new AtCommandBuilder().build();
    }

    @Test
    public void parameters() throws XBeeException {
        final byte[] command = Commands.NI;
        final byte[] parameters = {(byte) 0x00};

        AtCommandBuilder builder = new AtCommandBuilder()
            .setCommand(command)
            .setParameter(parameters);

        assertThat(((AtCommand) builder.build()).getParameter(), is(equalTo(parameters)));
    }
}
