/*
 * Copyright (C) 2020 highstreet technologies GmbH Intellectual Property.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.onap.ccsdk.features.sdnr.wt.odlclient.data;

/**
 * marker interface for websocket notifications by sdnr websocket.
 * @author jack
 *
 */
public interface SdnrNotification {

    boolean isControllerNotification();

}
