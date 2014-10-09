package com.acme.jaxbee.api.at;

import com.acme.jaxbee.api.RxFrame;

/**
 * Created by pauleyj on 10/4/14.
 */
public class AtCommandResponse extends RxFrame {
    //! API identifier for AT command response
    public static final byte FRAME_TYPE = (byte) 0x88;

    @Override
    public byte getFrameType() {
        return FRAME_TYPE;
    }

    @Override
    public void receive(byte b) {

    }
}
