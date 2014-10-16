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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AtCommandTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void frameType() throws XBeeException {

        AtCommand cmd = new AtCommand();

        assertThat(cmd.getFrameType(), is(equalTo(AtCommand.FRAME_TYPE)));
    }

    @Test
    public void frameId() throws XBeeException {
        final byte frameId = (byte) 0x01;

        AtCommand cmd = (AtCommand) new AtCommand()
                .setFrameId(frameId);

        assertThat(cmd.getFrameId(), is(equalTo(frameId)));
    }

    @Test
    public void command() throws XBeeException {
        final byte[] command = Commands.NI;

        AtCommand cmd = new AtCommand()
                .setCommand(command);

        assertThat(cmd.getCommand(), is(equalTo(command)));
    }

    @Test
    public void parameter() throws XBeeException {
        final byte[] parameter = new byte[]{0x01};
        AtCommand cmd = new AtCommand()
                .setParameter(parameter);

        assertThat(cmd.getParameter(), is(equalTo(parameter)));
    }


    @Test
    public void toBytes() throws XBeeException {
        final byte frameId = (byte) 0x00;
        final byte[] command = Commands.NI;
        final byte[] parameter = new byte[]{0x04};

        AtCommand cmd = (AtCommand) new AtCommand()
                .setCommand(command)
                .setParameter(parameter)
                .setFrameId(frameId);

        assertThat(cmd.getFrameType(), is(equalTo(AtCommand.FRAME_TYPE)));
        assertThat(cmd.getFrameId(), is(equalTo(frameId)));
        assertThat(cmd.getCommand(), is(equalTo(command)));
        assertThat(cmd.getParameter(), is(equalTo(parameter)));

        // frame type, frame id, command, parameter
        final byte[] expected = {AtCommand.FRAME_TYPE, frameId, 'N', 'I', 0x04};
        final byte[] actual = cmd.toBytes();
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void exceptionWithNoCommand() throws XBeeException {
        exception.expect(XBeeException.class);
        AtCommand cmd = new AtCommand();
        cmd.toBytes();
    }

    @Test
    public void invalidCommand() throws XBeeException {
        exception.expect(XBeeException.class);
        new AtCommand()
                .setCommand(new byte[]{'I', 'N', 'V', 'A', 'L', 'I', 'D'});
    }
}
