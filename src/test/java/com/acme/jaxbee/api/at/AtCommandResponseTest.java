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

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class AtCommandResponseTest {
    @Test
    public void testGetCommand() throws Exception {
        final byte[] data =
                {0x01, 'N', 'I', 0x04, 't', 'e', 's', 't'};
        AtCommandResponse response =
                new AtCommandResponse();
        for ( byte b : data ) {
            response.receive(b);
        }
        assertThat(response.getCommand(), is(equalTo(Commands.NI)));
    }

    @Test
    public void testGetStatus() throws Exception {
        final byte[] data =
                {0x01, 'N', 'I', 0x04, 't', 'e', 's', 't'};
        AtCommandResponse response =
                new AtCommandResponse();
        for ( byte b : data ) {
            response.receive(b);
        }
        assertThat(response.getStatus(), is(equalTo(AtCommandResponse.Status.from((byte) 0x04))));
    }

    @Test
    public void testStatusValues() {
        AtCommandResponse.Status status = AtCommandResponse.Status.from((byte) 0x00);
        assertThat(status, is(equalTo(AtCommandResponse.Status.OK)));

        status = AtCommandResponse.Status.from((byte) 0x01);
        assertThat(status, is(equalTo(AtCommandResponse.Status.ERROR)));

        status = AtCommandResponse.Status.from((byte) 0x02);
        assertThat(status, is(equalTo(AtCommandResponse.Status.INVALID_COMMAND)));

        status = AtCommandResponse.Status.from((byte) 0x03);
        assertThat(status, is(equalTo(AtCommandResponse.Status.INVALID_PARAMETER)));

        status = AtCommandResponse.Status.from((byte) 0x04);
        assertThat(status, is(equalTo(AtCommandResponse.Status.UNDEFINED)));
    }

    @Test
    public void testGetData() throws Exception {
        final byte[] data =
                {0x01, 'N', 'I', 0x04, 'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', '!'};
        AtCommandResponse response =
                new AtCommandResponse();
        for ( byte b : data ) {
            response.receive(b);
        }
        assertThat(response.getData(), is(equalTo("hello world!".getBytes())));
    }

    @Test
    public void testGetFrameType() throws Exception {
        final byte[] data =
                {0x01, 'N', 'I', 0x04, 't', 'e', 's', 't'};
        AtCommandResponse response =
                new AtCommandResponse();
        for ( byte b : data ) {
            response.receive(b);
        }
        assertThat(response.getFrameType(), is(equalTo(AtCommandResponse.FRAME_TYPE)));
    }

    @Test
    public void testReceive() throws Exception {
        AtCommandResponse response =
                new AtCommandResponse();
        assertThat(response.getState(), is(equalTo(AtCommandResponse.State.FRAME_ID)));

        response.receive((byte) 0x01);
        assertThat(response.getState(), is(equalTo(AtCommandResponse.State.AT_COMMAND)));

        for ( byte b : Commands.HV ) {
            assertThat(response.getState(), is(equalTo(AtCommandResponse.State.AT_COMMAND)));
            response.receive(b);
        }
        assertThat(response.getState(), is(equalTo(AtCommandResponse.State.COMMAND_STATUS)));

        response.receive((byte) 0x00);
        assertThat(response.getState(), is(equalTo(AtCommandResponse.State.COMMAND_DATA)));

        for ( byte b : "test".getBytes() ) {
            response.receive(b);
        }
        // never leaves this state
        assertThat(response.getState(), is(equalTo(AtCommandResponse.State.COMMAND_DATA)));
    }

    @Test
    public void testToString() throws Exception {
        final byte[] data =
                {0x01, 'N', 'I', 0x04, 't', 'e', 's', 't'};
        AtCommandResponse response =
                new AtCommandResponse();
        for ( byte b : data ) {
            response.receive(b);
        }
        assertThat(response.toString(), is(notNullValue()));
    }
}