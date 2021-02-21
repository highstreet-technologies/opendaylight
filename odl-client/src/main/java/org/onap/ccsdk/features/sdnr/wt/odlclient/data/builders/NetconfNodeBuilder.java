/*
 * Copyright (C) 2020 highstreet technologies GmbH Intellectual Property.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.onap.ccsdk.features.sdnr.wt.odlclient.data.builders;

import com.google.common.base.MoreObjects;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Objects;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.Host;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.PortNumber;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNode;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionStatus;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.netconf.node.connection.parameters.NonModuleCapabilities;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.netconf.node.connection.parameters.OdlHelloMessageCapabilities;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.netconf.node.connection.parameters.Protocol;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.netconf.node.connection.parameters.YangModuleCapabilities;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.netconf.node.connection.status.AvailableCapabilities;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.netconf.node.connection.status.ClusteredConnectionStatus;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.netconf.node.connection.status.PassThrough;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.netconf.node.connection.status.UnavailableCapabilities;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.netconf.node.credentials.Credentials;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.netconf.schema.storage.YangLibrary;
import org.opendaylight.yangtools.concepts.Builder;
import org.opendaylight.yangtools.yang.binding.CodeHelpers;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.common.Uint16;
import org.opendaylight.yangtools.yang.common.Uint32;

/**
 * Class that builds {@link NetconfNodeBuilder} instances. Overall design of the class is that of a
 * <a href="https://en.wikipedia.org/wiki/Fluent_interface">fluent interface</a>, where method chaining is used.
 *
 * <p>
 * In general, this class is supposed to be used like this template:
 * <pre>
 *   <code>
 *     NetconfNodeBuilder createTarget(int fooXyzzy, int barBaz) {
 *         return new NetconfNodeBuilderBuilder()
 *             .setFoo(new FooBuilder().setXyzzy(fooXyzzy).build())
 *             .setBar(new BarBuilder().setBaz(barBaz).build())
 *             .build();
 *     }
 *   </code>
 * </pre>
 *
 * <p>
 * This pattern is supported by the immutable nature of NetconfNodeBuilder, as instances can be freely passed around without
 * worrying about synchronization issues.
 *
 * <p>
 * As a side note: method chaining results in:
 * <ul>
 *   <li>very efficient Java bytecode, as the method invocation result, in this case the Builder reference, is
 *       on the stack, so further method invocations just need to fill method arguments for the next method
 *       invocation, which is terminated by {@link #build()}, which is then returned from the method</li>
 *   <li>better understanding by humans, as the scope of mutable state (the builder) is kept to a minimum and is
 *       very localized</li>
 *   <li>better optimization oportunities, as the object scope is minimized in terms of invocation (rather than
 *       method) stack, making <a href="https://en.wikipedia.org/wiki/Escape_analysis">escape analysis</a> a lot
 *       easier. Given enough compiler (JIT/AOT) prowess, the cost of th builder object can be completely
 *       eliminated</li>
 * </ul>
 *
 * @see NetconfNodeBuilder
 * @see Builder
 *
 */
public class NetconfNodeBuilder implements Builder<NetconfNode> {

    private Uint16 _actorResponseWaitTime;
    private AvailableCapabilities _availableCapabilities;
    private Uint16 _betweenAttemptsTimeoutMillis;
    private ClusteredConnectionStatus _clusteredConnectionStatus;
    private Uint16 _concurrentRpcLimit;
    private String _connectedMessage;
    private NetconfNodeConnectionStatus.ConnectionStatus _connectionStatus;
    private Uint32 _connectionTimeoutMillis;
    private Credentials _credentials;
    private Uint32 _defaultRequestTimeoutMillis;
    private Host _host;
    private Uint32 _keepaliveDelay;
    private Uint32 _maxConnectionAttempts;
    private NonModuleCapabilities _nonModuleCapabilities;
    private OdlHelloMessageCapabilities _odlHelloMessageCapabilities;
    private PassThrough _passThrough;
    private PortNumber _port;
    private Protocol _protocol;
    private String _schemaCacheDirectory;
    private BigDecimal _sleepFactor;
    private UnavailableCapabilities _unavailableCapabilities;
    private YangLibrary _yangLibrary;
    private YangModuleCapabilities _yangModuleCapabilities;
    private Boolean _reconnectOnChangedSchema;
    private Boolean _schemaless;
    private Boolean _tcpOnly;



