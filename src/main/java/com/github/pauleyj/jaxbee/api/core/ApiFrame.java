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

package com.github.pauleyj.jaxbee.api.core;

/**
 * The type Api frame.
 */
public abstract class ApiFrame {
    /**
     * The constant API_FRAME_TYPE_LENGTH.
     */
    protected static final int API_FRAME_TYPE_LENGTH = 0x01;
    /**
     * The constant API_FRAME_ID_LENGTH.
     */
    protected static final int API_FRAME_ID_LENGTH = 0x01;

    private byte frameId;

    /**
     * Instantiates a new Api frame.
     */
    protected ApiFrame(){}

    /**
     * Gets frame id.
     *
     * @return the frame id
     */
    public byte getFrameId() {
        return frameId;
    }

    /**
     * Sets frame id.
     *
     * @param frameId the frame id
     * @return the frame id
     */
    public ApiFrame setFrameId(byte frameId) {
        this.frameId = frameId;
        return this;
    }
}
