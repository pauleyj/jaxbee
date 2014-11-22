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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ModemStatusTest {

    @Test
    public void testGetStatus() throws Exception {
        ModemStatus frame = new ModemStatus();
        frame.receive((byte)0x00);

        assertThat(frame.getStatus(), is(equalTo(ModemStatus.Status.HARDWARE_RESET)));
    }

    @Test
    public void testGetStatusValue() throws Exception {
        ModemStatus frame = new ModemStatus();
        frame.receive((byte)0x00);

        assertThat(frame.getStatusValue(), is(equalTo((byte)0x00)));
    }

    @Test
    public void testGetFrameType() throws Exception {
        ModemStatus frame = new ModemStatus();

        assertThat(frame.getFrameType(), is(equalTo(ModemStatus.FRAME_TYPE)));
    }

    @Test
    public void testStatusValues() {
        ModemStatus.Status status = ModemStatus.Status.from((byte)0x00);
        assertThat(status, is(equalTo(ModemStatus.Status.HARDWARE_RESET)));

        status = ModemStatus.Status.from((byte)0x01);
        assertThat(status, is(equalTo(ModemStatus.Status.WATCHDOG_TIMER_RESET)));
        status = ModemStatus.Status.from((byte)0x02);
        assertThat(status, is(equalTo(ModemStatus.Status.JOINED_NETWORK)));
        status = ModemStatus.Status.from((byte)0x03);
        assertThat(status, is(equalTo(ModemStatus.Status.DISASSOCIATED)));
        status = ModemStatus.Status.from((byte)0x04);
        assertThat(status, is(equalTo(ModemStatus.Status.SYNCRONIZATION_LOST)));
        status = ModemStatus.Status.from((byte)0x05);
        assertThat(status, is(equalTo(ModemStatus.Status.COORDINATOR_REALIGNMENT)));
        status = ModemStatus.Status.from((byte)0x06);
        assertThat(status, is(equalTo(ModemStatus.Status.COORDINATOR_STARTED)));
        status = ModemStatus.Status.from((byte)0x07);
        assertThat(status, is(equalTo(ModemStatus.Status.NETWORK_KEY_UPDATED)));
        status = ModemStatus.Status.from((byte)0x0D);
        assertThat(status, is(equalTo(ModemStatus.Status.VOLTAGE_SUPPLY_LIMIT_EXCEEDED)));
        status = ModemStatus.Status.from((byte)0x11);
        assertThat(status, is(equalTo(ModemStatus.Status.CONFIGURATION_CHANGED_WHILE_JOIN)));
        status = ModemStatus.Status.from((byte)0x80);
        assertThat(status, is(equalTo(ModemStatus.Status.STACK_ERROR)));
    }
}