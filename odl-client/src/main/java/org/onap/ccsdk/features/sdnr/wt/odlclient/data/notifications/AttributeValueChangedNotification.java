/*
 * Copyright (C) 2020 highstreet technologies GmbH Intellectual Property.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.onap.ccsdk.features.sdnr.wt.odlclient.data.notifications;

public class AttributeValueChangedNotification extends BaseNotification {

    private String attributeName;
    private String newValue;

    @Override
    public String toString() {
        return "AttributeValueChangedNotification [attributeName=" + attributeName + ", newValue="
                + newValue + ", getNodeName()=" + getNodeName() + ", getCounter()=" + getCounter()
                + ", getTimeStamp()=" + getTimeStamp() + ", getObjectId()=" + getObjectId()
                + ", getType()=" + getType() + "]";
    }

    public String getAttributeName() {
        return this.attributeName;
    }

    public String getNewValue() {
        return this.newValue;
    }

}
