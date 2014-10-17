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

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class RemoteAtCommandResponseTest {

    @Test
    public void testGetSourceAddress64() throws Exception {
        final byte[] data =
                {0x01, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x01, 0x02, 'N', 'I', 0x00, 't', 'e', 's', 't'};
        ByteBuffer buffer = ByteBuffer.wrap(data);
        RemoteAtCommandResponse response =
                new RemoteAtCommandResponse();
        for ( byte b : data ) {
            response.receive(b);
        }
        assertThat(response.getSourceAddress64(), is(equalTo(((ByteBuffer) buffer.position(1)).getLong())));
    }

    @Test
    public void testGetSourceAddress16() throws Exception {
        final byte[] data =
                {0x01, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x01, 0x02, 'N', 'I', 0x01, 't', 'e', 's', 't'};
        ByteBuffer buffer = ByteBuffer.wrap(data);
        RemoteAtCommandResponse response =
                new RemoteAtCommandResponse();
        for ( byte b : data ) {
            response.receive(b);
        }
        assertThat(response.getSourceAddress16(), is(equalTo(((ByteBuffer) buffer.position(9)).getShort())));
    }

    @Test
    public void testGetCommand() throws Exception {
        final byte[] data =
                {0x01, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x01, 0x02, 'N', 'I', 0x02, 't', 'e', 's', 't'};
        RemoteAtCommandResponse response =
                new RemoteAtCommandResponse();
        for ( byte b : data ) {
            response.receive(b);
        }
        assertThat(response.getCommand(), is(equalTo(Commands.NI)));
    }

    @Test
    public void testGetStatus() throws Exception {
        final byte[] data0 =
                {0x01, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x01, 0x02, 'N', 'I', 0x00, 't', 'e', 's', 't'};
        RemoteAtCommandResponse response =
                new RemoteAtCommandResponse();
        for ( byte b : data0 ) {
            response.receive(b);
        }
        assertThat(response.getStatus(), is(equalTo(RemoteAtCommandResponse.Status.from((byte) 0x00))));
    }

    @Test
    public void testStatusValues() {
        RemoteAtCommandResponse.Status status = RemoteAtCommandResponse.Status.from((byte) 0x00);
        assertThat(status, is(equalTo(RemoteAtCommandResponse.Status.OK)));

        status = RemoteAtCommandResponse.Status.from((byte) 0x01);
        assertThat(status, is(equalTo(RemoteAtCommandResponse.Status.ERROR)));

        status = RemoteAtCommandResponse.Status.from((byte) 0x02);
        assertThat(status, is(equalTo(RemoteAtCommandResponse.Status.INVALID_COMMAND)));

        status = RemoteAtCommandResponse.Status.from((byte) 0x03);
        assertThat(status, is(equalTo(RemoteAtCommandResponse.Status.INVALID_PARAMETER)));

        status = RemoteAtCommandResponse.Status.from((byte) 0x04);
        assertThat(status, is(equalTo(RemoteAtCommandResponse.Status.REMOTE_COMMAND_TX_FAILURE)));

        status = RemoteAtCommandResponse.Status.from((byte) 0x05);
        assertThat(status, is(equalTo(RemoteAtCommandResponse.Status.UNDEFINED)));
    }

    @Test
    public void testGetData() throws Exception {
        final byte[] data =
                {0x01, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x01, 0x02, 'N', 'I', 0x04, 't', 'e', 's', 't'};
        RemoteAtCommandResponse response =
                new RemoteAtCommandResponse();
        for ( byte b : data ) {
            response.receive(b);
        }
        assertThat(response.getData(), is(equalTo("test".getBytes())));
    }

    @Test
    public void testGetFrameType() throws Exception {
        RemoteAtCommandResponse response =
                new RemoteAtCommandResponse();
        assertThat(response.getFrameType(), is(equalTo(RemoteAtCommandResponse.FRAME_TYPE)));
    }

    @Test
    public void testReceive() throws Exception {
        final byte[] data =
                {0x01, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x01, 0x02, 'N', 'I', 0x05, 't', 'e', 's', 't'};
        RemoteAtCommandResponse response =
                new RemoteAtCommandResponse();
        assertThat(response.getState(), is(equalTo(RemoteAtCommandResponse.State.FRAME_ID)));

        response.receive((byte) 0x01);
        assertThat(response.getState(), is(equalTo(RemoteAtCommandResponse.State.ADDRESS64)));

        for ( int i = 0; i < 8; ++i ) {
            assertThat(response.getState(), is(equalTo(RemoteAtCommandResponse.State.ADDRESS64)));
            response.receive((byte) i);
        }
        assertThat(response.getState(), is(equalTo(RemoteAtCommandResponse.State.ADDRESS16)));

        for ( int i = 0; i < 2; ++i ) {
            assertThat(response.getState(), is(equalTo(RemoteAtCommandResponse.State.ADDRESS16)));
            response.receive((byte) i);
        }
        assertThat(response.getState(), is(equalTo(RemoteAtCommandResponse.State.AT_COMMAND)));

        for ( byte b : Commands.NI ) {
            assertThat(response.getState(), is(equalTo(RemoteAtCommandResponse.State.AT_COMMAND)));
            response.receive(b);
        }
        assertThat(response.getState(), is(equalTo(RemoteAtCommandResponse.State.COMMAND_STATUS)));

        response.receive((byte) 0x00);
        assertThat(response.getState(), is(equalTo(RemoteAtCommandResponse.State.COMMAND_DATA)));

        for ( byte b : "test".getBytes() ) {
            assertThat(response.getState(), is(equalTo(RemoteAtCommandResponse.State.COMMAND_DATA)));
            response.receive(b);
        }
        assertThat(response.getState(), is(equalTo(RemoteAtCommandResponse.State.COMMAND_DATA)));
    }

    @Test
    public void testToString() throws Exception {
        final byte[] data =
                {0x01, 0x04, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x01, 0x02, 'N', 'I', 0x00, 't', 'e', 's', 't'};
        RemoteAtCommandResponse response =
                new RemoteAtCommandResponse();
        for ( byte b : data ) {
            response.receive(b);
        }
        assertThat(response.toString(), is(notNullValue()));
    }
}