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

package com.github.pauleyj.jaxbee.api;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class TxStatusTest {

    @Test
    public void testGetStatus() throws Exception {
        TxStatus frame = new TxStatus();
        assertThat(frame.getStatus(), is(equalTo(TxStatus.Status.UNKNOWN)));
    }

    @Test
    public void testGetFrameType() throws Exception {
        TxStatus frame = new TxStatus();
        assertThat(frame.getFrameType(), is(equalTo(TxStatus.FRAME_TYPE)));
    }

    @Test
    public void testReceive() throws Exception {
        TxStatus frame = new TxStatus();
        assertThat(frame.getState(), is(equalTo(TxStatus.State.FRAME_ID)));

        frame.receive((byte)0x01);
        assertThat(frame.getState(), is(equalTo(TxStatus.State.STATUS)));

        frame.receive((byte)0x00);
        assertThat(frame.getState(), is(equalTo(TxStatus.State.DONE)));
    }

    @Test
    public void testStatusValues(){
        assertThat(TxStatus.Status.from((byte)0x00), is(equalTo(TxStatus.Status.SUCCESS)));
        assertThat(TxStatus.Status.from((byte)0x01), is(equalTo(TxStatus.Status.NO_ACK)));
        assertThat(TxStatus.Status.from((byte)0x02), is(equalTo(TxStatus.Status.CCA_FAILURE)));
        assertThat(TxStatus.Status.from((byte)0x03), is(equalTo(TxStatus.Status.PURGED)));
        assertThat(TxStatus.Status.from((byte)0x04), is(equalTo(TxStatus.Status.UNKNOWN)));
    }

    @Test
    public void testToString() throws Exception {
        TxStatus frame = new TxStatus();
        assertThat(frame.toString(), is(notNullValue()));
    }
}