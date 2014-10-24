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
import java.util.Arrays;

public class ReceivePacket16IO extends ReceivePacket16 {
    public static final byte FRAME_TYPE = (byte) 0x83;

    public ReceivePacket16IO() {
        super();
    }

    @Override
    public byte getFrameType() {
        return FRAME_TYPE;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder('[');
        byte[] data = getData();
        if(data != null && data.length>0) {
            final int length = data.length;
            for (int i = 0; i < length; ++i) {
                builder.append(String.format("0x%2x", data[i]));
                if (i != (length - 1)) {
                    builder.append(", ");
                }
            }
        }
        builder.append(']');

        return new StringBuffer()
            .append('{')
            .append("\"sourceAddress16\" : ").append(String.format("0x%04x", getSourceAddress16())).append(", ")
            .append("\"rssi\" : ").append(String.format("0x%02x", getRssi())).append(", ")
            .append("\"options\" : ").append(getOptions()).append(", ")
            .append("\"data\" : ").append('"').append(builder.toString()).append('"')
            .append('}')
            .toString();
    }
}
