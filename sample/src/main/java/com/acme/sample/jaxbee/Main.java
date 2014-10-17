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

package com.acme.sample.jaxbee;

import com.acme.jaxbee.api.*;
import com.acme.jaxbee.api.core.RxFrame;
import com.acme.jaxbee.api.core.XBeeCommunications;
import com.acme.jaxbee.api.core.XBeeException;
import com.acme.jaxbee.api.core.XBeeListener;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    static SerialPort      serialPort;
    static XBee            xbee;
    static ExecutorService executor;

    public static void main(final String[] args) {
        System.out.println("Hello world, I talk XBee!");

        serialPort = new SerialPort("/dev/tty.usbserial-A800cGqh");
        try {
            serialPort.openPort();//Open port
            serialPort.setParams(
                    SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);//Set params
            int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;//Prepare mask
            serialPort.setEventsMask(mask);//Set mask
            serialPort.addEventListener(new SerialPortReader());//Add SerialPortEventListener

            executor = Executors.newSingleThreadExecutor();

            XBeeCommunications communications = new XBeeCommunications() {
                @Override
                public void onSend(byte b) {
//                    System.out.println(String.format("0x%02x", b));
                    try {
                        serialPort.writeByte(b);
                    } catch ( SerialPortException e ) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onSend(byte[] buffer) {
                    StringBuilder builder = new StringBuilder("TX --> ");
                    for (final byte b : buffer) {
                        builder.append(String.format("0x%02x", b)).append(' ');
                    }
                    System.out.println(builder.toString());
                    try {
                        serialPort.writeBytes(buffer);
                    } catch (SerialPortException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFlushSendBuffer() {

                }
            };
            XBeeListener listener = new XBeeListener() {
                @Override
                public void onReceiveFrame(RxFrame frame) {
                    System.out.println("onReceiveFrame(" + frame.getClass().getSimpleName() + ") - " + frame.toString());
                }
            };
            xbee = new XBee(communications, listener);

            try {
                AtCommandBuilder atCommandBuilder =
                    new AtCommandBuilder()
                        .setFrameId((byte) 0x01)
                        .setCommand(Commands.NI);
//                xbee.tx(atCommandBuilder.build());

                RemoteAtCommandBuilder remoteAtCommandBuilder =
                    new RemoteAtCommandBuilder()
                        .setFrameId((byte) 0x02)
                        .setDestinationAddress64(XBee.BROADCAST_ADDRESS_64)
                        .setDestinationAddress16(XBee.BROADCAST_ADDRESS_16)
                        .setCommand(Commands.NI);
//                xbee.tx(remoteAtCommandBuilder.build());

                TransmitRequest64Builder transmitRequest64Builder =
                    new TransmitRequest64Builder()
                        .setFrameId((byte) 0x03)
                        .setDestinationAddress64(0x13a200403203abL)
                        .setData("Hello".getBytes());
                xbee.tx(transmitRequest64Builder.build());

                ZigBeeTransmitRequestBuilder zigBeeTransmitRequestBuilder =
                    new ZigBeeTransmitRequestBuilder()
                        .setFrameId((byte) 0x04)
                        .setDestinationAddress64(0x13a200403203abL)
                        .setDestinationAddress16((short) 0xd9f0)
                        .setData("Hello world!".getBytes());
                xbee.tx(zigBeeTransmitRequestBuilder.build());

//                TimeUnit.SECONDS.sleep(1);
//                serialPort.closePort();
            } catch (XBeeException e) {
                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
            }
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    /*
     * In this class must implement the method serialEvent, through it we learn about
     * events that happened to our port. But we will not report on all events but only
     * those that we put in the mask. In this case the arrival of the data and change the
     * status lines CTS and DSR
     */
    static class SerialPortReader implements SerialPortEventListener {

        public void serialEvent(final SerialPortEvent event) {
            if (event.isRXCHAR()) {//If data is available
                //Read data
                try {
                    final int inputBufferBytesCount = event.getEventValue();
                    final byte[] buffer = serialPort.readBytes(inputBufferBytesCount);
                    final StringBuilder builder = new StringBuilder("RX <-- ");
                    for (byte b : buffer) {
                        builder.append(String.format("0x%02x", b)).append(' ');
                    }
                    System.out.println(builder.toString());
                    xbee.rx(buffer);
                } catch (SerialPortException ex) {
                    System.out.println(ex);
                }
            } else if (event.isCTS()) {//If CTS line has changed state
                if (event.getEventValue() == 1) {//If line is ON
                    System.out.println("CTS - ON");
                } else {
                    System.out.println("CTS - OFF");
                }
            } else if (event.isDSR()) {//If DSR line has changed state
                if (event.getEventValue() == 1) {//If line is ON
                    System.out.println("DSR - ON");
                } else {
                    System.out.println("DSR - OFF");
                }
            }
        }
    }
}
