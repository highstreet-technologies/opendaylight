/*
 * Copyright (C) 2020 highstreet technologies GmbH Intellectual Property.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.onap.ccsdk.features.sdnr.wt.odlclient.data.notifications;

public class ProblemNotification extends BaseNotification {

    @Override
    public String toString() {
        return "ProblemNotification [problem=" + problem + ", severity=" + severity + ", getNodeName()="
                + getNodeName() + ", getCounter()=" + getCounter() + ", getTimeStamp()=" + getTimeStamp()
                + ", getObjectId()=" + getObjectId() + ", getType()=" + getType() + "]";
    }

    private String problem;
    private String severity;

    public String getProblem() {
        return this.problem;
    }

    public String getSeverity() {
        return this.severity;
    }

}
