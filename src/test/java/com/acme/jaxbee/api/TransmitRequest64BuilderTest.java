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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.ByteBuffer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class TransmitRequest64BuilderTest {
    TransmitRequest64 frame;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {

        TransmitRequest64Builder builder = new TransmitRequest64Builder();
        builder.setDestinationAddress64((long) 0x12345678);
        builder.setOptions((byte) 0xAA);
        builder.setFrameId((byte) 0x04);
        builder.setData("Hello world!".getBytes());
        frame = (TransmitRequest64) builder.build();
    }

    @Test
    public void testSetFrameId() throws Exception {
        assertThat(frame.getFrameId(), is(equalTo((byte) 0x04)));
    }

    @Test
    public void testSetDestinationAddress64() throws Exception {
        assertThat(frame.getDestinationAddress64(), is(equalTo((long) 0x12345678)));
    }

    @Test
    public void testSetOptions() throws Exception {
        assertThat(frame.getOptions(), is(equalTo((byte) 0xAA)));
    }

    @Test
    public void testSetData() throws Exception {
        assertThat(frame.getData(), is(equalTo("Hello world!".getBytes())));
    }

    @Test
    public void testNullData() throws Exception {
        exception.expect(XBeeException.class);
        new TransmitRequest64Builder().setData(null);
    }

    @Test
    public void testZeroLengthData() throws Exception {
        exception.expect(XBeeException.class);
        new TransmitRequest64Builder().setData(new byte[]{});
    }

    @Test
    public void testToMuchData() throws Exception {
        exception.expect(XBeeException.class);
        ByteBuffer buffer = ByteBuffer.allocate(TransmitRequest64.MAX_DATA_BYTES + 1);
        new TransmitRequest64Builder().setData(buffer.array());
    }

    @Test
    public void testDataNotSet() throws Exception {
        exception.expect(XBeeException.class);
        new TransmitRequest64Builder().build();
    }
}