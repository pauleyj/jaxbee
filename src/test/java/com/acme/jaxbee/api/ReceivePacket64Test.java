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

public class ReceivePacket64Test {

    @Test
    public void testGetSourceAddress64() throws Exception {
        final byte[] data =
            {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x28, 0x06, 'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
        ByteBuffer buffer = ByteBuffer.wrap(data);
        ReceivePacket64 frame = new ReceivePacket64();
        for (byte b : data) {
            frame.receive(b);
        }
        assertThat(frame.getSourceAddress64(), is(equalTo(((ByteBuffer) buffer.position(0)).getLong())));
    }

    @Test
    public void testGetRssi() throws Exception {
        final byte[] data =
            {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x28, 0x06, 'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
        ReceivePacket64 frame = new ReceivePacket64();
        for (byte b : data) {
            frame.receive(b);
        }
        assertThat(frame.getRssi(), is(equalTo((byte) 0x28)));
    }

    @Test
    public void testGetOptions() throws Exception {
        final byte[] data =
            {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x28, 0x06, 'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
        ReceivePacket64 frame = new ReceivePacket64();
        for (byte b : data) {
            frame.receive(b);
        }
        assertThat(frame.getOptions(), is(equalTo((byte) 0x06)));
    }

    @Test
    public void testGetData() throws Exception {
        final byte[] data =
            {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x28, 0x06, 'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
        ReceivePacket64 frame = new ReceivePacket64();
        for (byte b : data) {
            frame.receive(b);
        }
        assertThat(frame.getData(), is(equalTo("hello world".getBytes())));
    }

    @Test
    public void testGetFrameType() throws Exception {
        ReceivePacket64 frame = new ReceivePacket64();
        assertThat(frame.getFrameType(), is(equalTo(ReceivePacket64.FRAME_TYPE)));
    }

    @Test
    public void testReceive() throws Exception {
        ReceivePacket64 frame = new ReceivePacket64();
        assertThat(frame.getState(), is(equalTo(ReceivePacket64.State.ADDRESS64)));

        for (int i = 0; i < 8; ++i) {
            assertThat(frame.getState(), is(equalTo(ReceivePacket64.State.ADDRESS64)));
            frame.receive((byte) i);
        }
        assertThat(frame.getState(), is(equalTo(ReceivePacket64.State.RSSI)));

        frame.receive((byte) 0x28);
        assertThat(frame.getState(), is(equalTo(ReceivePacket64.State.OPTIONS)));

        frame.receive((byte) 0x06);
        assertThat(frame.getState(), is(equalTo(ReceivePacket64.State.RECEIVE_DATA)));

        for (byte b : "test".getBytes()) {
            assertThat(frame.getState(), is(equalTo(ReceivePacket64.State.RECEIVE_DATA)));
            frame.receive(b);
        }
        assertThat(frame.getState(), is(equalTo(ReceivePacket64.State.RECEIVE_DATA)));
    }

    @Test
    public void testToString() throws Exception {
        final byte[] data =
            {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x28, 0x06, 'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
        ReceivePacket64 frame = new ReceivePacket64();
        for (byte b : data) {
            frame.receive(b);
        }
        assertThat(frame.toString(), is(notNullValue()));
    }
}