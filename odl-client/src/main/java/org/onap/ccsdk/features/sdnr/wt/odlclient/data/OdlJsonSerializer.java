/*
 * Copyright (C) 2019 highstreet technologies GmbH Intellectual Property.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.onap.ccsdk.features.sdnr.wt.odlclient.data;

import org.onap.ccsdk.features.sdnr.wt.odlclient.data.serializer.SerializerElem;

public class OdlJsonSerializer extends OdlDataSerializer{

     public OdlJsonSerializer() {
        super(null);
    }


    @Override
    SerializerElem preValueWrite(String key, Object value, boolean withNsPrefix, Class<?> rootClass) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    void postValueWrite(SerializerElem e, String key) {
        // TODO Auto-generated method stub

    }

    @Override
    void onValueWrite(SerializerElem e, Object o) {
        // TODO Auto-generated method stub

    }

	@Override
	void clear() {
		// TODO Auto-generated method stub

	}

}
