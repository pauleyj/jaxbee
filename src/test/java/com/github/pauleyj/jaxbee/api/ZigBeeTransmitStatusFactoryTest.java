package com.github.pauleyj.jaxbee.api;

import com.github.pauleyj.jaxbee.api.core.RxFrame;
import com.github.pauleyj.jaxbee.api.core.RxFrameFactory;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ZigBeeTransmitStatusFactoryTest {

    @Test
    public void testNewFrame() throws Exception {
        RxFrameFactory factory = new ZigBeeTransmitStatusFactory();
        RxFrame frame = factory.newFrame();
        assertThat(frame, is(instanceOf(ZigBeeTransmitStatus.class)));
    }
}