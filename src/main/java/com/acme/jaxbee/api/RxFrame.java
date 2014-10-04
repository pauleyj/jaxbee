package com.acme.jaxbee.api;

/**
 * Created by pauleyj on 10/4/14.
 */
public abstract class RxFrame extends ApiFrame {
    public abstract void receive(final byte b);
}
