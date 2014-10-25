package com.acme.jaxbee.api;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

public class ZigBeeReceivePacketTest {

    private byte[] data;

    @Before
    public void setUp() throws Exception {
        data = new byte[]{
            (byte) 0x00, (byte) 0x13, (byte) 0xa2, (byte) 0x00,
            (byte) 0x40, (byte) 0x32, (byte) 0x03, (byte) 0xab, (byte) 0x53,
            (byte) 0x7a, (byte) 0x42, (byte) 0x48, (byte) 0x65, (byte) 0x6c,
            (byte) 0x6c, (byte) 0x6f, (byte) 0x20, (byte) 0x77, (byte) 0x6f,
            (byte) 0x72, (byte) 0x6c, (byte) 0x64, (byte) 0x21};
    }

    @Test
    public void testGetSourceAddress64() throws Exception {
        ZigBeeReceivePacket frame = new ZigBeeReceivePacket();
        for (byte b : data) {
            frame.receive(b);
        }
        assertThat(frame.getSourceAddress64(), is(equalTo(0x13a200403203abL)));
    }

    @Test
    public void testGetSourceAddress16() throws Exception {
        ZigBeeReceivePacket frame = new ZigBeeReceivePacket();
        for (byte b : data) {
            frame.receive(b);
        }
        assertThat(frame.getSourceAddress16(), is(equalTo((short) 0x537a)));
    }

    @Test
    public void testGetReceiveOptions() throws Exception {
        ZigBeeReceivePacket frame = new ZigBeeReceivePacket();
        for (byte b : data) {
            frame.receive(b);
        }
        assertThat(frame.getReceiveOptions(), is(equalTo((byte) 0x42)));
    }

    @Test
    public void testGetData() throws Exception {
        ZigBeeReceivePacket frame = new ZigBeeReceivePacket();
        for (byte b : data) {
            frame.receive(b);
        }
        assertThat(frame.getData(), is(equalTo("Hello world!".getBytes())));
    }

    @Test
    public void testGetFrameType() throws Exception {
        ZigBeeReceivePacket frame = new ZigBeeReceivePacket();
        for (byte b : data) {
            frame.receive(b);
        }
        assertThat(frame.getFrameType(), is(equalTo(ZigBeeReceivePacket.FRAME_TYPE)));
    }

    @Test
    public void testReceive() throws Exception {
        ZigBeeReceivePacket frame = new ZigBeeReceivePacket();
        assertThat(frame.getState(), is(equalTo(ZigBeeReceivePacket.State.ADDRESS64)));
        for(byte i=0; i<8; ++i){
            assertThat(frame.getState(), is(equalTo(ZigBeeReceivePacket.State.ADDRESS64)));
            frame.receive(i);
        }
        assertThat(frame.getState(), is(equalTo(ZigBeeReceivePacket.State.ADDRESS16)));

        for(byte i=0; i<2; ++i){
            assertThat(frame.getState(), is(equalTo(ZigBeeReceivePacket.State.ADDRESS16)));
            frame.receive(i);
        }
        assertThat(frame.getState(), is(equalTo(ZigBeeReceivePacket.State.RECEIVE_OPTIONS)));

        frame.receive((byte)0x00);
        assertThat(frame.getState(), is(equalTo(ZigBeeReceivePacket.State.RECEIVE_DATA)));

        for(byte i=0; i<10; ++i){
            assertThat(frame.getState(), is(equalTo(ZigBeeReceivePacket.State.RECEIVE_DATA)));
            frame.receive(i);
        }
        assertThat(frame.getState(), is(equalTo(ZigBeeReceivePacket.State.RECEIVE_DATA)));
    }

    @Test
    public void testToString() throws Exception {
        ZigBeeReceivePacket frame = new ZigBeeReceivePacket();
        for (byte b : data) {
            frame.receive(b);
        }
        assertThat(frame.toString(), is(notNullValue()));
    }
}