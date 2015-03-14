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

import com.github.pauleyj.jaxbee.api.core.RxFrame;

public class ModemStatus extends RxFrame<ModemStatus> {
    public static final byte FRAME_TYPE = (byte)0x8A;

    public enum Status {
        HARDWARE_RESET,
        WATCHDOG_TIMER_RESET,
        JOINED_NETWORK,
        DISASSOCIATED,
        SYNCRONIZATION_LOST,
        COORDINATOR_REALIGNMENT,
        COORDINATOR_STARTED,
        NETWORK_KEY_UPDATED,
        VOLTAGE_SUPPLY_LIMIT_EXCEEDED,
        CONFIGURATION_CHANGED_WHILE_JOIN,
        STACK_ERROR;

        /**
         * From status.
         *
         * @param value the value
         * @return the status
         */
        static Status from(byte value) {
            switch (value) {
                case 0:
                    return HARDWARE_RESET;
                case 1:
                    return WATCHDOG_TIMER_RESET;
                case 2:
                    return JOINED_NETWORK;
                case 3:
                    return DISASSOCIATED;
                case 4:
                    return SYNCRONIZATION_LOST;
                case 5:
                    return COORDINATOR_REALIGNMENT;
                case 6:
                    return COORDINATOR_STARTED;
                case 7:
                    return NETWORK_KEY_UPDATED;
                case 0x0D:
                    return VOLTAGE_SUPPLY_LIMIT_EXCEEDED;
                case 0x11:
                    return CONFIGURATION_CHANGED_WHILE_JOIN;
                case (byte)0x80:
                default:
                    return STACK_ERROR;
            }
        }
    }

    private byte status;

    public ModemStatus(){}

    public Status getStatus() {
        return Status.from(status);
    }

    public byte getStatusValue() {
        return status;
    }

    @Override
    public byte getFrameType() {
        return FRAME_TYPE;
    }

    @Override
    public void receive(byte b) {
        status = b;
    }
}
