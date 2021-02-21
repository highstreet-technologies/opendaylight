/*
 * Copyright (C) 2020 highstreet technologies GmbH Intellectual Property.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.onap.ccsdk.features.sdnr.wt.odlclient.data.notifications;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.onap.ccsdk.features.sdnr.wt.odlclient.data.SdnrNotification;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.DateAndTime;

public abstract class BaseNotification implements SdnrNotification {

    private static String EMPTY = "empty";

    private String nodeName;
    private String counter;
    private String timeStamp;
    private String objectId;

    public BaseNotification() {
        // For Jaxb
        this.objectId = EMPTY;
    }

    public BaseNotification(String nodeName, Integer counter, DateAndTime timeStamp, String objectId) {
        this.nodeName = nodeName;
        this.counter = String.valueOf(counter);
        this.timeStamp = timeStamp.getValue();
        this.objectId = objectId;
        if (this.objectId == null) {
            this.objectId = EMPTY;
        }
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getCounter() {
        return counter;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getType() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean isControllerNotification() {
        final String regex = "^SDN-Controller-[0-9]*$";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(this.nodeName);
        return matcher.find();
    }

}
