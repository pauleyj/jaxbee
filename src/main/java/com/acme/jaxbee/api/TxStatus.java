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

/**
 * Created by pauleyj on 10/13/14.
 */
public class TxStatus extends RxFrame {
    public static final byte FRAME_TYPE = (byte) 0x89;

    private enum State {
        FRAME_ID,
        STATUS
    }

    private byte status;
    private State state;

    public enum Status {
        SUCCESS,
        NO_ACK,
        CCA_FAILURE,
        PURGED,
        UNKNOWN;

        static Status from(byte value) {
            switch (value) {
                case 0:
                    return SUCCESS;
                case 1:
                    return NO_ACK;
                case 2:
                    return CCA_FAILURE;
                case 3:
                    return PURGED;
                default:
                    return UNKNOWN;
            }
        }
    }

    public TxStatus() {
        status = Byte.MIN_VALUE;
        state = State.FRAME_ID;
    }

    public Status getStatus() {
        return Status.from(status);
    }

    @Override
    public byte getFrameType() {
        return FRAME_TYPE;
    }

    @Override
    public void receive(byte b) {
        if (State.FRAME_ID == state) {
            setFrameId(b);
            state = State.STATUS;
        } else if (State.STATUS == state) {
            status = b;
        }
    }

    @Override
    public String toString() {
        return new StringBuffer()
            .append('{')
            .append("\"frameId\" : ").append(getFrameId()).append(", ")
            .append("\"status\" : ").append('"').append(getStatus()).append('"').append(", ")
            .append('}')
            .toString();
    }
}
