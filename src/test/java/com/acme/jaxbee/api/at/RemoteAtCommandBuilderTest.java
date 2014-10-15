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
import static org.junit.Assert.assertThat;

public class RemoteAtCommandBuilderTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void frameType() throws XBeeException {
        final byte[] command = Commands.NI;

        RemoteAtCommandBuilder builder = new RemoteAtCommandBuilder()
            .setCommand(command);

        assertThat(builder.build().getFrameType(), is(equalTo(RemoteAtCommand.FRAME_TYPE)));
    }

    @Test
    public void frameId() throws XBeeException {
        final byte frameId = (byte) 0x01;
        final byte[] command = Commands.NI;

        RemoteAtCommandBuilder builder = new RemoteAtCommandBuilder()
            .setFrameId(frameId)
            .setCommand(command);

        assertThat(builder.build().getFrameId(), is(equalTo(frameId)));
    }

    @Test
    public void destination64() throws XBeeException {
        final long addr64 = 0x0000000001030307;
        final byte[] command = Commands.NI;

        RemoteAtCommandBuilder builder = new RemoteAtCommandBuilder()
            .setDestinationAddress64(addr64)
            .setCommand(command);

        assertThat(((RemoteAtCommand) builder.build()).getDestinationAddress64(), is(equalTo(addr64)));
    }

    @Test
    public void destination16() throws XBeeException {
        final short addr16 = 0x1337;
        final byte[] command = Commands.NI;

        RemoteAtCommandBuilder builder = new RemoteAtCommandBuilder()
            .setDestinationAddress16(addr16)
            .setCommand(command);

        assertThat(((RemoteAtCommand) builder.build()).getDestinationAddress16(), is(equalTo(addr16)));
    }

    @Test
    public void options() throws XBeeException {
        final byte[] command = Commands.NI;
        final byte options = 0x02;

        RemoteAtCommandBuilder builder = new RemoteAtCommandBuilder()
            .setOptions(options)
            .setCommand(command);

        assertThat(((RemoteAtCommand) builder.build()).getOptions(), is(equalTo(options)));
    }

    @Test
    public void command() throws XBeeException {
        final byte[] command = Commands.NI;

        RemoteAtCommandBuilder builder = new RemoteAtCommandBuilder()
            .setCommand(command);

        assertThat(((RemoteAtCommand) builder.build()).getCommand(), is(equalTo(command)));
    }

    @Test
    public void commandRequired() throws XBeeException {
        exception.expect(XBeeException.class);
        new RemoteAtCommandBuilder().build();
    }

    @Test
    public void parameters() throws XBeeException {
        final byte[] command = Commands.NI;
        final byte[] parameters = {(byte) 0x00};

        RemoteAtCommandBuilder builder = new RemoteAtCommandBuilder()
            .setCommand(command)
            .setParameter(parameters);

        assertThat(((RemoteAtCommand) builder.build()).getParameter(), is(equalTo(parameters)));
    }

    @Test
    public void toBytesWithNullCommand() throws XBeeException {
        exception.expect(XBeeException.class);
        new RemoteAtCommandBuilder().build();
    }
}
