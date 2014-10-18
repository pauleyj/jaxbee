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

import com.acme.jaxbee.api.core.RxFrame;

import java.nio.ByteBuffer;

/**
 * Created by edwardsb on 10/13/14.
 */
public class ZigBeeTransmitStatus extends RxFrame {

    /**
     * Transmit Status Frame Type
     */
    public static final byte FRAME_TYPE = (byte)0x8B;

    private static final byte ADDRESS16_LENGTH = 0x02;

    //
    // the states of the Transmit Status Message ref. page 112 of XBee®/XBee‐PRO® ZB RF Modules
    enum State {
        FRAME_ID,
        ADDRESS16,
        TRANSMIT_RETRY_COUNT,
        DELIVERY_STATUS,
        DISCOVERY_STATUS,
    }

    State getState() {
        return state;
    }

    /**
     * The enum Delivery Status
     */
    public static enum DeliveryStatus {
        SUCCESS,
        MAC_ACK_FAILURE,
        CCA_FAILURE,
        INVALID_DESTINATION_ENPOINT,
        NETWORK_ACK_FAILURE,
        NOT_JOINED_TO_NETWORK,
        SELF_ADDRESSED,
        ADDRESS_NOT_FOUND,
        ROUTE_NOT_FOUND,
        BROADCAST_SOURCE_FAILED_TO_HEAR_NEIGBOR_RELAY_MESSAGE,
        INVALID_BINDING_TABLE_INDEX,
        RESOURCE_ERROR_LACK_FREE_BUFFERS_TIMERS_ETC,
        ATTEMPTED_BROADCAST_WITH_APS_TRANSMISSION,
        ATTEMPTED_UNICAST_WITH_APS_TRANSMISSION_BUT_EE0,
        DATA_PAYLOAD_TOO_LARGE,
        UNDEFINED;

        static DeliveryStatus from(byte value) {
            switch (value) {
                case 0:
                    return SUCCESS;
                case 1:
                    return MAC_ACK_FAILURE;
                case 2:
                    return CCA_FAILURE;
                case 0x15:
                    return INVALID_DESTINATION_ENPOINT;
                case 0x21:
                    return NETWORK_ACK_FAILURE;
                case 0x22:
                    return NOT_JOINED_TO_NETWORK;
                case 0x23:
                    return SELF_ADDRESSED;
                case 0x24:
                    return ADDRESS_NOT_FOUND;
                case 0x25:
                    return ROUTE_NOT_FOUND;
                case 0x26:
                    return BROADCAST_SOURCE_FAILED_TO_HEAR_NEIGBOR_RELAY_MESSAGE;
                case 0x2B:
                    return INVALID_BINDING_TABLE_INDEX;
                case 0x2C:
                case 0x32:
                    return RESOURCE_ERROR_LACK_FREE_BUFFERS_TIMERS_ETC;
                case 0x2D:
                    return ATTEMPTED_BROADCAST_WITH_APS_TRANSMISSION;
                case 0x2E:
                    return ATTEMPTED_UNICAST_WITH_APS_TRANSMISSION_BUT_EE0;
                case 0x74:
                    return DATA_PAYLOAD_TOO_LARGE;
                default:
                    return UNDEFINED;
            }
        }
    }

    /**
     * The enum Discovery Status
     */
    public static enum DiscoveryStatus {
        NO_DISCOVERY_OVERHEAD,
        ADDRESS_DISCOVERY,
        ROUTE_DISCOVERY,
        ADDRESS_AND_ROUTE,
        EXTENDED_TIMEOUT_DISCOVERY,
        UNDEFINED;

        static DiscoveryStatus from(byte value) {
            switch (value) {
                case 0:
                    return NO_DISCOVERY_OVERHEAD;
                case 1:
                    return ADDRESS_DISCOVERY;
                case 2:
                    return ROUTE_DISCOVERY;
                case 3:
                    return ADDRESS_AND_ROUTE;
                case 0x40:
                    return EXTENDED_TIMEOUT_DISCOVERY;
                default:
                    return UNDEFINED;
            }
        }
    }

    private byte index;
    private short sourceAddress16;
    private State state;
    private byte deliveryStatus;
    private byte discoveryStatus;
    private byte transmitRetryCount;

