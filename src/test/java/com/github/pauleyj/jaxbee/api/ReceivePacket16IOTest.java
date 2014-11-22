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

package com.github.pauleyj.jaxbee.api;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class ReceivePacket16IOTest {

    @Test
    public void testGetSourceAddress16() throws Exception {
        final byte[] data =
            {0x01, 0x02, (byte) 0x28, 0x06, 'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
        ByteBuffer buffer = ByteBuffer.wrap(data);
        ReceivePacket16IO frame = new ReceivePacket16IO();
        for (byte b : data) {
            frame.receive(b);
        }
        assertThat(frame.getSourceAddress16(), is(equalTo(((ByteBuffer) buffer.position(0)).getShort())));
    }

    @Test
    public void testGetRssi() throws Exception {
        final byte[] data =
            {0x01, 0x02, (byte) 0x28, 0x06, 'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
        ReceivePacket16IO frame = new ReceivePacket16IO();
        for (byte b : data) {
            frame.receive(b);
        }
        assertThat(frame.getRssi(), is(equalTo((byte) 0x28)));
    }

    @Test
    public void testGetOptions() throws Exception {
        final byte[] data =
            {0x01, 0x02, (byte) 0x28, 0x06, 'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
        ReceivePacket16IO frame = new ReceivePacket16IO();
        for (byte b : data) {
            frame.receive(b);
        }
        assertThat(frame.getOptions(), is(equalTo((byte) 0x06)));
    }

    @Test
    public void testGetData() throws Exception {
        final byte[] data =
            {0x01, 0x02, (byte) 0x28, 0x06, 'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
        ReceivePacket16IO frame = new ReceivePacket16IO();
        for (byte b : data) {
            frame.receive(b);
        }
        assertThat(frame.getData(), is(equalTo("hello world".getBytes())));
    }

    @Test
    public void testGetFrameType() throws Exception {
        ReceivePacket16IO frame = new ReceivePacket16IO();
        assertThat(frame.getFrameType(), is(equalTo(ReceivePacket16IO.FRAME_TYPE)));
    }

    @Test
    public void testReceive() throws Exception {
        ReceivePacket16IO frame = new ReceivePacket16IO();
        assertThat(frame.getState(), is(equalTo(ReceivePacket16IO.State.ADDRESS16)));

        for (int i = 0; i < 2; ++i) {
            assertThat(frame.getState(), is(equalTo(ReceivePacket16IO.State.ADDRESS16)));
            frame.receive((byte) i);
        }
        assertThat(frame.getState(), is(equalTo(ReceivePacket16IO.State.RSSI)));

        frame.receive((byte) 0x28);
        assertThat(frame.getState(), is(equalTo(ReceivePacket16IO.State.OPTIONS)));

        frame.receive((byte) 0x06);
        assertThat(frame.getState(), is(equalTo(ReceivePacket16IO.State.RECEIVE_DATA)));

        for (byte b : "test".getBytes()) {
            assertThat(frame.getState(), is(equalTo(ReceivePacket16IO.State.RECEIVE_DATA)));
            frame.receive(b);
        }
        assertThat(frame.getState(), is(equalTo(ReceivePacket16IO.State.RECEIVE_DATA)));
    }

    @Test
    public void testToString() throws Exception {
        final byte[] data =
            {0x01, 0x02, (byte) 0x28, 0x06, 'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
        ReceivePacket16IO frame = new ReceivePacket16IO();
        for (byte b : data) {
            frame.receive(b);
        }
        assertThat(frame.toString(), is(notNullValue()));
    }
}