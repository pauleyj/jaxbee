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
import com.acme.jaxbee.XBeeCommunications;
import com.acme.jaxbee.XBeeException;
import com.acme.jaxbee.XBeeListener;
import com.acme.jaxbee.api.RxFrame;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;

public class TestAtCommand {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    public XBee               xbee;
    public XBeeCommunications communications;
    public XBeeListener       listener;

    @Before
    public void setupTest() {
        communications = spy(new XBeeCommunications() {
            @Override
            public void onSend(byte b) {

            }

            @Override
            public void onSend(byte[] buffer) {

            }

            @Override
            public void onFlushSendBuffer() {

            }
        });
        listener = spy(new XBeeListener() {
            @Override
            public void onReceiveFrame(RxFrame frame) {

            }
        });
        xbee = new XBee(communications, listener);
    }

    @Test
    public void testAtcommand() throws XBeeException {
        final byte frameId = (byte) 0x01;
        final byte[] command = Commands.NI;

        AtCommandBuilder builder = new AtCommandBuilder()
                .setFrameId(frameId)
                .setCommand(command);

        final byte[] expected = {0x08, frameId, (byte) 'N', (byte) 'I'};
        final byte[] actual = builder.build().toBytes();
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void testAtcommandWithoutFrameId() throws XBeeException {
        final byte frameId = (byte) 0x00;
        final byte[] command = Commands.NI;

        AtCommandBuilder builder = new AtCommandBuilder()
                .setCommand(command);

        final byte[] expected = {0x08, frameId, (byte) 'N', (byte) 'I'};
        final byte[] actual = builder.build().toBytes();
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void testAtcommandWithParam() throws XBeeException {
        final byte frameId = (byte) 0x01;
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

        final byte[] expected = {0x08, frameId, (byte) 'N', (byte) 'I', 0x04};
        final byte[] actual = cmd.toBytes();
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void testAtcommandWithDefaultFrameIdWithParam() throws XBeeException {
        final byte frameId = (byte) 0x00;
        final byte[] command = Commands.NI;
        final byte[] parameter = new byte[]{0x04};

        AtCommand cmd = new AtCommand()
                .setCommand(command)
                .setParameter(parameter);

        assertThat(cmd.getFrameType(), is(equalTo(AtCommand.FRAME_TYPE)));
        assertThat(cmd.getFrameId(), is(equalTo(frameId)));
        assertThat(cmd.getCommand(), is(equalTo(command)));
        assertThat(cmd.getParameter(), is(equalTo(parameter)));

        final byte[] expected = {0x08, frameId, 'N', 'I', 0x04};
        final byte[] actual = cmd.toBytes();
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void commandRequired() throws XBeeException {
        exception.expect(XBeeException.class);
        AtCommand cmd = new AtCommand();
        cmd.toBytes();
    }

    @Test
    public void invalidCommand() throws XBeeException {
        exception.expect(XBeeException.class);
        AtCommand cmd = new AtCommand()
                .setCommand(new byte[]{'I', 'N', 'V', 'A', 'L', 'I', 'D'});
    }
}