    public NetconfNodeBuilder() {
    }
    public NetconfNodeBuilder(org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeFields arg) {
        this._credentials = arg.getCredentials();
        this._host = arg.getHost();
        this._port = arg.getPort();
        this._tcpOnly = arg.isTcpOnly();
        this._protocol = arg.getProtocol();
        this._schemaless = arg.isSchemaless();
        this._yangModuleCapabilities = arg.getYangModuleCapabilities();
        this._nonModuleCapabilities = arg.getNonModuleCapabilities();
        this._reconnectOnChangedSchema = arg.isReconnectOnChangedSchema();
        this._connectionTimeoutMillis = arg.getConnectionTimeoutMillis();
        this._defaultRequestTimeoutMillis = arg.getDefaultRequestTimeoutMillis();
        this._maxConnectionAttempts = arg.getMaxConnectionAttempts();
        this._betweenAttemptsTimeoutMillis = arg.getBetweenAttemptsTimeoutMillis();
        this._sleepFactor = arg.getSleepFactor();
        this._keepaliveDelay = arg.getKeepaliveDelay();
        this._concurrentRpcLimit = arg.getConcurrentRpcLimit();
        this._actorResponseWaitTime = arg.getActorResponseWaitTime();
        this._odlHelloMessageCapabilities = arg.getOdlHelloMessageCapabilities();
        this._connectionStatus = arg.getConnectionStatus();
        this._clusteredConnectionStatus = arg.getClusteredConnectionStatus();
        this._connectedMessage = arg.getConnectedMessage();
        this._availableCapabilities = arg.getAvailableCapabilities();
        this._unavailableCapabilities = arg.getUnavailableCapabilities();
        this._passThrough = arg.getPassThrough();
        this._schemaCacheDirectory = arg.getSchemaCacheDirectory();
        this._yangLibrary = arg.getYangLibrary();
    }
    public NetconfNodeBuilder(org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeCredentials arg) {
        this._credentials = arg.getCredentials();
    }
    public NetconfNodeBuilder(org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionParameters arg) {
        this._host = arg.getHost();
        this._port = arg.getPort();
        this._tcpOnly = arg.isTcpOnly();
        this._protocol = arg.getProtocol();
        this._schemaless = arg.isSchemaless();
        this._yangModuleCapabilities = arg.getYangModuleCapabilities();
        this._nonModuleCapabilities = arg.getNonModuleCapabilities();
        this._reconnectOnChangedSchema = arg.isReconnectOnChangedSchema();
        this._connectionTimeoutMillis = arg.getConnectionTimeoutMillis();
        this._defaultRequestTimeoutMillis = arg.getDefaultRequestTimeoutMillis();
        this._maxConnectionAttempts = arg.getMaxConnectionAttempts();
        this._betweenAttemptsTimeoutMillis = arg.getBetweenAttemptsTimeoutMillis();
        this._sleepFactor = arg.getSleepFactor();
        this._keepaliveDelay = arg.getKeepaliveDelay();
        this._concurrentRpcLimit = arg.getConcurrentRpcLimit();
        this._actorResponseWaitTime = arg.getActorResponseWaitTime();
        this._odlHelloMessageCapabilities = arg.getOdlHelloMessageCapabilities();
    }
    public NetconfNodeBuilder(org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionStatus arg) {
        this._connectionStatus = arg.getConnectionStatus();
        this._clusteredConnectionStatus = arg.getClusteredConnectionStatus();
        this._connectedMessage = arg.getConnectedMessage();
        this._availableCapabilities = arg.getAvailableCapabilities();
        this._unavailableCapabilities = arg.getUnavailableCapabilities();
        this._passThrough = arg.getPassThrough();
    }
    public NetconfNodeBuilder(org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfSchemaStorage arg) {
        this._schemaCacheDirectory = arg.getSchemaCacheDirectory();
        this._yangLibrary = arg.getYangLibrary();
    }

    public NetconfNodeBuilder(NetconfNode base) {
        this._actorResponseWaitTime = base.getActorResponseWaitTime();
        this._availableCapabilities = base.getAvailableCapabilities();
        this._betweenAttemptsTimeoutMillis = base.getBetweenAttemptsTimeoutMillis();
        this._clusteredConnectionStatus = base.getClusteredConnectionStatus();
        this._concurrentRpcLimit = base.getConcurrentRpcLimit();
        this._connectedMessage = base.getConnectedMessage();
        this._connectionStatus = base.getConnectionStatus();
        this._connectionTimeoutMillis = base.getConnectionTimeoutMillis();
        this._credentials = base.getCredentials();
        this._defaultRequestTimeoutMillis = base.getDefaultRequestTimeoutMillis();
        this._host = base.getHost();
        this._keepaliveDelay = base.getKeepaliveDelay();
        this._maxConnectionAttempts = base.getMaxConnectionAttempts();
        this._nonModuleCapabilities = base.getNonModuleCapabilities();
        this._odlHelloMessageCapabilities = base.getOdlHelloMessageCapabilities();
        this._passThrough = base.getPassThrough();
        this._port = base.getPort();
        this._protocol = base.getProtocol();
        this._schemaCacheDirectory = base.getSchemaCacheDirectory();
        this._sleepFactor = base.getSleepFactor();
        this._unavailableCapabilities = base.getUnavailableCapabilities();
        this._yangLibrary = base.getYangLibrary();
        this._yangModuleCapabilities = base.getYangModuleCapabilities();
        this._reconnectOnChangedSchema = base.isReconnectOnChangedSchema();
        this._schemaless = base.isSchemaless();
        this._tcpOnly = base.isTcpOnly();
    }

