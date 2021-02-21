/*
 * Copyright (C) 2020 highstreet technologies GmbH Intellectual Property.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.onap.ccsdk.features.sdnr.wt.odlclient.remote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import org.eclipse.jdt.annotation.Nullable;
import org.onap.ccsdk.features.sdnr.wt.odlclient.data.DeviceConnectionChangedHandler;
import org.onap.ccsdk.features.sdnr.wt.odlclient.data.SdnrNotification;
import org.onap.ccsdk.features.sdnr.wt.odlclient.data.notifications.AttributeValueChangedNotification;
import org.onap.ccsdk.features.sdnr.wt.odlclient.data.notifications.ObjectCreationNotification;
import org.onap.ccsdk.features.sdnr.wt.odlclient.data.notifications.ObjectDeletionNotification;
import org.onap.ccsdk.features.sdnr.wt.odlclient.restconf.RestconfHttpClient;
import org.opendaylight.mdsal.common.api.LogicalDatastoreType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNode;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionStatus.ConnectionStatus;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.network.topology.topology.topology.types.TopologyNetconf;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.NetworkTopology;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.NodeId;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.TopologyId;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.Topology;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.TopologyKey;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.Node;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.NodeKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoteDeviceConnectionChangeProvider {

    private static final InstanceIdentifier<Topology> NETCONF_TOPO_IID = InstanceIdentifier
            .create(NetworkTopology.class).child(Topology.class,
                    new TopologyKey(new TopologyId(TopologyNetconf.QNAME.getLocalName())));

    private static final Logger LOG = LoggerFactory
            .getLogger(RemoteDeviceConnectionChangeProvider.class);
    private final RestconfHttpClient client;
    private final List<DeviceConnectionChangedHandler> listeners;

    public RemoteDeviceConnectionChangeProvider(RestconfHttpClient restClient) {
        this.client = restClient;
        this.listeners = new ArrayList<>();
    }

    public void onControllerNotification(SdnrNotification notification) {
        if (notification.isControllerNotification()) {
            if (notification instanceof ObjectCreationNotification) {
                LOG.debug("handle create notification");
                String nodeId = ((ObjectCreationNotification) notification).getObjectId();
                this.handleChange(nodeId);

            } else if (notification instanceof ObjectDeletionNotification) {
                LOG.debug("handle delete notification");
                String nodeId = ((ObjectCreationNotification) notification).getObjectId();
                this.pushDisconnect(nodeId);

            } else if (notification instanceof AttributeValueChangedNotification) {
                AttributeValueChangedNotification notification2 = (AttributeValueChangedNotification) notification;
                LOG.debug("handle change notification for {}",notification2.getAttributeName());
                if (notification2.getAttributeName().equals("ConnectionStatus")) {
                    String nodeId = notification2.getObjectId();
                    this.handleChange(nodeId);
                }
            } else {
                LOG.debug("notification {} ignored by type", notification);
            }
        } else {
            LOG.debug("notification {} ignored", notification);
        }
    }

    private void handleChange(String nodeId) {
        InstanceIdentifier<NetconfNode> nodeIif = NETCONF_TOPO_IID.child(Node.class,new NodeKey(new NodeId(nodeId)))
                .augmentation(NetconfNode.class);
        try {
            LOG.debug("read remote netconfnode for node {}",nodeId);
            Optional<NetconfNode> netconfNode = this.client.read(LogicalDatastoreType.CONFIGURATION, nodeIif)
                    .get();
            this.handleChange(nodeId, netconfNode.isPresent() ? netconfNode.get() : null);
        } catch (ClassNotFoundException | NoSuchFieldException | SecurityException
                | IllegalArgumentException | IllegalAccessException | InterruptedException
                | ExecutionException | IOException e) {
            LOG.warn("problem reading netconfnode for {}: ", nodeId, e);
        }

    }

    private void handleChange(String nodeId, NetconfNode netconfNode) {

        if(netconfNode!=null) {
            @Nullable
            ConnectionStatus csts = netconfNode.getConnectionStatus();
            LOG.debug("handle change for connection status {}",csts);
            if (csts != null) {
                if (csts == ConnectionStatus.Connected) {
                    this.pushConnect(nodeId, netconfNode);
                } else if (csts == ConnectionStatus.Connecting) {
                    this.pushConnecting(nodeId, netconfNode);
                } else if (csts == ConnectionStatus.UnableToConnect) {
                    this.pushUnableToConnect(nodeId, netconfNode);
                }

            } else {
                LOG.warn("unable to handle node {} without connection status", nodeId);
            }
        }
        else {
            this.pushDisconnect(nodeId);
        }
    }

    private void pushConnect(String nodeId, NetconfNode netconfNode) {
        for (DeviceConnectionChangedHandler listener : this.listeners) {
            LOG.debug("push connected to {}",listener.getClass());
            listener.onRemoteDeviceConnected(nodeId, netconfNode);
        }
    }

    private void pushConnecting(String nodeId, NetconfNode netconfNode) {
        for (DeviceConnectionChangedHandler listener : this.listeners) {
            LOG.debug("push connecting to {}",listener.getClass());
            listener.onRemoteDeviceConnecting(nodeId);
        }
    }

    private void pushUnableToConnect(String nodeId, NetconfNode netconfNode) {
        for (DeviceConnectionChangedHandler listener : this.listeners) {
            LOG.debug("push unable to connect to {}",listener.getClass());
            listener.onRemoteDeviceUnableToConnect(nodeId);
        }
    }

    private void pushDisconnect(String nodeId) {
        for (DeviceConnectionChangedHandler listener : this.listeners) {
            LOG.debug("push disconnect to {}",listener.getClass());
            listener.onRemoteDeviceDisConnected(nodeId);
        }
    }

    public void register(DeviceConnectionChangedHandler listener) {
        this.listeners.add(listener);
    }

    public void unregister(DeviceConnectionChangedHandler listener) {
        this.listeners.remove(listener);
    }

}