    /**
     * 16-bit Network Address the packet was delivered to (if successful).
     * If not successful, this address will be 0xFFFD: Destination Address Unknown.
     * @return the source address 15
     */
    public short getSourceAddress16() {
        return sourceAddress16;
    }

    /**
     * Get the DeliveryStatus of the TX Status Message
     * 0x00 = Success
     * 0x01 = MAC ACK Failure
     * 0x02 = CCA Failure
     * 0x15 = Invalid destination endpoint
     * 0x21 = Network ACK Failure
     * 0x22 = Not Joined to Network
     * 0x23 = Self-addressed
     * 0x24 = Address Not Found
     * 0x25 = Route Not Found
     * 0x26 = Broadcast source failed to hear a neighbor relay the message
     * 0x2B = Invalid binding table index
     * 0x2C = Resource error lack of free buffers, timers, etc.
     * 0x2D = Attempted broadcast with APS transmission
     * 0x2E = Attempted unicast with APS transmission, but EE=0
     * 0x32 = Resource error lack of free buffers, timers, etc. 0x74 = Data payload too large
     * @return the Delivery Status
     */
    public DeliveryStatus getDeliveryStatus() {
        return DeliveryStatus.from(deliveryStatus);
    }

    /**
     * Get the DiscoveryStatus of the TX Status Message
     * 0x00 = No Discovery Overhead
     * 0x01 = Address Discovery
     * 0x02 = Route Discovery
     * 0x03 = Address and Route
     * 0x40 = Extended Timeout Discovery
     * @return the Discovery Status
     */
    public DiscoveryStatus getDiscoveryStatus() {
        return DiscoveryStatus.from(discoveryStatus);
    }

    /**
     * The number of application transmission retries that took place.
     * @return the number of transmit retries
     */
    public int getTransmitRetryCount() {
        return transmitRetryCount;
    }

    public ZigBeeTransmitStatus() {
        index = 0;
        sourceAddress16 = Short.MIN_VALUE;
        deliveryStatus = Byte.MIN_VALUE;
        discoveryStatus = Byte.MIN_VALUE;
        state = State.FRAME_ID;
    }

    @Override
    public byte getFrameType() {
        return FRAME_TYPE;
    }

    @Override
    public void receive(byte b) {

        switch (state) {
            case FRAME_ID:
                handleStateFrameId(b);
                break;
            case ADDRESS16:
                handleStateAddress16(b);
                break;
            case TRANSMIT_RETRY_COUNT:
                handleStateTransmitRetryCount(b);
                break;
            case DELIVERY_STATUS:
                handleStateDeliveryStatus(b);
                break;
            case DISCOVERY_STATUS:
                handleStateDiscoveryStatus(b);
                break;
        }
    }

    private void handleStateFrameId(byte b) {
        setFrameId(b);
        index = 0;
        state = State.ADDRESS16;
    }

    private void handleStateAddress16(byte b) {
        sourceAddress16 = ByteBuffer.allocate(ADDRESS16_LENGTH).putShort(0, sourceAddress16).put(index++, b).getShort();
        if (index == ADDRESS16_LENGTH) {
            index = 0;
            state = State.TRANSMIT_RETRY_COUNT;
        }
    }

    private void handleStateTransmitRetryCount(byte b) {
        transmitRetryCount = b;
        index = 0;
        state = State.DELIVERY_STATUS;
    }

    private void handleStateDeliveryStatus(byte b) {
        deliveryStatus = b;
        index = 0;
        state = State.DISCOVERY_STATUS;
    }

    private void handleStateDiscoveryStatus(byte b) {
        discoveryStatus = b;
        index = 0;
    }

    @Override
    public String toString() {
        return new StringBuffer()
                .append('{')
                .append("\"frameId\" : ").append(getFrameId()).append(", ")
                .append("\"sourceAddress16\" : ").append(String.format("0x%02x", sourceAddress16)).append(", ")
                .append("\"transmit retry count\" : ").append('"').append(getTransmitRetryCount()).append('"').append(", ")
                .append("\"delivery status\" : ").append('"').append(getDeliveryStatus()).append('"').append(", ")
                .append("\"discovery status\" : ").append('"').append(getDiscoveryStatus()).append('"')
                .append('}')
                .toString();
    }
}