    /**
     * Set fields from given grouping argument. Valid argument is instance of one of following types:
     * <ul>
     * <li>org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeCredentials</li>
     * <li>org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionStatus</li>
     * <li>org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfSchemaStorage</li>
     * <li>org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeFields</li>
     * <li>org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionParameters</li>
     * </ul>
     *
     * @param arg grouping object
     * @throws IllegalArgumentException if given argument is none of valid types
    */
    public void fieldsFrom(DataObject arg) {
        boolean isValidArg = false;
        if (arg instanceof org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeCredentials) {
            this._credentials = ((org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeCredentials)arg).getCredentials();
            isValidArg = true;
        }
        if (arg instanceof org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionStatus) {
            this._connectionStatus = ((org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionStatus)arg).getConnectionStatus();
            this._clusteredConnectionStatus = ((org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionStatus)arg).getClusteredConnectionStatus();
            this._connectedMessage = ((org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionStatus)arg).getConnectedMessage();
            this._availableCapabilities = ((org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionStatus)arg).getAvailableCapabilities();
            this._unavailableCapabilities = ((org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionStatus)arg).getUnavailableCapabilities();
            this._passThrough = ((org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionStatus)arg).getPassThrough();
            isValidArg = true;
        }
        if (arg instanceof org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfSchemaStorage) {
            this._schemaCacheDirectory = ((org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfSchemaStorage)arg).getSchemaCacheDirectory();
            this._yangLibrary = ((org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfSchemaStorage)arg).getYangLibrary();
            isValidArg = true;
        }
        if (arg instanceof org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeFields) {
            isValidArg = true;
        }
        if (arg instanceof org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionParameters) {
            this._host = ((org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionParameters)arg).getHost();
            this._port = ((org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionParameters)arg).getPort();
            this._tcpOnly = ((org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionParameters)arg).isTcpOnly();
            this._protocol = ((org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionParameters)arg).getProtocol();
            this._schemaless = ((org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionParameters)arg).isSchemaless();
            this._yangModuleCapabilities = ((org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionParameters)arg).getYangModuleCapabilities();
            this._nonModuleCapabilities = ((org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionParameters)arg).getNonModuleCapabilities();
            this._reconnectOnChangedSchema = ((org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionParameters)arg).isReconnectOnChangedSchema();
            this._connectionTimeoutMillis = ((org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionParameters)arg).getConnectionTimeoutMillis();
            this._defaultRequestTimeoutMillis = ((org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionParameters)arg).getDefaultRequestTimeoutMillis();
            this._maxConnectionAttempts = ((org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionParameters)arg).getMaxConnectionAttempts();
            this._betweenAttemptsTimeoutMillis = ((org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionParameters)arg).getBetweenAttemptsTimeoutMillis();
            this._sleepFactor = ((org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionParameters)arg).getSleepFactor();
            this._keepaliveDelay = ((org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionParameters)arg).getKeepaliveDelay();
            this._concurrentRpcLimit = ((org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionParameters)arg).getConcurrentRpcLimit();
            this._actorResponseWaitTime = ((org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionParameters)arg).getActorResponseWaitTime();
            this._odlHelloMessageCapabilities = ((org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionParameters)arg).getOdlHelloMessageCapabilities();
            isValidArg = true;
        }
        CodeHelpers.validValue(isValidArg, arg, "[org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeCredentials, org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionStatus, org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfSchemaStorage, org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeFields, org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionParameters]");
    }

    public Uint16 getActorResponseWaitTime() {
        return _actorResponseWaitTime;
    }

    public AvailableCapabilities getAvailableCapabilities() {
        return _availableCapabilities;
    }

    public Uint16 getBetweenAttemptsTimeoutMillis() {
        return _betweenAttemptsTimeoutMillis;
    }

