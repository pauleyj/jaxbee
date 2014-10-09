package com.acme.jaxbee.api.at;

import com.acme.jaxbee.XBeeException;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by pauleyj on 10/7/14.
 */
public class TestAtCommandBuilder {

    @Test
    public void atCommandBuilderParameters() throws XBeeException {
        final byte[] command = Commands.NI;
        final byte frameId = (byte) 0x00;
        final byte[] parameters = {(byte) 0x00};

        AtCommandBuilder builder = new AtCommandBuilder();
        AtCommand cmd =
            builder.setCommand(command)
                   .setFrameId(frameId)
                   .setParameters(parameters)
                   .build();

        assertThat(cmd.getParameters(), is(equalTo(parameters)));
    }

    @Test
    public void atCommandBuilderCommand() throws XBeeException {
        final byte[] command = Commands.NI;
        final byte frameId = (byte) 0x00;
        final byte[] parameters = {(byte) 0x00};

        AtCommandBuilder builder = new AtCommandBuilder();
        AtCommand cmd =
            builder.setCommand(command)
                   .setFrameId(frameId)
                   .setParameters(parameters)
                   .build();

        assertThat(cmd.getCommand(), is(equalTo(command)));
    }

    @Test
    public void atCommandBuilderFrameId() throws XBeeException {
        final byte[] command = Commands.NI;
        final byte frameId = (byte) 0x00;
        final byte[] parameters = {(byte) 0x00};

        AtCommandBuilder builder = new AtCommandBuilder();
        AtCommand cmd =
            builder.setCommand(command)
                   .setFrameId(frameId)
                   .setParameters(parameters)
                   .build();

        assertThat(cmd.getFrameId(), is(equalTo(frameId)));
    }

    @Test
    public void atCommandBuilderApiType() throws XBeeException {
        final byte[] command = Commands.NI;
        final byte frameId = (byte) 0x00;
        final byte[] parameters = {(byte) 0x00};

        AtCommandBuilder builder = new AtCommandBuilder();
        AtCommand cmd =
            builder.setCommand(command)
                   .setFrameId(frameId)
                   .setParameters(parameters)
                   .build();

        assertThat(cmd.getFrameType(), is(equalTo(AtCommand.FRAME_TYPE)));
    }
}
