package com.acme.jaxbee.api;

/**
 * Created by pauleyj on 10/4/14.
 */
public abstract class ApiFrame {
    private byte frameId;

    protected ApiFrame(){}

    public byte getFrameId() {
        return frameId;
    }

    public void setFrameId(byte frameId) {
        this.frameId = frameId;
    }
}
