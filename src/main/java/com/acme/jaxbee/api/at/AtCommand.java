package com.acme.jaxbee.api.at;

import com.acme.jaxbee.XBeeException;
import com.acme.jaxbee.api.TxFrame;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by pauleyj on 10/4/14.
 */
public class AtCommand extends TxFrame {
    //! API identifier for AT command
    public static final byte FRAME_TYPE = (byte) 0x08;
    private static final byte AT_COMMAND_LENGTH = (byte) 0x02;

    private byte[] command;
    private byte[] parameters;

    public AtCommand() {
        command = null;
    }

    public byte[] getCommand() {
        return command;
    }
    public AtCommand setCommand(final byte[] command) throws XBeeException {
        if (command == null || command.length != AT_COMMAND_LENGTH) {
            throw new XBeeException("Invalid AT command");
        }
        this.command = Arrays.copyOf(command, command.length);
        return this;
    }

    private int getPrametersLength() {
        return (parameters != null) ? parameters.length : 0;
    }

    public byte[] getParameters() {
        return parameters;
    }

    public void setParameters(byte[] parameters) {
        if (parameters != null && parameters.length > 0) {
            this.parameters = Arrays.copyOf(parameters, parameters.length);
        }
    }

    @Override
    public byte getFrameType() {
        return FRAME_TYPE;
    }

    @Override
    public byte[] toBytes() {
        // capacity = frame type + frame id + at command + parameters
        final int capacity = API_FRAME_TYPE_LENGTH + API_FRAME_ID_LENGTH + AT_COMMAND_LENGTH + getPrametersLength();
        final ByteBuffer buffer =
            ByteBuffer.allocate(capacity)
                      .put(FRAME_TYPE)
                      .put(getFrameId())
                      .put(command);
        if (parameters != null) {
            buffer.put(parameters);
        }
        return buffer.array();
    }
}
