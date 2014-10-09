package com.acme.sample.jaxbee;

import com.acme.jaxbee.RxFrameFactory;
import com.acme.jaxbee.XBee;
import com.acme.jaxbee.XBeeCommunications;
import com.acme.jaxbee.XBeeException;
import com.acme.jaxbee.api.TxFrame;
import com.acme.jaxbee.api.at.AtCommandBuilder;
import com.acme.jaxbee.api.at.Commands;

/**
 * Created by pauleyj on 10/4/14.
 */
public class Main {

    public static void main(final String[] args) {
        System.out.println("Hello world, some day I'll talk XBee!");

        XBeeCommunications communications = new XBeeCommunications() {
                        @Override
                        public void onSend(byte b) {
                            System.out.println(String.format("0x%02x", b));
                        }

                        @Override
                        public void onSend(byte[] buffer) {
                            StringBuilder builder = new StringBuilder();
                            for(final byte b : buffer) {
                                builder.append(String.format("0x%02x", b)).append(' ');
                            }
                            System.out.println(builder.toString());
                        }

                        @Override
                        public void onReceiveRfDataByte(byte rx) {
                            System.out.println(String.format("0x%02x", rx) + " - \"" + (char)rx + '"');
                        }

                        @Override
                        public void onFlushSendBuffer() {
                        }
                    };

        XBee xbee = new XBee(communications);

        try {
            AtCommandBuilder builder = new AtCommandBuilder();
            TxFrame frame = builder.setFrameId((byte)0x01).setCommand(Commands.NI).build();
            xbee.tx(frame);
        } catch (XBeeException e) {
            e.printStackTrace();
        }
    }
}
