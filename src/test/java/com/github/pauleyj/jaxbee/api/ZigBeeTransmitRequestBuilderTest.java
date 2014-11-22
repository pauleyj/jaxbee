package com.github.pauleyj.jaxbee.api;

import com.github.pauleyj.jaxbee.api.core.TxFrame;
import com.github.pauleyj.jaxbee.api.core.XBeeException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.ByteBuffer;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ZigBeeTransmitRequestBuilderTest {

    ZigBeeTransmitRequest frame;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {

        ZigBeeTransmitRequestBuilder builder = new ZigBeeTransmitRequestBuilder();
        builder.setDestinationAddress16((short) 0x1111);
        builder.setDestinationAddress64((long) 0x12345678);
        builder.setOptions((byte) 0xAA);
        builder.setFrameId((byte) 0x04);
        builder.setBroadcastRadius((byte) 0x11);
        builder.setData("Hello world!".getBytes());
        frame = (ZigBeeTransmitRequest)builder.build();
    }

    @Test
    public void testSetFrameId() throws Exception {

        assertThat(frame.getFrameId(), is(equalTo((byte)0x04)));

    }

    @Test
    public void testSetDestinationAddress64() throws Exception {

        assertThat(frame.getDestinationAddress64(), is(equalTo((long) 0x12345678)));

    }

    @Test
    public void testSetDestinationAddress16() throws Exception {

        assertThat(frame.getDestinationAddress16(), is(equalTo((short) 0x1111)));

    }

    @Test
    public void testSetBroadcastRadius() throws Exception {

        assertThat(frame.getBroadcastRadius(), is(equalTo((byte)0x11)));

    }

    @Test
    public void testSetOptions() throws Exception {

        assertThat(frame.getOptions(), is(equalTo((byte)0xAA)));

    }

    @Test
    public void testSetData() throws Exception {

        assertThat(frame.getData(), is(equalTo("Hello world!".getBytes())));

    }

    @Test
    public void testNullData() throws Exception {
        exception.expect(XBeeException.class);
        ZigBeeTransmitRequestBuilder zrb = new ZigBeeTransmitRequestBuilder();
        zrb.setData(null);

    }

    @Test
    public void testZeroLengthData() throws Exception {
        exception.expect(XBeeException.class);
        ZigBeeTransmitRequestBuilder zrb = new ZigBeeTransmitRequestBuilder();
        byte[] bytes = {};
        zrb.setData(bytes);

    }

    @Test
    public void testTooMuchData() throws Exception {
        exception.expect(XBeeException.class);
        ZigBeeTransmitRequestBuilder zrb = new ZigBeeTransmitRequestBuilder();
        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        zrb.setData(byteBuffer.array());

    }

    @Test
    public void testBuild() throws Exception {

        ZigBeeTransmitRequestBuilder builder = new ZigBeeTransmitRequestBuilder();
        builder.setDestinationAddress16((short) 0x1111);
        builder.setDestinationAddress64((long) 0x12345678);
        builder.setOptions((byte)0xAA);
        builder.setFrameId((byte)0x04);
        builder.setBroadcastRadius((byte) 0x11);
        builder.setData("Hello world!".getBytes());
        TxFrame txFrame = builder.build();
        assertThat(txFrame, is(instanceOf(ZigBeeTransmitRequest.class)));

    }
}