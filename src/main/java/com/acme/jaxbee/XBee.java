package com.acme.jaxbee;

import com.acme.jaxbee.api.RxFrame;
import com.acme.jaxbee.api.TxFrame;

import java.nio.ByteBuffer;

/**
 * Created by pauleyj on 10/4/14.
 */
public class XBee {
    private static final byte START_DELEMITER           = (byte)0x7E;
    private static final byte CHECKSUM_CONSTANT         = (byte)0xFF;
    private static final int API_FRAME_WRAPPER_LENGTH   = 0x04; // start delimiter 1 byte, length 2 bytes, checksum 1 byte

    private XBeeCommunications communications;
    private RxFrameFactory rxFrameFactory;
    private RxFrame current;

    public XBee(final XBeeCommunications communications) {
        this.communications = communications;
        this.rxFrameFactory = new RxFrameFactory();
    }

    public void tx(final TxFrame frame) {
        final byte[] data = frame.toBytes();
        final short length = (short)data.length;
        final ByteBuffer buffer =
            ByteBuffer.allocate(API_FRAME_WRAPPER_LENGTH + length)
            .put(START_DELEMITER)
            .putShort(length);
        byte checksum = 0;
        for(final byte b : data) {
            buffer.put(b);
            checksum += b;
        }
        checksum = (byte) ((CHECKSUM_CONSTANT - checksum) & CHECKSUM_CONSTANT);
        buffer.put(checksum);
        communications.onSend(buffer.array());
    }

    public void rx(final byte b) {
        try {
            current = rxFrameFactory.newRxFrameForApiId(b);
        } catch (XBeeException e) {
            // notify listener of invalid frame rx
        }
    }
}
