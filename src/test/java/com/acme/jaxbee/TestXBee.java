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

package com.acme.jaxbee;

import com.acme.jaxbee.api.RxFrame;
import com.acme.jaxbee.api.at.AtCommandBuilder;
import com.acme.jaxbee.api.at.AtCommandResponse;
import com.acme.jaxbee.api.at.Commands;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;

public class TestXBee {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    public XBee xbee;
    public XBeeCommunications communications;
    public XBeeListener listener;

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
    public void atCommandResponse() throws XBeeException {
        final byte frameId = (byte)0x01;
        final byte[] command = Commands.NI;

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                // test results
                Object[] args = invocation.getArguments();
                assertThat(args.length, is(equalTo(1)));
                assertThat(args[0], is(instanceOf(RxFrame.class)));
                assertThat(args[0], is(instanceOf(AtCommandResponse.class)));

                AtCommandResponse response = (AtCommandResponse)args[0];
                assertThat(response.getFrameType(), is(equalTo(AtCommandResponse.FRAME_TYPE)));
                assertThat(response.getFrameId(), is(equalTo(frameId)));
                assertThat(response.getCommand(), is(equalTo(command)));
                assertThat(response.getStatus(), is(equalTo(AtCommandResponse.Status.OK)));
                return null;
            }
        }).when(listener).onReceiveFrame(any(RxFrame.class));

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                //
                // we are here in response to the below xbee.tx call of an ATNI
                // request.  respond with an appropriate ATNI response.
                byte[] response = {
                    (byte) 0x7e, (byte) 0x00, (byte) 0x18, (byte) 0x88,
                    (byte) 0x01, (byte) 0x4e, (byte) 0x49, (byte) 0x00,
                    (byte) 0x20, (byte) 0x5a, (byte) 0x69, (byte) 0x67,
                    (byte) 0x42, (byte) 0x65, (byte) 0x65, (byte) 0x20,
                    (byte) 0x43, (byte) 0x6f, (byte) 0x6f, (byte) 0x72,
                    (byte) 0x64, (byte) 0x69, (byte) 0x6e, (byte) 0x61,
                    (byte) 0x74, (byte) 0x6f, (byte) 0x72, (byte) 0xe5};
                xbee.rx(response);
                return null;
            }
        }).when(communications).onSend(any(byte[].class));

        AtCommandBuilder builder = new AtCommandBuilder()
            .setFrameId(frameId)
            .setCommand(command);
        xbee.tx(builder.build());
    }
}
