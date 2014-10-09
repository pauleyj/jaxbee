package com.acme.jaxbee.api;

/**
 * Created by pauleyj on 10/4/14.
 */
public abstract class ApiFrame {
    protected static final int API_FRAME_TYPE_LENGTH = 0x01;
    protected static final int API_FRAME_ID_LENGTH = 0x01;

    private byte frameId;

    protected ApiFrame(){}

    public byte getFrameId() {
        return frameId;
    }

    public ApiFrame setFrameId(byte frameId) {
        this.frameId = frameId;
        return this;
    }
}
