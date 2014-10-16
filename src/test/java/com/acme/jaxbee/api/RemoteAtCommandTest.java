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
import com.acme.jaxbee.api.Commands;
import com.acme.jaxbee.api.RemoteAtCommand;
import com.acme.jaxbee.api.RemoteAtCommandBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RemoteAtCommandTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void frameType() throws XBeeException {

        RemoteAtCommand cmd = new RemoteAtCommand();

        assertThat(cmd.getFrameType(), is(equalTo(RemoteAtCommand.FRAME_TYPE)));
    }

    @Test
    public void frameId() throws XBeeException {
        final byte frameId = (byte) 0x01;

        RemoteAtCommand cmd = (RemoteAtCommand) new RemoteAtCommand()
                .setFrameId(frameId);

        assertThat(cmd.getFrameId(), is(equalTo(frameId)));
    }

    @Test
    public void command() throws XBeeException {
        final byte[] command = Commands.NI;

        RemoteAtCommand cmd = new RemoteAtCommand()
                .setCommand(command);

        assertThat(cmd.getCommand(), is(equalTo(command)));
    }

    @Test
    public void parameter() throws XBeeException {
        final byte[] parameter = new byte[]{0x01};
        RemoteAtCommand cmd = new RemoteAtCommand()
                .setParameter(parameter);

        assertThat(cmd.getParameter(), is(equalTo(parameter)));
    }


    @Test
    public void toBytes() throws XBeeException {
        final byte frameId = (byte) 0x01;
        final long addr64 = 0x0000000000000001;
        final short addr16 = 0x0001;
        final byte[] command = Commands.NI;
        final byte options = 0x02;
        final byte[] parameters = {(byte) 0x00};

        RemoteAtCommandBuilder builder = new RemoteAtCommandBuilder()
                .setFrameId(frameId)
                .setDestinationAddress64(addr64)
                .setDestinationAddress16(addr16)
                .setCommand(command)
                .setOptions(options)
                .setParameter(parameters);

        byte[] expected = {RemoteAtCommand.FRAME_TYPE, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x01, 0x02, 'N', 'I', 0x00};
        byte[] actual = builder.build().toBytes();

        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void exceptionWithNoCommand() throws XBeeException {
        exception.expect(XBeeException.class);
        RemoteAtCommand cmd = new RemoteAtCommand();
        cmd.toBytes();
    }

    @Test
    public void invalidCommand() throws XBeeException {
        exception.expect(XBeeException.class);
        new RemoteAtCommand()
                .setCommand(new byte[]{'I', 'N', 'V', 'A', 'L', 'I', 'D'});
    }
}
