/*
 * Copyright (C) 2019 highstreet technologies GmbH Intellectual Property.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.onap.ccsdk.features.sdnr.wt.odlclient.data.serializer;

import java.util.ArrayList;
import java.util.List;

public class SerializerElem {
    private final String key;
    private final String namespace;
    private final String nsPrefix;
    private final List<SerializerElem> items;
    private String value;
    public SerializerElem(String key, String namespace) {
        this(key,namespace,null);
    }
    public SerializerElem(String key, String namespace, String prefix) {
        this(key,null,namespace,prefix,new ArrayList<>());
    }
    private SerializerElem(String key, String value, String namespace, String nsPrefix, List<SerializerElem> items) {
        this.key = key;
        this.value = value;
        this.namespace = namespace;
        this.nsPrefix = nsPrefix;
        this.items = items;
    }
    public void addChild(SerializerElem e) {
       this.items.add(e);
    }
    @Override
    public String toString() {

        if(this.namespace!=null && this.namespace.length()>0) {
            if(this.nsPrefix!=null && this.nsPrefix.length()>0) {
                return String.format("<%s xmlns:%s=\"%s\">%s</%s>",this.key,this.nsPrefix,this.namespace,this.getValueWithNs(),this.key);
//                return String.format("<%s xmlns=\"%s\">%s</%s>",this.key,this.namespace,this.getValueWithNs(),this.key);
            }
            else {
                return String.format("<%s xmlns=\"%s\">%s</%s>",this.key,this.namespace,this.getValueWithNs(),this.key);
            }
        }
        else {
            return String.format("<%s>%s</%s>",this.key,this.getValueWithNs(),this.key);
        }
    }

    private String getValueWithNs() {
        if (this.value != null) {
            return (this.namespace != null && this.namespace.length() > 0)
                    ? String.format("%s:%s", this.nsPrefix, this.value)
                    : this.value;
        }
//        if (this.value != null) {
//            return (this.namespace != null && this.namespace.length() > 0)
//                    ? String.format("%s", this.value)
//                    : this.value;
//        }
        if (this.items.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (SerializerElem e : this.items) {
                sb.append(e.toString());
            }
            return sb.toString();
        }
        return "";
    }
    public void setValue(Object o) {
        this.value = o!=null?String.valueOf(o):null;
    }

}