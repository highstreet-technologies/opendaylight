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
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TypeObjectJsonDeserializer<T> extends JsonDeserializer<T> {

    private static final Logger LOG = LoggerFactory.getLogger(TypeObjectJsonDeserializer.class);
    private static final String TYPEOBJECT_INSTANCE_METHOD = "getDefaultInstance";
    private final JavaType type;
    private final JsonDeserializer<?> deser;

    public TypeObjectJsonDeserializer(JavaType type, JsonDeserializer<?> deser) {
        this.type = type;
        this.deser = deser;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T deserialize(JsonParser parser, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        Class<?> clazz = type.getRawClass();
        final String arg = parser.getValueAsString();
        try {

            if (hasClassDeclaredMethod(clazz, TYPEOBJECT_INSTANCE_METHOD)) {
                Method method = clazz.getDeclaredMethod(TYPEOBJECT_INSTANCE_METHOD, String.class);
                T res = (T) method.invoke(null, arg);
                return res;
            } else {
                //try to find builder with getDefaultInstance method
                try {
                    Class<?> builderClazz = findBuilderClass(ctxt,clazz);
                    if (hasClassDeclaredMethod(builderClazz, TYPEOBJECT_INSTANCE_METHOD)) {
                        Method method = builderClazz.getDeclaredMethod(TYPEOBJECT_INSTANCE_METHOD, String.class);
                        T res = (T) method.invoke(null, arg);
                        return res;
                    }
                } catch (ClassNotFoundException e) {

                }


                // find constructor argument types
                List<Class<?>> ctypes = getConstructorParameterTypes(clazz, String.class);
                for (Class<?> ctype : ctypes) {
                    if (ctype.equals(String.class)) {
                        return (T) clazz.getConstructor(ctype).newInstance(arg);
                    } else if (hasClassDeclaredMethod(ctype, TYPEOBJECT_INSTANCE_METHOD)) {
                        Method method = ctype.getDeclaredMethod(TYPEOBJECT_INSTANCE_METHOD, String.class);
                        return (T) clazz.getConstructor(ctype)
                                .newInstance(method.invoke(null, arg));
                    } else {
                        // TODO: recursive instantiation down to string constructor or
                        // getDefaultInstance method
                    }
                }

            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | NoSuchElementException | SecurityException
                | InstantiationException e) {
            LOG.warn("problem deserializing {} with value {}: {}", clazz.getName(), arg, e);
        }
        return (T) deser.deserialize(parser, ctxt);
    }

    private Class<?> findBuilderClass(DeserializationContext ctxt,Class<?> clazz) throws ClassNotFoundException{
        return ctxt.findClass(clazz.getName() + "Builder");
    }
    private static boolean hasClassDeclaredMethod(Class<?> clazz, String name) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
    private static List<Class<?>> getConstructorParameterTypes(Class<?> clazz, Class<?> prefer) {

        Constructor<?>[] constructors = clazz.getConstructors();
        List<Class<?>> res = new ArrayList<>();
        for (Constructor<?> c : constructors) {
            Class<?>[] ptypes = c.getParameterTypes();
            if (ptypes.length == 1) {
                res.add(ptypes[0]);
            }

            if (prefer != null && ptypes.length == 1 && ptypes[0].equals(prefer)) {
                return Arrays.asList(prefer);
            }
        }
        return res;
    }
}
