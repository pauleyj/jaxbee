package com.acme.jaxbee.api.at;

import com.acme.jaxbee.XBeeException;
import com.acme.jaxbee.api.ITxFrameBuilder;

/**
 * Created by pauleyj on 10/4/14.
 */
public class AtCommandBuilder implements ITxFrameBuilder {
    private byte frameId = 0;
    private byte[] command = null;
    private byte[] parameters = null;

    public AtCommandBuilder() {
    }

    public AtCommandBuilder setFrameId(final byte frameId) {
        this.frameId = frameId;
        return this;
    }

    public AtCommandBuilder setCommand(final byte[] command) {
        this.command = command;
        return this;
    }

    public AtCommandBuilder setParameters(byte[] parameters) {
        this.parameters = parameters;
        return this;
    }

    @Override
    public AtCommand build() throws XBeeException {
        if (command != null) {
            AtCommand frame = new AtCommand();
            frame.setCommand(command).setFrameId(frameId);
            if(parameters != null) {
                frame.setParameters(parameters);
            }
            return frame;
        } else {
            throw new XBeeException("Invalid command");
        }
    }
}
