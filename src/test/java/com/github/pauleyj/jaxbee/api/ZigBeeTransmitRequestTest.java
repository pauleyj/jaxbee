package com.github.pauleyj.jaxbee.api;

import com.github.pauleyj.jaxbee.api.core.TxFrame;
import com.github.pauleyj.jaxbee.api.core.XBeeException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.ByteBuffer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ZigBeeTransmitRequestTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testDestinationAddress64() throws Exception {

        ZigBeeTransmitRequest ztr = new ZigBeeTransmitRequest();
        ztr.setDestinationAddress64((long) 10);
        assertThat(ztr.getDestinationAddress64(), is(equalTo((long) 10)));

    }

    @Test
    public void testDestinationAddress16() throws Exception {

        ZigBeeTransmitRequest ztr = new ZigBeeTransmitRequest();
        ztr.setDestinationAddress16((short) 10);
        assertThat(ztr.getDestinationAddress16(), is(equalTo((short) 10)));

    }

    @Test
    public void testBroadcastRadius() throws Exception {

        ZigBeeTransmitRequest ztr = new ZigBeeTransmitRequest();
        ztr.setBroadcastRadius((byte) 0x10);
        assertThat(ztr.getBroadcastRadius(), is(equalTo((byte) 0x10)));

    }

    @Test
    public void testOptions() throws Exception {

        ZigBeeTransmitRequest ztr = new ZigBeeTransmitRequest();
        ztr.setOptions((byte) 0x10);
        assertThat(ztr.getOptions(), is(equalTo((byte) 0x10)));

    }

    @Test
    public void testData() throws Exception {

        byte[] data =  {0x00, 0x01, 0x02};
        ZigBeeTransmitRequest ztr = new ZigBeeTransmitRequest();
        ztr.setData(data);
        assertThat(ztr.getData(), is(equalTo(data)));

    }

    @Test
    public void testNullData() throws Exception {
        exception.expect(XBeeException.class);
        ZigBeeTransmitRequest ztr = new ZigBeeTransmitRequest();
        ztr.setData(null);
    }

    @Test
    public void testToLargeData() throws Exception {
        exception.expect(XBeeException.class);
        ZigBeeTransmitRequest ztr = new ZigBeeTransmitRequest();
        ByteBuffer bb = ByteBuffer.allocate(100);
        for (int i = 0; i < 100; i++) {
            bb.put((byte)i);
        }
        ztr.setData(bb.array());
    }

    @Test
    public void testGetFrameType() throws Exception {

        ZigBeeTransmitRequest ztr = new ZigBeeTransmitRequest();
        assertThat(ztr.getFrameType(), is(equalTo(ZigBeeTransmitRequest.FRAME_TYPE)));

    }

    @Test
    public void testToBytes() throws Exception {

        byte [] frame = {0x10, 0x04, 0x00, 0x13, (byte) 0xA2, 0x00, 0x40, 0x32, 0x03, (byte) 0xAB, (byte) 0xD9, (byte) 0xF0, 0x00, 0x00, 0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x20, 0x77, 0x6F, 0x72, 0x6C, 0x64, 0x21 };
        ZigBeeTransmitRequestBuilder zigBeeTransmitRequestBuilder =
                new ZigBeeTransmitRequestBuilder()
                        .setFrameId((byte) 0x04)
                        .setDestinationAddress64(0x13a200403203abL)
                        .setDestinationAddress16((short) 0xd9f0)
                        .setData("Hello world!".getBytes());
        TxFrame zigBeeTransmitRequest = zigBeeTransmitRequestBuilder.build();

        byte[] bytes = zigBeeTransmitRequest.toBytes();

        assertThat(bytes, is(equalTo(frame)));


    }
}