    public ClusteredConnectionStatus getClusteredConnectionStatus() {
        return _clusteredConnectionStatus;
    }

    public Uint16 getConcurrentRpcLimit() {
        return _concurrentRpcLimit;
    }

    public String getConnectedMessage() {
        return _connectedMessage;
    }

    public NetconfNodeConnectionStatus.ConnectionStatus getConnectionStatus() {
        return _connectionStatus;
    }

    public Uint32 getConnectionTimeoutMillis() {
        return _connectionTimeoutMillis;
    }

    public Credentials getCredentials() {
        return _credentials;
    }

    public Uint32 getDefaultRequestTimeoutMillis() {
        return _defaultRequestTimeoutMillis;
    }

    public Host getHost() {
        return _host;
    }

    public Uint32 getKeepaliveDelay() {
        return _keepaliveDelay;
    }

    public Uint32 getMaxConnectionAttempts() {
        return _maxConnectionAttempts;
    }

    public NonModuleCapabilities getNonModuleCapabilities() {
        return _nonModuleCapabilities;
    }

    public OdlHelloMessageCapabilities getOdlHelloMessageCapabilities() {
        return _odlHelloMessageCapabilities;
    }

    public PassThrough getPassThrough() {
        return _passThrough;
    }

    public PortNumber getPort() {
        return _port;
    }

    public Protocol getProtocol() {
        return _protocol;
    }

    public String getSchemaCacheDirectory() {
        return _schemaCacheDirectory;
    }

    public BigDecimal getSleepFactor() {
        return _sleepFactor;
    }

    public UnavailableCapabilities getUnavailableCapabilities() {
        return _unavailableCapabilities;
    }

    public YangLibrary getYangLibrary() {
        return _yangLibrary;
    }

    public YangModuleCapabilities getYangModuleCapabilities() {
        return _yangModuleCapabilities;
    }

    public Boolean isReconnectOnChangedSchema() {
        return _reconnectOnChangedSchema;
    }

    public Boolean isSchemaless() {
        return _schemaless;
    }

    public Boolean isTcpOnly() {
        return _tcpOnly;
    }


    private static void checkActorResponseWaitTimeRange(final int value) {
        if (value >= 1) {
            return;
        }
        CodeHelpers.throwInvalidRange("[[1..65535]]", value);
    }

    public NetconfNodeBuilder setActorResponseWaitTime(final Uint16 value) {
        if (value != null) {
            checkActorResponseWaitTimeRange(value.intValue());

        }
        this._actorResponseWaitTime = value;
        return this;
    }

    /**
     * Utility migration setter.
     *
     * @param value field value in legacy type
     * @return this builder
     * #@deprecated Use {#link setActorResponseWaitTime(Uint16)} instead.
     */
//    @Deprecated(forRemoval = true)
//    public NetconfNodeBuilder setActorResponseWaitTime(final Integer value) {
//        return setActorResponseWaitTime(CodeHelpers.compatUint(value));
//    }

    public NetconfNodeBuilder setAvailableCapabilities(final AvailableCapabilities value) {
        this._availableCapabilities = value;
        return this;
    }

    public NetconfNodeBuilder setBetweenAttemptsTimeoutMillis(final Uint16 value) {
        this._betweenAttemptsTimeoutMillis = value;
        return this;
    }

    /**
     * Utility migration setter.
     *
     * @param value field value in legacy type
     * @return this builder
     * #@deprecated Use {#link setBetweenAttemptsTimeoutMillis(Uint16)} instead.
     */
//    @Deprecated(forRemoval = true)
//    public NetconfNodeBuilder setBetweenAttemptsTimeoutMillis(final Integer value) {
//        return setBetweenAttemptsTimeoutMillis(CodeHelpers.compatUint(value));
//    }

    public NetconfNodeBuilder setClusteredConnectionStatus(final ClusteredConnectionStatus value) {
        this._clusteredConnectionStatus = value;
        return this;
    }

    public NetconfNodeBuilder setConcurrentRpcLimit(final Uint16 value) {
        this._concurrentRpcLimit = value;
        return this;
    }

    /**
     * Utility migration setter.
     *
     * @param value field value in legacy type
     * @return this builder
     * #@deprecated Use {#link setConcurrentRpcLimit(Uint16)} instead.
     */
//    @Deprecated(forRemoval = true)
//    public NetconfNodeBuilder setConcurrentRpcLimit(final Integer value) {
//        return setConcurrentRpcLimit(CodeHelpers.compatUint(value));
//    }

    public NetconfNodeBuilder setConnectedMessage(final String value) {
        this._connectedMessage = value;
        return this;
    }

