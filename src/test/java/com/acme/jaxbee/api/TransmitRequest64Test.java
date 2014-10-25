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

import java.nio.ByteBuffer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class TransmitRequest64Test {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testDestinationAddress64() throws Exception {
        final long destinationAddress64 = 0x0123456789ABCDEFL;
        TransmitRequest64 frame = new TransmitRequest64()
            .setDestinationAddress64(destinationAddress64);
        assertThat(frame.getDestinationAddress64(), is(equalTo(destinationAddress64)));
    }

    @Test
    public void testOptions() throws Exception {
        final byte options = 0x01;
        TransmitRequest64 frame = new TransmitRequest64()
            .setOptions(options);
        assertThat(frame.getOptions(), is(equalTo(options)));
    }

    @Test
    public void testData() throws Exception {
        byte[] data = {0x00, 0x01, 0x02};
        TransmitRequest64 frame = new TransmitRequest64()
            .setData(data);
        assertThat(frame.getData(), is(equalTo(data)));
    }

    @Test
    public void testNullData() throws Exception {
        exception.expect(XBeeException.class);
        new TransmitRequest64()
            .setData(null);
    }

    @Test
    public void testTooMuchData() throws Exception {
        exception.expect(XBeeException.class);
        ByteBuffer buffer = ByteBuffer.allocate(TransmitRequest64.MAX_DATA_BYTES + 1);
        new TransmitRequest64()
            .setData(buffer.array());
    }

    @Test
    public void testLargeData() throws Exception {
        final String data = "Hello world! This is a bunch of data...";
        TransmitRequest64 frame = new TransmitRequest64()
            .setData(data.getBytes());
        assertThat(frame.getData(), is(equalTo(data.getBytes())));
    }

    @Test
    public void testFrameType() throws Exception {
        TransmitRequest64 frame = new TransmitRequest64();
        assertThat(frame.getFrameType(), is(equalTo(TransmitRequest64.FRAME_TYPE)));
    }

    @Test
    public void testToBytes() throws Exception {
        byte[] expected = {(byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x13, (byte) 0xa2, (byte) 0x00, (byte) 0x40, (byte) 0x32, (byte) 0x03, (byte) 0xab, (byte) 0x00, (byte) 0x48, (byte) 0x65, (byte) 0x6c, (byte) 0x6c, (byte) 0x6f};
        TransmitRequest64 frame = (TransmitRequest64)new TransmitRequest64()
            .setDestinationAddress64(0x13a200403203abL)
            .setData("Hello".getBytes())
            .setFrameId((byte)0x03);

        assertThat(frame.toBytes(), is(equalTo(expected)));
    }
}