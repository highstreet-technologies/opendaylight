/*
 * Copyright (C) 2020 highstreet technologies GmbH Intellectual Property.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.onap.ccsdk.features.sdnr.wt.odlclient.data.notifications;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ObjectDeletionNotification")
public class ObjectDeletionNotification extends BaseNotification {

    @Override
    public String toString() {
        return "ObjectDeletionNotification [getNodeName()=" + getNodeName() + ", getCounter()="
                + getCounter() + ", getTimeStamp()=" + getTimeStamp() + ", getType()=" + getType() + "]";
    }

}
