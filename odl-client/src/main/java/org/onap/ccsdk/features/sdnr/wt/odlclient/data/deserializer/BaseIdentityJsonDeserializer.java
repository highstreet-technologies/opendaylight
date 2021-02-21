/*
 * Copyright (C) 2020 highstreet technologies GmbH Intellectual Property.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.onap.ccsdk.features.sdnr.wt.odlclient.data.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import org.onap.ccsdk.features.sdnr.wt.odlclient.data.ClassFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseIdentityJsonDeserializer<T> extends JsonDeserializer<T> {

    private static final Logger LOG = LoggerFactory.getLogger(BaseIdentityJsonDeserializer.class);
    private final JsonDeserializer<?> deser;
    private final ClassFinder clsFinder;


    public BaseIdentityJsonDeserializer(final JsonDeserializer<?> deser, ClassFinder clsFinder) {
        this.deser = deser;
        this.clsFinder = clsFinder;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        LOG.debug("BaseIdentityDeserializer class for '{}'",parser.getValueAsString());
        String clazzToSearch = parser.getValueAsString();
        // clazz from Elasticsearch is full qualified
        int lastDot = clazzToSearch.lastIndexOf(".");
        if (lastDot > -1) {
            clazzToSearch = clazzToSearch.substring(lastDot+1);
        } else {
            clazzToSearch = clazzToSearch.substring(0, 1).toUpperCase() + clazzToSearch.substring(1);
        }
        Class<?> clazz;
        try {
            clazz = ctxt.findClass(clazzToSearch);
            if (clazz != null)
                return (T)clazz;
        } catch (ClassNotFoundException e) {
            try {
                clazz = this.clsFinder.findClass(clazzToSearch);
                if (clazz != null)
                    return (T)clazz;
            } catch (ClassNotFoundException e2) {
                LOG.warn("BaseIdentityDeserializer class not found for '"+parser.getValueAsString()+"'",e2);
            }
        }
        return (T) deser.deserialize(parser, ctxt);
    }
}
