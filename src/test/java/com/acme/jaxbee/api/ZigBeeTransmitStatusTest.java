package com.acme.jaxbee.api;

import com.acme.jaxbee.api.core.RxFrame;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ZigBeeTransmitStatusTest {

    ZigBeeTransmitStatus frame;

    final static byte FRAME_ID_BYTE = (byte) 0x01;
    final static byte ADDRESS_PART1_BYTE = (byte) 0x7D;
    final static byte ADDRESS_PART2_BYTE = (byte) 0x84;
    final static byte TRANSMIT_RETRY_COUNT_BYTE = (byte) 0x00;
    final static byte DELIVERY_STATUS_BYTE = (byte) 0x00;
    final static byte DISCOVERY_STATUS_BYTE = (byte) 0x01;


    @Before
    public void setUp() throws Exception {

        frame = new ZigBeeTransmitStatus();
        byte[] response = {
                FRAME_ID_BYTE,
                ADDRESS_PART1_BYTE,
                ADDRESS_PART2_BYTE,
                TRANSMIT_RETRY_COUNT_BYTE,
                DELIVERY_STATUS_BYTE,
                DISCOVERY_STATUS_BYTE};

        for(byte b: response) {
            frame.receive(b);
        }
    }


    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetSourceAddress16() throws Exception {
        ByteBuffer bb = ByteBuffer.allocate(2).put(0, ADDRESS_PART1_BYTE).put(1, ADDRESS_PART2_BYTE);
        short address = ((ByteBuffer)bb.position(0)).getShort();
        assertThat(frame.getSourceAddress16(), is(equalTo(address)));

    }

    @Test
    public void testGetDeliveryStatus() throws Exception {

        ZigBeeTransmitStatus success = createFrameWithDeliveryStatus((byte) 0x00);
        assertThat(success.getDeliveryStatus(), is(equalTo(ZigBeeTransmitStatus.DeliveryStatus.SUCCESS)));

        ZigBeeTransmitStatus mac_failure = createFrameWithDeliveryStatus((byte) 0x01);
        assertThat(mac_failure.getDeliveryStatus(), is(equalTo(ZigBeeTransmitStatus.DeliveryStatus.MAC_ACK_FAILURE)));

        ZigBeeTransmitStatus cca_failure = createFrameWithDeliveryStatus((byte) 0x02);
        assertThat(cca_failure.getDeliveryStatus(), is(equalTo(ZigBeeTransmitStatus.DeliveryStatus.CCA_FAILURE)));

        ZigBeeTransmitStatus invalid_destination_endpoint = createFrameWithDeliveryStatus((byte) 0x15);
        assertThat(invalid_destination_endpoint.getDeliveryStatus(), is(equalTo(ZigBeeTransmitStatus.DeliveryStatus.INVALID_DESTINATION_ENPOINT)));

        ZigBeeTransmitStatus network_ack_failure = createFrameWithDeliveryStatus((byte) 0x21);
        assertThat(network_ack_failure.getDeliveryStatus(), is(equalTo(ZigBeeTransmitStatus.DeliveryStatus.NETWORK_ACK_FAILURE)));

        ZigBeeTransmitStatus not_joined_to_network = createFrameWithDeliveryStatus((byte) 0x22);
        assertThat(not_joined_to_network.getDeliveryStatus(), is(equalTo(ZigBeeTransmitStatus.DeliveryStatus.NOT_JOINED_TO_NETWORK)));

        ZigBeeTransmitStatus self_addressed = createFrameWithDeliveryStatus((byte) 0x23);
        assertThat(self_addressed.getDeliveryStatus(), is(equalTo(ZigBeeTransmitStatus.DeliveryStatus.SELF_ADDRESSED)));

        ZigBeeTransmitStatus address_not_found = createFrameWithDeliveryStatus((byte) 0x24);
        assertThat(address_not_found.getDeliveryStatus(), is(equalTo(ZigBeeTransmitStatus.DeliveryStatus.ADDRESS_NOT_FOUND)));

        ZigBeeTransmitStatus route_not_found = createFrameWithDeliveryStatus((byte) 0x25);
        assertThat(route_not_found.getDeliveryStatus(), is(equalTo(ZigBeeTransmitStatus.DeliveryStatus.ROUTE_NOT_FOUND)));

        ZigBeeTransmitStatus broadcast_source_failed = createFrameWithDeliveryStatus((byte) 0x26);
        assertThat(broadcast_source_failed.getDeliveryStatus(), is(equalTo(ZigBeeTransmitStatus.DeliveryStatus.BROADCAST_SOURCE_FAILED_TO_HEAR_NEIGBOR_RELAY_MESSAGE)));

        ZigBeeTransmitStatus invalid_binding = createFrameWithDeliveryStatus((byte) 0x2B);
        assertThat(invalid_binding.getDeliveryStatus(), is(equalTo(ZigBeeTransmitStatus.DeliveryStatus.INVALID_BINDING_TABLE_INDEX)));

        ZigBeeTransmitStatus resource_error_buffer = createFrameWithDeliveryStatus((byte) 0x2C);
        assertThat(resource_error_buffer.getDeliveryStatus(), is(equalTo(ZigBeeTransmitStatus.DeliveryStatus.RESOURCE_ERROR_LACK_FREE_BUFFERS_TIMERS_ETC)));

        ZigBeeTransmitStatus attempted_broadcast = createFrameWithDeliveryStatus((byte) 0x2D);
        assertThat(attempted_broadcast.getDeliveryStatus(), is(equalTo(ZigBeeTransmitStatus.DeliveryStatus.ATTEMPTED_BROADCAST_WITH_APS_TRANSMISSION)));

        ZigBeeTransmitStatus attempted_unicast = createFrameWithDeliveryStatus((byte) 0x2E);
        assertThat(attempted_unicast.getDeliveryStatus(), is(equalTo(ZigBeeTransmitStatus.DeliveryStatus.ATTEMPTED_UNICAST_WITH_APS_TRANSMISSION_BUT_EE0)));

        ZigBeeTransmitStatus resource_error_timer = createFrameWithDeliveryStatus((byte) 0x32);
        assertThat(resource_error_timer.getDeliveryStatus(), is(equalTo(ZigBeeTransmitStatus.DeliveryStatus.RESOURCE_ERROR_LACK_FREE_BUFFERS_TIMERS_ETC)));

        ZigBeeTransmitStatus payload_too_large = createFrameWithDeliveryStatus((byte) 0x74);
        assertThat(payload_too_large.getDeliveryStatus(), is(equalTo(ZigBeeTransmitStatus.DeliveryStatus.DATA_PAYLOAD_TOO_LARGE)));

        ZigBeeTransmitStatus undefined = createFrameWithDeliveryStatus((byte) 0xFF);
        assertThat(undefined.getDeliveryStatus(), is(equalTo(ZigBeeTransmitStatus.DeliveryStatus.UNDEFINED)));
    }

    private ZigBeeTransmitStatus createFrameWithDeliveryStatus(byte b) {
        ZigBeeTransmitStatus zts = new ZigBeeTransmitStatus();
        zts.receive(FRAME_ID_BYTE);
        zts.receive(ADDRESS_PART1_BYTE);
        zts.receive(ADDRESS_PART2_BYTE);
        zts.receive(TRANSMIT_RETRY_COUNT_BYTE);
        zts.receive(b); // the delivery status
        zts.receive(DISCOVERY_STATUS_BYTE);
        return zts;
    }

    @Test
    public void testGetDiscoveryStatus() throws Exception {
        ZigBeeTransmitStatus no_discovery_overhead = createFrameWithDiscoveryStatus((byte) 0x00);
        assertThat(no_discovery_overhead.getDiscoveryStatus(), is(equalTo(ZigBeeTransmitStatus.DiscoveryStatus.NO_DISCOVERY_OVERHEAD)));

        ZigBeeTransmitStatus address_discovery = createFrameWithDiscoveryStatus((byte) 0x01);
        assertThat(address_discovery.getDiscoveryStatus(), is(equalTo(ZigBeeTransmitStatus.DiscoveryStatus.ADDRESS_DISCOVERY)));

        ZigBeeTransmitStatus route_discovery = createFrameWithDiscoveryStatus((byte) 0x02);
        assertThat(route_discovery.getDiscoveryStatus(), is(equalTo(ZigBeeTransmitStatus.DiscoveryStatus.ROUTE_DISCOVERY)));

        ZigBeeTransmitStatus address_and_route = createFrameWithDiscoveryStatus((byte) 0x03);
        assertThat(address_and_route.getDiscoveryStatus(), is(equalTo(ZigBeeTransmitStatus.DiscoveryStatus.ADDRESS_AND_ROUTE)));

        ZigBeeTransmitStatus extended_timeout_discovery = createFrameWithDiscoveryStatus((byte) 0x40);
        assertThat(extended_timeout_discovery.getDiscoveryStatus(), is(equalTo(ZigBeeTransmitStatus.DiscoveryStatus.EXTENDED_TIMEOUT_DISCOVERY)));

        ZigBeeTransmitStatus undefined = createFrameWithDiscoveryStatus((byte) 0xFF);
        assertThat(undefined.getDiscoveryStatus(), is(equalTo(ZigBeeTransmitStatus.DiscoveryStatus.UNDEFINED)));

    }

    private ZigBeeTransmitStatus createFrameWithDiscoveryStatus(byte b) {
        ZigBeeTransmitStatus zts = new ZigBeeTransmitStatus();
        zts.receive(FRAME_ID_BYTE);
        zts.receive(ADDRESS_PART1_BYTE);
        zts.receive(ADDRESS_PART2_BYTE);
        zts.receive(TRANSMIT_RETRY_COUNT_BYTE);
        zts.receive(DELIVERY_STATUS_BYTE);
        zts.receive(b); // the discovery status
        return zts;
    }

    @Test
    public void testGetTransmitRetryCount() throws Exception {

        assertThat(frame.getTransmitRetryCount(), is(equalTo((int) TRANSMIT_RETRY_COUNT_BYTE)));

    }

    @Test
    public void testGetFrameType() throws Exception {

        RxFrame frame = new ZigBeeTransmitStatus();
        frame.receive((byte)0x00);
        assertThat("FrameType should be ZigBee Transmit Status FrameType", frame.getFrameType(), is(equalTo((ZigBeeTransmitStatus.FRAME_TYPE))));

    }

    @Test
    public void testReceive() throws Exception {

        ZigBeeTransmitStatus zts = new ZigBeeTransmitStatus();
        assertThat(zts.getState(), is(equalTo(ZigBeeTransmitStatus.State.FRAME_ID)));

        zts.receive(FRAME_ID_BYTE);
        assertThat(zts.getState(), is(equalTo(ZigBeeTransmitStatus.State.ADDRESS16)));

        zts.receive(ADDRESS_PART1_BYTE);
        assertThat(zts.getState(), is(equalTo(ZigBeeTransmitStatus.State.ADDRESS16)));

        zts.receive(ADDRESS_PART2_BYTE);
        assertThat(zts.getState(), is(equalTo(ZigBeeTransmitStatus.State.TRANSMIT_RETRY_COUNT)));

        zts.receive(TRANSMIT_RETRY_COUNT_BYTE);
        assertThat(zts.getState(), is(equalTo(ZigBeeTransmitStatus.State.DELIVERY_STATUS)));

        zts.receive(DELIVERY_STATUS_BYTE);
        assertThat(zts.getState(), is(equalTo(ZigBeeTransmitStatus.State.DISCOVERY_STATUS)));

        zts.receive(DISCOVERY_STATUS_BYTE);
        assertThat(zts.getState(), is(equalTo(ZigBeeTransmitStatus.State.DISCOVERY_STATUS)));
    }

    @Test
    public void testToString() throws Exception {

        String frameString = frame.toString();

        assertThat(frameString, is(notNullValue()));

        Gson gson = new Gson();
        Object object = gson.fromJson(frameString, Object.class);

        assertThat(object, is(notNullValue()));
    }
}