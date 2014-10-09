package com.acme.jaxbee;

/**
 * Created with IntelliJ IDEA.
 * User: pauleyj
 * Date: 12/1/13
 * Time: 1:20 AM
 * basic communications with com.acme.monitor.core.xbee
 */
public interface XBeeCommunications {
    //! Type definition for function used to send a single byte through communication
    void onSend(final byte b);

    //! Type definition for function used to send bytes through communication
    void onSend(final byte[] buffer);

    //! Type definition for call-back function when a RF byte is received
    void onReceiveRfDataByte(final byte rx);

    //! Type definition for function used to flush output communication buffer
    void onFlushSendBuffer();
}
