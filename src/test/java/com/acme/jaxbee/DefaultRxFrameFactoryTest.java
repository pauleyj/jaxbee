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

package com.acme.jaxbee;

import com.acme.jaxbee.DefaultRxFrameFactory;
import com.acme.jaxbee.api.RemoteAtCommandResponse;
import com.acme.jaxbee.api.core.RxFrame;
import com.acme.jaxbee.api.core.RxFrameFactory;
import com.acme.jaxbee.api.core.XBeeException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class DefaultRxFrameFactoryTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testNewRxFrameForApiId() throws Exception {
        DefaultRxFrameFactory factory = new DefaultRxFrameFactory();
        RxFrame frame = factory.newRxFrameForApiId(RemoteAtCommandResponse.FRAME_TYPE);

        assertThat(frame, is(instanceOf(RemoteAtCommandResponse.class)));
    }

    @Test
    public void testAddRxFrameFactoryForApiId() throws XBeeException {
        final byte dummyFrameType = (byte) 0xFF;
        DefaultRxFrameFactory factory = new DefaultRxFrameFactory();
        RxFrameFactory dummy = new RxFrameFactory() {
            @Override
            public RxFrame newFrame() {
                return new RxFrame() {
                    @Override
                    public byte getFrameType() {
                        return dummyFrameType;
                    }

                    @Override
                    public void receive(byte b) {

                    }
                };
            }
        };

        factory.addRxFrameFactoryForApiId(dummyFrameType, dummy);
        RxFrame frame = factory.newRxFrameForApiId((dummyFrameType));
        assertThat(frame.getFrameType(), is(equalTo(dummyFrameType)));
    }

    @Test
    public void testUnknownApiIdThrowsException() throws XBeeException {
        exception.expect(XBeeException.class);
        DefaultRxFrameFactory factory = new DefaultRxFrameFactory();
        factory.newRxFrameForApiId((byte)0xFF);
    }
}