    public NetconfNodeBuilder setConnectionStatus(final NetconfNodeConnectionStatus.ConnectionStatus value) {
        this._connectionStatus = value;
        return this;
    }

    public NetconfNodeBuilder setConnectionTimeoutMillis(final Uint32 value) {
        this._connectionTimeoutMillis = value;
        return this;
    }

    /**
     * Utility migration setter.
     *
     * @param value field value in legacy type
     * @return this builder
     * #@deprecated Use {#link setConnectionTimeoutMillis(Uint32)} instead.
     */
//    @Deprecated(forRemoval = true)
//    public NetconfNodeBuilder setConnectionTimeoutMillis(final Long value) {
//        return setConnectionTimeoutMillis(CodeHelpers.compatUint(value));
//    }

    public NetconfNodeBuilder setCredentials(final Credentials value) {
        this._credentials = value;
        return this;
    }

    public NetconfNodeBuilder setDefaultRequestTimeoutMillis(final Uint32 value) {
        this._defaultRequestTimeoutMillis = value;
        return this;
    }

    /**
     * Utility migration setter.
     *
     * @param value field value in legacy type
     * @return this builder
     * #@deprecated Use {#link setDefaultRequestTimeoutMillis(Uint32)} instead.
     */
//    @Deprecated(forRemoval = true)
//    public NetconfNodeBuilder setDefaultRequestTimeoutMillis(final Long value) {
//        return setDefaultRequestTimeoutMillis(CodeHelpers.compatUint(value));
//    }

    public NetconfNodeBuilder setHost(final Host value) {
        this._host = value;
        return this;
    }

    public NetconfNodeBuilder setKeepaliveDelay(final Uint32 value) {
        this._keepaliveDelay = value;
        return this;
    }

    /**
     * Utility migration setter.
     *
     * @param value field value in legacy type
     * @return this builder
     * #@deprecated Use {#link setKeepaliveDelay(Uint32)} instead.
     */
//    @Deprecated(forRemoval = true)
//    public NetconfNodeBuilder setKeepaliveDelay(final Long value) {
//        return setKeepaliveDelay(CodeHelpers.compatUint(value));
//    }

    public NetconfNodeBuilder setMaxConnectionAttempts(final Uint32 value) {
        this._maxConnectionAttempts = value;
        return this;
    }

    /**
     * Utility migration setter.
     *
     * @param value field value in legacy type
     * @return this builder
     * #@deprecated Use {#link setMaxConnectionAttempts(Uint32)} instead.
     */
//    @Deprecated(forRemoval = true)
//    public NetconfNodeBuilder setMaxConnectionAttempts(final Long value) {
//        return setMaxConnectionAttempts(CodeHelpers.compatUint(value));
//    }

    public NetconfNodeBuilder setNonModuleCapabilities(final NonModuleCapabilities value) {
        this._nonModuleCapabilities = value;
        return this;
    }

    public NetconfNodeBuilder setOdlHelloMessageCapabilities(final OdlHelloMessageCapabilities value) {
        this._odlHelloMessageCapabilities = value;
        return this;
    }

    public NetconfNodeBuilder setPassThrough(final PassThrough value) {
        this._passThrough = value;
        return this;
    }

    public NetconfNodeBuilder setPort(final PortNumber value) {
        this._port = value;
        return this;
    }

    public NetconfNodeBuilder setProtocol(final Protocol value) {
        this._protocol = value;
        return this;
    }

    public NetconfNodeBuilder setSchemaCacheDirectory(final String value) {
        this._schemaCacheDirectory = value;
        return this;
    }


    public NetconfNodeBuilder setSleepFactor(final BigDecimal value) {
        if (value != null) {

        }
        this._sleepFactor = value;
        return this;
    }

    public NetconfNodeBuilder setUnavailableCapabilities(final UnavailableCapabilities value) {
        this._unavailableCapabilities = value;
        return this;
    }

    public NetconfNodeBuilder setYangLibrary(final YangLibrary value) {
        this._yangLibrary = value;
        return this;
    }

    public NetconfNodeBuilder setYangModuleCapabilities(final YangModuleCapabilities value) {
        this._yangModuleCapabilities = value;
        return this;
    }

    public NetconfNodeBuilder setReconnectOnChangedSchema(final Boolean value) {
        this._reconnectOnChangedSchema = value;
        return this;
    }

    public NetconfNodeBuilder setSchemaless(final Boolean value) {
        this._schemaless = value;
        return this;
    }

    public NetconfNodeBuilder setTcpOnly(final Boolean value) {
        this._tcpOnly = value;
        return this;
    }


