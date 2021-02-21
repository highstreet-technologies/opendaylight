/*
 * Copyright (C) 2020 highstreet technologies GmbH Intellectual Property.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.onap.ccsdk.features.sdnr.wt.odlclient.ws;

import org.eclipse.jetty.websocket.api.Session;
import org.onap.ccsdk.features.sdnr.wt.odlclient.data.SdnrNotification;
import org.onap.ccsdk.features.sdnr.wt.odlclient.data.notifications.ProblemNotification;

public abstract class FilteredNotificationWebsocketCallback implements SdnrWebsocketCallback{

    private final String nodeId;

    public abstract void onFilteredNotificationReceived(ProblemNotification notification);

    public FilteredNotificationWebsocketCallback(String nodeId){
        this.nodeId = nodeId;
    }
    @Override
    public void onConnect(Session lsession) {
    }

    @Override
    public void onMessageReceived(String msg) {
    }

    @Override
    public void onDisconnect(int statusCode, String reason) {
    }

    @Override
    public void onError(Throwable cause) {
    }

    @Override
    public void onNotificationReceived(SdnrNotification notification) {
        if(notification.isControllerNotification()) {
            return;
        }
        if(notification instanceof ProblemNotification) {
            ProblemNotification problemNotification = (ProblemNotification)notification;

            if(problemNotification.getNodeName().equals(this.nodeId)) {
                this.onFilteredNotificationReceived(problemNotification);
            }
        }
    }

}
