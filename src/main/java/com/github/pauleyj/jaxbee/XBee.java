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

package com.github.pauleyj.jaxbee;

import com.github.pauleyj.jaxbee.api.core.*;

import java.nio.ByteBuffer;

/**
 * The type X bee.
 */
public class XBee {
    private static final byte START_DELIMITER = (byte) 0x7E;
    private static final byte API_FRAME_WRAPPER_LENGTH = 0x04; // start delimiter 1 byte, length 2 bytes, checksum 1 byte
    private static final byte XBEE_NUMBER_OF_SIZE_BYTES = 0x02;
    private static final byte XBEE_FRAME_VALID_CHECKSUM = (byte) 0xFF;

    // xbee state machine states
    private enum XBeeState {
        /**
         * The STATE_FRAME_WAIT_START.
         */
        STATE_FRAME_WAIT_START, //!< Waiting for the delimiter (start)
        /**
         * The STATE_FRAME_DATA_LENGTH.
         */
        STATE_FRAME_DATA_LENGTH, //!< Reading the length of the packet
        /**
         * The STATE_FRAME_API_IDENTIFIER.
         */
        STATE_FRAME_API_IDENTIFIER, //!< Reading the type of message
        /**
         * The STATE_FRAME_DATA.
         */
        STATE_FRAME_DATA, // Reading frame data
        /**
         * The STATE_FRAME_CHECKSUM_VALIDATION.
         */
        STATE_FRAME_CHECKSUM_VALIDATION // Checksum
    }

    // xbee serial i/o interface
    private XBeeCommunications communications;
    // rx frame listener
    private XBeeListener       listener;
    // the rx frame factory
    private XBeeRxFrameFactory rxFrameFactory;
    // current rx frame
    private RxFrame            current;
    // current state of state machine
    private XBeeState          state;
    // size of data frame
    private short              rxFrameDataSize;
    // current index of frame data size
    private byte               rxFrameDataSizeByteIndex;
    // current index of frame data
    private short              rxFrameDataIndex;
    // calculated checksum
    private byte               rxFrameDataChecksum;

    /**
     * Instantiates a new X bee.
     *
     * @param communications the communications
     * @param listener the listener
     */
    public XBee(final XBeeCommunications communications, final XBeeListener listener) {
        this(communications, listener, new DefaultRxFrameFactory());
    }

    /**
     * Instantiates a new X bee.
     *
     * @param communications the communications
     * @param listener the listener
     * @param factory the factory
     */
    public XBee(final XBeeCommunications communications, final XBeeListener listener, XBeeRxFrameFactory factory) {
        this.communications = communications;
        this.listener = listener;
        this.rxFrameFactory = factory;
        state = XBeeState.STATE_FRAME_WAIT_START;
    }

    /**
     * Add rx frame factory for api id.
     *
     * @param apiId the api id
     * @param factory the factory
     */
    public void addRxFrameFactoryForApiId(final byte apiId, final RxFrameFactory factory) {
        rxFrameFactory.addRxFrameFactoryForApiId(apiId, factory);
    }

    /**
     * Tx void.
     *
     * @param frame the frame
     * @throws XBeeException the x bee exception
     */
    public void tx(final TxFrame frame) throws XBeeException {
        final byte[] data = frame.toBytes();
        final short length = (short) data.length;
        final ByteBuffer buffer =
                ByteBuffer.allocate(API_FRAME_WRAPPER_LENGTH + length)
                        .put(START_DELIMITER)
                        .putShort(length);
        byte checksum = 0;
        for ( final byte b : data ) {
            buffer.put(b);
            checksum += b;
        }
        checksum = (byte) (XBEE_FRAME_VALID_CHECKSUM - checksum);
        buffer.put(checksum);
        communications.onSend(buffer.array());
        communications.onFlushSendBuffer();
    }


    /**
     * Rx void.
     *
     * @param buffer the buffer
     */
    public void rx(final byte[] buffer) {
        if ( buffer != null && buffer.length > 0 ) {
            for ( final byte b : buffer ) {
                rx(b);
            }
        }
    }

    /**
     * Rx void.
     *
     * @param b the b
     */
    public void rx(final byte b) {
        if (XBeeState.STATE_FRAME_WAIT_START == state) {
            //Wait for the state of a message( do nothing as long as b is the start delimiter)
            handleStateWaitFrameStart(b);
        } else if (XBeeState.STATE_FRAME_DATA_LENGTH == state) {
            // Read the length of the message
            handleStateFrameDataLength(b);
        } else if (XBeeState.STATE_FRAME_API_IDENTIFIER == state) {
            // Read the identifier of the message and go to the dedicated state
            try {
                handleStateApiIdentifier(b);
            } catch (XBeeException e) {
                state = XBeeState.STATE_FRAME_WAIT_START;
                // todo: notify listener of invalid frame rx?
            }
        } else if (XBeeState.STATE_FRAME_DATA == state) {
            // read frame data
            handleStateFrameData(b);
        } else if (XBeeState.STATE_FRAME_CHECKSUM_VALIDATION == state) {
            // checksum validation
            handleStateFrameChecksumValidation(b);
        } else {
            // else drop it unless it is a start delimiter
            handleStateWaitFrameStart(b);
        }
    }

    private void handleStateFrameChecksumValidation(byte b) {
        try {
            rxFrameDataChecksum += b;
            // validate checksum
            if (XBEE_FRAME_VALID_CHECKSUM == rxFrameDataChecksum) {
                // todo: notify of received frame
                listener.onReceiveFrame(current);
            } else {
                // todo: notify listener of invalid frame rx?
            }
        } finally {
            state = XBeeState.STATE_FRAME_WAIT_START;
        }
    }

    private void handleStateFrameData(byte b) {
        current.receive(b);
        // computed checksum
        rxFrameDataChecksum += b;
        // if rxFrameDataSize bytes have been read,
        // goto next state
        rxFrameDataIndex++;
        if (rxFrameDataIndex == rxFrameDataSize) {
            state = XBeeState.STATE_FRAME_CHECKSUM_VALIDATION;
        }
    }

    private void handleStateApiIdentifier(byte b) throws XBeeException {
        current = rxFrameFactory.newRxFrameForApiId(b);
        // set up next state
        rxFrameDataChecksum += b;
        rxFrameDataIndex++;
        // goto next state
        state = XBeeState.STATE_FRAME_DATA;
    }

    private void handleStateFrameDataLength(byte b) {
        final ByteBuffer buffer = (ByteBuffer) ByteBuffer
            .allocate(2)
            .putShort(0, rxFrameDataSize)
            .put((rxFrameDataSizeByteIndex++), b)
            .rewind();
        rxFrameDataSize = buffer.getShort();

        // If size is complete, go to next state
        if (XBEE_NUMBER_OF_SIZE_BYTES == rxFrameDataSizeByteIndex) {
            // Set up next state
            rxFrameDataIndex = 0;
            rxFrameDataChecksum = 0;
            // Go to next state
            state = XBeeState.STATE_FRAME_API_IDENTIFIER;
        }
    }

    private void handleStateWaitFrameStart(byte b) {
        if (START_DELIMITER == b) {
            // Set up next state
            rxFrameDataSize = 0;
            rxFrameDataSizeByteIndex = 0;
            // Go to next state
            state = XBeeState.STATE_FRAME_DATA_LENGTH;
        } else {
            state = XBeeState.STATE_FRAME_WAIT_START;
        }
    }
}
