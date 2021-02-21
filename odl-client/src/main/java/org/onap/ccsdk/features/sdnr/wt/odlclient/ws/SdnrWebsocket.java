/*
 * Copyright (C) 2019 highstreet technologies GmbH Intellectual Property.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.onap.ccsdk.features.sdnr.wt.odlclient.ws;

import java.io.IOException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.onap.ccsdk.features.sdnr.wt.odlclient.data.SdnrNotification;
import org.onap.ccsdk.features.sdnr.wt.odlclient.data.SdnrNotificationMapperXml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebSocket(maxTextMessageSize = 128 * 1024, maxIdleTime = Integer.MAX_VALUE)
public class SdnrWebsocket {

    private static final Logger LOG = LoggerFactory.getLogger(SdnrWebsocket.class);
    private static final String MESSAGE_NOTIFICATION_REGISTER =
            "{\"data\":\"scopes\"," + "\"scopes\":[\"ObjectCreationNotification\",\"ObjectDeletionNotification\","
                    + "\"AttributeValueChangedNotification\",\"ProblemNotification\"]}";

    private final SdnrWebsocketCallback callback;
    private final SdnrNotificationMapperXml mappper;
    private Session session;

    public SdnrWebsocket(SdnrWebsocketCallback cb) {
        this.callback = cb;
        this.mappper = new SdnrNotificationMapperXml();
    }

    public boolean sendNotificationRegistration() {
        if (this.session != null && this.session.isOpen()) {
            LOG.debug("sending notification registration");
            try {
                this.session.getRemote().sendString(MESSAGE_NOTIFICATION_REGISTER);
                return true;
            } catch (IOException e) {
                LOG.warn("unable to send register message: ", e);
                return false;
            }
        }
        return false;
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        LOG.info("Connection closed: {} - {}", statusCode, reason);
        this.session = null;
        this.callback.onDisconnect(statusCode, reason);
    }

    @OnWebSocketConnect
    public void onConnect(Session lsession) {
        LOG.info("Got connect: {}", lsession);
        this.session = lsession;
        this.sendNotificationRegistration();
        this.callback.onConnect(lsession);


    }

    @OnWebSocketMessage
    public void onMessage(String msg) {
        LOG.debug("Got msg: {}", msg);
        this.callback.onMessageReceived(msg);
        this.handleIncomingMessage(msg);
    }

    private void handleIncomingMessage(String msg) {
        SdnrNotification notification = this.mappper.read(msg);
        if (notification != null) {
            this.callback.onNotificationReceived(notification);
        }
    }

    @OnWebSocketError
    public void onError(Throwable cause) {
        LOG.warn("WebSocket Error: {}", cause);
        this.callback.onError(cause);
    }
}
