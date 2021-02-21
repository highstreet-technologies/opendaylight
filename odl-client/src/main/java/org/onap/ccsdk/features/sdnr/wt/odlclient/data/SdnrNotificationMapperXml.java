/*
 * Copyright (C) 2020 highstreet technologies GmbH Intellectual Property.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.onap.ccsdk.features.sdnr.wt.odlclient.data;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.onap.ccsdk.features.sdnr.wt.odlclient.data.notifications.AttributeValueChangedNotification;
import org.onap.ccsdk.features.sdnr.wt.odlclient.data.notifications.ObjectCreationNotification;
import org.onap.ccsdk.features.sdnr.wt.odlclient.data.notifications.ObjectDeletionNotification;
import org.onap.ccsdk.features.sdnr.wt.odlclient.data.notifications.ProblemNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SdnrNotificationMapperXml extends OdlObjectMapperXml {

    private static final Logger LOG = LoggerFactory.getLogger(SdnrNotificationMapperXml.class);
    private static final long serialVersionUID = 1L;
    private static final String REGEX = "<([^\\?].*)>";
    private static final Pattern PATTERN = Pattern.compile(REGEX, Pattern.MULTILINE);

    private final Map<String, Class<? extends SdnrNotification>> candidates;

    public SdnrNotificationMapperXml() {
        super(false);
        this.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
        this.candidates = new HashMap<>();
        this.candidates.put("AttributeValueChangedNotification",
                AttributeValueChangedNotification.class);
        this.candidates.put("ObjectCreationNotification", ObjectCreationNotification.class);
        this.candidates.put("ObjectDeletionNotification", ObjectDeletionNotification.class);
        this.candidates.put("ProblemNotification", ProblemNotification.class);

    }

    public SdnrNotification read(String xml) {
        final Matcher matcher = PATTERN.matcher(xml);
        if (matcher.find()) {
            try {
                Class<? extends SdnrNotification> clazz = this.candidates.get(matcher.group(1));
                if (clazz != null) {
                    return this.readValue(xml, clazz);
                }
            } catch (IOException e) {
                LOG.warn("problem deserializing xml {}: ", xml, e);
            }
        }
        return null;
    }
}
