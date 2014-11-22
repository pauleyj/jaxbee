package com.github.pauleyj.jaxbee.api;

import com.github.pauleyj.jaxbee.api.core.RxFrame;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ZigBeeReceivePacketFactoryTest {

    @Test
    public void testNewFrame() throws Exception {
        RxFrame frame = new ZigBeeReceivePacketFactory().newFrame();
        assertThat(frame, is(instanceOf(ZigBeeReceivePacket.class)));
    }
}