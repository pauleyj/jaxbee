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

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class NodeIdentificationIndicatorTest {
    private byte[] data;

    @Before
    public void setup() {
        data = new byte[]{
            (byte) 0x00, (byte) 0x13, (byte) 0xa2, (byte) 0x00, (byte) 0x40,
            (byte) 0x32, (byte) 0x03, (byte) 0xab, (byte) 0xe7, (byte) 0xbd,
            (byte) 0x42, (byte) 0xe7, (byte) 0xbd, (byte) 0x00, (byte) 0x13,
            (byte) 0xa2, (byte) 0x00, (byte) 0x40, (byte) 0x32, (byte) 0x03,
            (byte) 0xab, (byte) 0x45, (byte) 0x50, (byte) 0x30, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x03, (byte) 0xc1,
            (byte) 0x05, (byte) 0x10, (byte) 0x1e};
    }

    @Test
    public void testGetSourceAddress64() throws Exception {
        NodeIdentificationIndicator frame = new NodeIdentificationIndicator();
        for (byte b : data) {
            frame.receive(b);
        }
        assertThat(frame.getSourceAddress64(), is(equalTo(0x0013a200403203abL)));
    }

    @Test
    public void testGetSourceAddress16() throws Exception {
        NodeIdentificationIndicator frame = new NodeIdentificationIndicator();
        for (byte b : data) {
            frame.receive(b);
        }
        assertThat(frame.getSourceAddress16(), is(equalTo((short) 0xe7bd)));
    }

    @Test
    public void testGetReceiveOptions() throws Exception {
        NodeIdentificationIndicator frame = new NodeIdentificationIndicator();
        for (byte b : data) {
            frame.receive(b);
        }
        assertThat(frame.getReceiveOptions(), is(equalTo((byte) 0x42)));
    }

    @Test
    public void testGetTransmitterSourceAddress16() throws Exception {
        NodeIdentificationIndicator frame = new NodeIdentificationIndicator();
        for (byte b : data) {
            frame.receive(b);
        }
        assertThat(frame.getTransmitterSourceAddress16(), is(equalTo((short) 0xe7bd)));
    }

    @Test
    public void testGetTransmitterSourceAddress64() throws Exception {
        NodeIdentificationIndicator frame = new NodeIdentificationIndicator();
        for (byte b : data) {
            frame.receive(b);
        }
        assertThat(frame.getTransmitterSourceAddress64(), is(equalTo(0x0013a200403203abL)));
    }

    @Test
    public void testGetNodeIdentifier() throws Exception {
        NodeIdentificationIndicator frame = new NodeIdentificationIndicator();
        for (byte b : data) {
            frame.receive(b);
        }
        assertThat(frame.getNodeIdentifier(), is(equalTo("EP0".getBytes())));
    }

    @Test
    public void testGetParentAddress16() throws Exception {
        NodeIdentificationIndicator frame = new NodeIdentificationIndicator();
        for (byte b : data) {
            frame.receive(b);
        }
        assertThat(frame.getParentAddress16(), is(equalTo((short) 0x0000)));
    }

    @Test
    public void testGetDeviceType() throws Exception {
        NodeIdentificationIndicator frame = new NodeIdentificationIndicator();
        for (byte b : data) {
            frame.receive(b);
        }
        assertThat(frame.getDeviceType(), is(equalTo(NodeIdentificationIndicator.DeviceType.END_DEVICE)));
    }

    @Test
    public void testGetSourceEvent() throws Exception {
        NodeIdentificationIndicator frame = new NodeIdentificationIndicator();
        for (byte b : data) {
            frame.receive(b);
        }
        assertThat(frame.getSourceEvent(), is(equalTo(NodeIdentificationIndicator.SourceEvent.POWER_CYCLE)));
    }

    @Test
    public void testGetProfileId() throws Exception {
        NodeIdentificationIndicator frame = new NodeIdentificationIndicator();
        for (byte b : data) {
            frame.receive(b);
        }
        assertThat(frame.getProfileId(), is(equalTo(new byte[]{(byte) 0xC1, (byte) 0x05})));
    }

    @Test
    public void testGetManufactureId() throws Exception {
        NodeIdentificationIndicator frame = new NodeIdentificationIndicator();
        for (byte b : data) {
            frame.receive(b);
        }
        assertThat(frame.getManufactureId(), is(equalTo(new byte[]{(byte) 0x10, (byte) 0x1E})));
    }

    @Test
    public void testGetFrameType() throws Exception {
        NodeIdentificationIndicator frame = new NodeIdentificationIndicator();
        assertThat(frame.getFrameType(), is(equalTo(NodeIdentificationIndicator.FRAME_TYPE)));
    }

    @Test
    public void testReceive() throws Exception {
        NodeIdentificationIndicator frame = new NodeIdentificationIndicator();
        assertThat(frame.getState(), is(equalTo(NodeIdentificationIndicator.State.SOURCE_ADDR_64)));

        for (int i = 0; i < 8; ++i) {
            assertThat(frame.getState(), is(equalTo(NodeIdentificationIndicator.State.SOURCE_ADDR_64)));
            frame.receive((byte) i);
        }
        assertThat(frame.getState(), is(equalTo(NodeIdentificationIndicator.State.SOURCE_ADDR_16)));

        for (int i = 0; i < 2; ++i) {
            assertThat(frame.getState(), is(equalTo(NodeIdentificationIndicator.State.SOURCE_ADDR_16)));
            frame.receive((byte) i);
        }
        assertThat(frame.getState(), is(equalTo(NodeIdentificationIndicator.State.RECEIVE_OPTS)));

        frame.receive((byte) 0x00);
        assertThat(frame.getState(), is(equalTo(NodeIdentificationIndicator.State.TX_SOURCE_ADDR_16)));

        for (int i = 0; i < 2; ++i) {
            assertThat(frame.getState(), is(equalTo(NodeIdentificationIndicator.State.TX_SOURCE_ADDR_16)));
            frame.receive((byte) i);
        }
        assertThat(frame.getState(), is(equalTo(NodeIdentificationIndicator.State.TX_SOURCE_ADDR_64)));

        for (int i = 0; i < 8; ++i) {
            assertThat(frame.getState(), is(equalTo(NodeIdentificationIndicator.State.TX_SOURCE_ADDR_64)));
            frame.receive((byte) i);
        }
        assertThat(frame.getState(), is(equalTo(NodeIdentificationIndicator.State.NI)));

        for (byte b : "really long node identifier".getBytes()) {
            assertThat(frame.getState(), is(equalTo(NodeIdentificationIndicator.State.NI)));
            frame.receive(b);
        }
        assertThat(frame.getState(), is(equalTo(NodeIdentificationIndicator.State.NI)));

        frame.receive((byte) 0x00);
        assertThat(frame.getState(), is(equalTo(NodeIdentificationIndicator.State.PARENT_ADDR_16)));

        for (int i = 0; i < 2; ++i) {
            assertThat(frame.getState(), is(equalTo(NodeIdentificationIndicator.State.PARENT_ADDR_16)));
            frame.receive((byte) i);
        }
        assertThat(frame.getState(), is(equalTo(NodeIdentificationIndicator.State.DEVICE_TYPE)));

        frame.receive((byte) 0x00);
        assertThat(frame.getState(), is(equalTo(NodeIdentificationIndicator.State.SOURCE_EVENT)));

        frame.receive((byte) 0x01);
        assertThat(frame.getState(), is(equalTo(NodeIdentificationIndicator.State.PROFILE_ID)));

        for (int i = 0; i < 2; ++i) {
            assertThat(frame.getState(), is(equalTo(NodeIdentificationIndicator.State.PROFILE_ID)));
            frame.receive((byte) i);
        }
        assertThat(frame.getState(), is(equalTo(NodeIdentificationIndicator.State.MANUFACTURE_ID)));

        for (int i = 0; i < 2; ++i) {
            assertThat(frame.getState(), is(equalTo(NodeIdentificationIndicator.State.MANUFACTURE_ID)));
            frame.receive((byte) i);
        }
        assertThat(frame.getState(), is(equalTo(NodeIdentificationIndicator.State.DONE)));
    }

    @Test
    public void testToString() throws Exception {
        NodeIdentificationIndicator frame = new NodeIdentificationIndicator();
        for (byte b : data) {
            frame.receive(b);
        }
        assertThat(frame.toString(), is(notNullValue()));
    }

    @Test
    public void testDeviceTypeValues() {
        NodeIdentificationIndicator.DeviceType type = NodeIdentificationIndicator.DeviceType.from((byte) 0x00);
        assertThat(type, is(equalTo(NodeIdentificationIndicator.DeviceType.COORDINATOR)));

        type = NodeIdentificationIndicator.DeviceType.from((byte) 0x01);
        assertThat(type, is(equalTo(NodeIdentificationIndicator.DeviceType.ROUTER)));

        type = NodeIdentificationIndicator.DeviceType.from((byte) 0x02);
        assertThat(type, is(equalTo(NodeIdentificationIndicator.DeviceType.END_DEVICE)));

        type = NodeIdentificationIndicator.DeviceType.from((byte) 0x03);
        assertThat(type, is(equalTo(NodeIdentificationIndicator.DeviceType.UNDEFINED)));
    }

    @Test
    public void testSourceEventValues() {
        NodeIdentificationIndicator.SourceEvent type = NodeIdentificationIndicator.SourceEvent.from((byte) 0x01);
        assertThat(type, is(equalTo(NodeIdentificationIndicator.SourceEvent.NODE_IDENTIFICATION)));

        type = NodeIdentificationIndicator.SourceEvent.from((byte) 0x02);
        assertThat(type, is(equalTo(NodeIdentificationIndicator.SourceEvent.JOIN_EVENT)));

        type = NodeIdentificationIndicator.SourceEvent.from((byte) 0x03);
        assertThat(type, is(equalTo(NodeIdentificationIndicator.SourceEvent.POWER_CYCLE)));

        type = NodeIdentificationIndicator.SourceEvent.from((byte) 0x00);
        assertThat(type, is(equalTo(NodeIdentificationIndicator.SourceEvent.UNDEFINED)));
    }
}