    @Override
    public NetconfNode build() {
        return new NetconfNodeImpl(this);
    }

    private static final class NetconfNodeImpl
        implements NetconfNode {

        private final Uint16 _actorResponseWaitTime;
        private final AvailableCapabilities _availableCapabilities;
        private final Uint16 _betweenAttemptsTimeoutMillis;
        private final ClusteredConnectionStatus _clusteredConnectionStatus;
        private final Uint16 _concurrentRpcLimit;
        private final String _connectedMessage;
        private final NetconfNodeConnectionStatus.ConnectionStatus _connectionStatus;
        private final Uint32 _connectionTimeoutMillis;
        private final Credentials _credentials;
        private final Uint32 _defaultRequestTimeoutMillis;
        private final Host _host;
        private final Uint32 _keepaliveDelay;
        private final Uint32 _maxConnectionAttempts;
        private final NonModuleCapabilities _nonModuleCapabilities;
        private final OdlHelloMessageCapabilities _odlHelloMessageCapabilities;
        private final PassThrough _passThrough;
        private final PortNumber _port;
        private final Protocol _protocol;
        private final String _schemaCacheDirectory;
        private final BigDecimal _sleepFactor;
        private final UnavailableCapabilities _unavailableCapabilities;
        private final YangLibrary _yangLibrary;
        private final YangModuleCapabilities _yangModuleCapabilities;
        private final Boolean _reconnectOnChangedSchema;
        private final Boolean _schemaless;
        private final Boolean _tcpOnly;

        NetconfNodeImpl(NetconfNodeBuilder base) {
            this._actorResponseWaitTime = base.getActorResponseWaitTime();
            this._availableCapabilities = base.getAvailableCapabilities();
            this._betweenAttemptsTimeoutMillis = base.getBetweenAttemptsTimeoutMillis();
            this._clusteredConnectionStatus = base.getClusteredConnectionStatus();
            this._concurrentRpcLimit = base.getConcurrentRpcLimit();
            this._connectedMessage = base.getConnectedMessage();
            this._connectionStatus = base.getConnectionStatus();
            this._connectionTimeoutMillis = base.getConnectionTimeoutMillis();
            this._credentials = base.getCredentials();
            this._defaultRequestTimeoutMillis = base.getDefaultRequestTimeoutMillis();
            this._host = base.getHost();
            this._keepaliveDelay = base.getKeepaliveDelay();
            this._maxConnectionAttempts = base.getMaxConnectionAttempts();
            this._nonModuleCapabilities = base.getNonModuleCapabilities();
            this._odlHelloMessageCapabilities = base.getOdlHelloMessageCapabilities();
            this._passThrough = base.getPassThrough();
            this._port = base.getPort();
            this._protocol = base.getProtocol();
            this._schemaCacheDirectory = base.getSchemaCacheDirectory();
            this._sleepFactor = base.getSleepFactor();
            this._unavailableCapabilities = base.getUnavailableCapabilities();
            this._yangLibrary = base.getYangLibrary();
            this._yangModuleCapabilities = base.getYangModuleCapabilities();
            this._reconnectOnChangedSchema = base.isReconnectOnChangedSchema();
            this._schemaless = base.isSchemaless();
            this._tcpOnly = base.isTcpOnly();
        }

        @Override
        public Uint16 getActorResponseWaitTime() {
            return _actorResponseWaitTime;
        }

        @Override
        public AvailableCapabilities getAvailableCapabilities() {
            return _availableCapabilities;
        }

        @Override
        public Uint16 getBetweenAttemptsTimeoutMillis() {
            return _betweenAttemptsTimeoutMillis;
        }

        @Override
        public ClusteredConnectionStatus getClusteredConnectionStatus() {
            return _clusteredConnectionStatus;
        }

        @Override
        public Uint16 getConcurrentRpcLimit() {
            return _concurrentRpcLimit;
        }

        @Override
        public String getConnectedMessage() {
            return _connectedMessage;
        }

        @Override
        public NetconfNodeConnectionStatus.ConnectionStatus getConnectionStatus() {
            return _connectionStatus;
        }

        @Override
        public Uint32 getConnectionTimeoutMillis() {
            return _connectionTimeoutMillis;
        }

        @Override
        public Credentials getCredentials() {
            return _credentials;
        }

        @Override
        public Uint32 getDefaultRequestTimeoutMillis() {
            return _defaultRequestTimeoutMillis;
        }

        @Override
        public Host getHost() {
            return _host;
        }

        @Override
        public Uint32 getKeepaliveDelay() {
            return _keepaliveDelay;
        }

        @Override
        public Uint32 getMaxConnectionAttempts() {
            return _maxConnectionAttempts;
        }

        @Override
        public NonModuleCapabilities getNonModuleCapabilities() {
            return _nonModuleCapabilities;
        }

        @Override
        public OdlHelloMessageCapabilities getOdlHelloMessageCapabilities() {
            return _odlHelloMessageCapabilities;
        }

        @Override
        public PassThrough getPassThrough() {
            return _passThrough;
        }

        @Override
        public PortNumber getPort() {
            return _port;
        }

        @Override
        public Protocol getProtocol() {
            return _protocol;
        }

        @Override
        public String getSchemaCacheDirectory() {
            return _schemaCacheDirectory;
        }

        @Override
        public BigDecimal getSleepFactor() {
            return _sleepFactor;
        }

        @Override
        public UnavailableCapabilities getUnavailableCapabilities() {
            return _unavailableCapabilities;
        }

        @Override
        public YangLibrary getYangLibrary() {
            return _yangLibrary;
        }

        @Override
        public YangModuleCapabilities getYangModuleCapabilities() {
            return _yangModuleCapabilities;
        }

        @Override
        public Boolean isReconnectOnChangedSchema() {
            return _reconnectOnChangedSchema;
        }

        @Override
        public Boolean isSchemaless() {
            return _schemaless;
        }

        @Override
        public Boolean isTcpOnly() {
            return _tcpOnly;
        }

        private int hash = 0;
        private volatile boolean hashValid = false;

        @Override
        public int hashCode() {
            if (hashValid) {
                return hash;
            }

            final int prime = 31;
            int result = 1;
            result = prime * result + Objects.hashCode(_actorResponseWaitTime);
            result = prime * result + Objects.hashCode(_availableCapabilities);
            result = prime * result + Objects.hashCode(_betweenAttemptsTimeoutMillis);
            result = prime * result + Objects.hashCode(_clusteredConnectionStatus);
            result = prime * result + Objects.hashCode(_concurrentRpcLimit);
            result = prime * result + Objects.hashCode(_connectedMessage);
            result = prime * result + Objects.hashCode(_connectionStatus);
            result = prime * result + Objects.hashCode(_connectionTimeoutMillis);
            result = prime * result + Objects.hashCode(_credentials);
            result = prime * result + Objects.hashCode(_defaultRequestTimeoutMillis);
            result = prime * result + Objects.hashCode(_host);
            result = prime * result + Objects.hashCode(_keepaliveDelay);
            result = prime * result + Objects.hashCode(_maxConnectionAttempts);
            result = prime * result + Objects.hashCode(_nonModuleCapabilities);
            result = prime * result + Objects.hashCode(_odlHelloMessageCapabilities);
            result = prime * result + Objects.hashCode(_passThrough);
            result = prime * result + Objects.hashCode(_port);
            result = prime * result + Objects.hashCode(_protocol);
            result = prime * result + Objects.hashCode(_schemaCacheDirectory);
            result = prime * result + Objects.hashCode(_sleepFactor);
            result = prime * result + Objects.hashCode(_unavailableCapabilities);
            result = prime * result + Objects.hashCode(_yangLibrary);
            result = prime * result + Objects.hashCode(_yangModuleCapabilities);
            result = prime * result + Objects.hashCode(_reconnectOnChangedSchema);
            result = prime * result + Objects.hashCode(_schemaless);
            result = prime * result + Objects.hashCode(_tcpOnly);

            hash = result;
            hashValid = true;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof DataObject)) {
                return false;
            }
            if (!NetconfNode.class.equals(((DataObject)obj).implementedInterface())) {
                return false;
            }
            NetconfNode other = (NetconfNode)obj;
            if (!Objects.equals(_actorResponseWaitTime, other.getActorResponseWaitTime())) {
                return false;
            }
            if (!Objects.equals(_availableCapabilities, other.getAvailableCapabilities())) {
                return false;
            }
            if (!Objects.equals(_betweenAttemptsTimeoutMillis, other.getBetweenAttemptsTimeoutMillis())) {
                return false;
            }
            if (!Objects.equals(_clusteredConnectionStatus, other.getClusteredConnectionStatus())) {
                return false;
            }
            if (!Objects.equals(_concurrentRpcLimit, other.getConcurrentRpcLimit())) {
                return false;
            }
            if (!Objects.equals(_connectedMessage, other.getConnectedMessage())) {
                return false;
            }
            if (!Objects.equals(_connectionStatus, other.getConnectionStatus())) {
                return false;
            }
            if (!Objects.equals(_connectionTimeoutMillis, other.getConnectionTimeoutMillis())) {
                return false;
            }
            if (!Objects.equals(_credentials, other.getCredentials())) {
                return false;
            }
            if (!Objects.equals(_defaultRequestTimeoutMillis, other.getDefaultRequestTimeoutMillis())) {
                return false;
            }
            if (!Objects.equals(_host, other.getHost())) {
                return false;
            }
            if (!Objects.equals(_keepaliveDelay, other.getKeepaliveDelay())) {
                return false;
            }
            if (!Objects.equals(_maxConnectionAttempts, other.getMaxConnectionAttempts())) {
                return false;
            }
            if (!Objects.equals(_nonModuleCapabilities, other.getNonModuleCapabilities())) {
                return false;
            }
            if (!Objects.equals(_odlHelloMessageCapabilities, other.getOdlHelloMessageCapabilities())) {
                return false;
            }
            if (!Objects.equals(_passThrough, other.getPassThrough())) {
                return false;
            }
            if (!Objects.equals(_port, other.getPort())) {
                return false;
            }
            if (!Objects.equals(_protocol, other.getProtocol())) {
                return false;
            }
            if (!Objects.equals(_schemaCacheDirectory, other.getSchemaCacheDirectory())) {
                return false;
            }
            if (!Objects.equals(_sleepFactor, other.getSleepFactor())) {
                return false;
            }
            if (!Objects.equals(_unavailableCapabilities, other.getUnavailableCapabilities())) {
                return false;
            }
            if (!Objects.equals(_yangLibrary, other.getYangLibrary())) {
                return false;
            }
            if (!Objects.equals(_yangModuleCapabilities, other.getYangModuleCapabilities())) {
                return false;
            }
            if (!Objects.equals(_reconnectOnChangedSchema, other.isReconnectOnChangedSchema())) {
                return false;
            }
            if (!Objects.equals(_schemaless, other.isSchemaless())) {
                return false;
            }
            if (!Objects.equals(_tcpOnly, other.isTcpOnly())) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            final MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper("NetconfNode");
            CodeHelpers.appendValue(helper, "_actorResponseWaitTime", _actorResponseWaitTime);
            CodeHelpers.appendValue(helper, "_availableCapabilities", _availableCapabilities);
            CodeHelpers.appendValue(helper, "_betweenAttemptsTimeoutMillis", _betweenAttemptsTimeoutMillis);
            CodeHelpers.appendValue(helper, "_clusteredConnectionStatus", _clusteredConnectionStatus);
            CodeHelpers.appendValue(helper, "_concurrentRpcLimit", _concurrentRpcLimit);
            CodeHelpers.appendValue(helper, "_connectedMessage", _connectedMessage);
            CodeHelpers.appendValue(helper, "_connectionStatus", _connectionStatus);
            CodeHelpers.appendValue(helper, "_connectionTimeoutMillis", _connectionTimeoutMillis);
            CodeHelpers.appendValue(helper, "_credentials", _credentials);
            CodeHelpers.appendValue(helper, "_defaultRequestTimeoutMillis", _defaultRequestTimeoutMillis);
            CodeHelpers.appendValue(helper, "_host", _host);
            CodeHelpers.appendValue(helper, "_keepaliveDelay", _keepaliveDelay);
            CodeHelpers.appendValue(helper, "_maxConnectionAttempts", _maxConnectionAttempts);
            CodeHelpers.appendValue(helper, "_nonModuleCapabilities", _nonModuleCapabilities);
            CodeHelpers.appendValue(helper, "_odlHelloMessageCapabilities", _odlHelloMessageCapabilities);
            CodeHelpers.appendValue(helper, "_passThrough", _passThrough);
            CodeHelpers.appendValue(helper, "_port", _port);
            CodeHelpers.appendValue(helper, "_protocol", _protocol);
            CodeHelpers.appendValue(helper, "_schemaCacheDirectory", _schemaCacheDirectory);
            CodeHelpers.appendValue(helper, "_sleepFactor", _sleepFactor);
            CodeHelpers.appendValue(helper, "_unavailableCapabilities", _unavailableCapabilities);
            CodeHelpers.appendValue(helper, "_yangLibrary", _yangLibrary);
            CodeHelpers.appendValue(helper, "_yangModuleCapabilities", _yangModuleCapabilities);
            CodeHelpers.appendValue(helper, "_reconnectOnChangedSchema", _reconnectOnChangedSchema);
            CodeHelpers.appendValue(helper, "_schemaless", _schemaless);
            CodeHelpers.appendValue(helper, "_tcpOnly", _tcpOnly);
            return helper.toString();
        }
    }
}
