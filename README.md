# JaXBee - The Java XBee Library

JaXBee is a platform independent XBee API mode library for Java.  It currently has full support for XBee 802.15.4 and nearly complete XBee ZNet2.5 and ZB (ZigBee) modules.  It should be fairly straight forward to add support for other XBee modules as needed.  Please feel free to contribute additional modules!

What makes this XBee library useful? It has no concept of the UART on the platform you are using so it can be used on any platform that supports Java and has a UART that you can communicate with.  For example, I use it on OS X and on Android using the [IOIO](https://github.com/ytai/ioio/wiki).

---

### Usage

Using the JaXBee library is easy enough, implement a couple of interfaces, instantiate an XBee, and start communicating.  A functional Mac OS X sample program is included. The sample uses the [Java Simple Serial Connector](https://github.com/scream3r/java-simple-serial-connector) library for UART communications.

#### The Interfaces

Two interfaces are necessary to use the JaXBee Library, XBeeCommunications and XBeeListener.  These are used to send data to the UART and deliver a frame when received.

##### XBeeCommunications

XBeeCommunications is where the JaXBee library will direct API frame bytes for delivery to the UART where the XBee will receive them and perform the action directed by the API frame.  This enables JaXBee to be used with any Java UART implementation which can and does vary from library to library.

```java
XBeeCommunications communications = new XBeeCommunications() {
    @Override
    public void onSend(byte b) {
		// send a byte out the UART
		// uart.tx(b);
    }

    @Override
    public void onSend(byte[] buffer) {
        // send an array of bytes out the UART
        // uart.tx(buffer);
    }

    @Override
    public void onFlushSendBuffer() {
		// cause the UART to flush send buffer
		// uart.flush();
   }
};
```

##### XBeeListener

XBeeListener is where received XBee API frames are delivered.

```java
XBeeListener listener = new XBeeListener() {
    @Override
    public void onReceiveFrame(RxFrame frame) {
       System.out.println("onReceiveFrame(" + frame.getClass().getSimpleName() + ") - " + frame.toString());
   }
};
```

##### XBee

XBee is the XBee object.  Use it to transmit and receive API frames.

```java
XBee xbee = new XBee(communications, listener);
// build and send a NI AT command (Node Identifier)
AtCommandBuilder builder =
    new AtCommandBuilder()
            .setFrameId((byte) 0x01)
            .setCommand(Commands.NI);
xbee.tx(builder.build());
```

##### Receiving Data from the UART

When data is received from the UART, pass it along to JaXBee, the XBeeListener will be notified when a completed frame has arrived.

```java
...
byte b = uart.rx();
xbee.rx(b);
```

### Get It

JaxBee is available via Maven Central, add it as a dependency to your Maven POM

```xml
<dependency>
  <groupId>com.github.pauleyj</groupId>
  <artifactId>jaxbee</artifactId>
  <version>1.0.0</version>
</dependency>
```

### License

JaXBee is licensed under the Apache License, Version 2.0.  See the [LICENSE](https://github.com/pauleyj/jaxbee/blob/master/LICENSE) for more information.


