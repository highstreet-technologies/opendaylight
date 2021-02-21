/*
 * Copyright (C) 2019 highstreet technologies GmbH Intellectual Property.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.onap.ccsdk.features.sdnr.wt.odlclient.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.custommonkey.xmlunit.XMLUnit;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Ignore;
import org.junit.Test;
import org.onap.ccsdk.features.sdnr.wt.odlclient.data.OdlJsonSerializer;
import org.onap.ccsdk.features.sdnr.wt.odlclient.data.OdlObjectMapper;
import org.onap.ccsdk.features.sdnr.wt.odlclient.data.OdlObjectMapperXml;
import org.onap.ccsdk.features.sdnr.wt.odlclient.data.OdlRpcObjectMapperXml;
import org.onap.ccsdk.features.sdnr.wt.odlclient.data.OdlXmlSerializer;
import org.opendaylight.yang.gen.v1.http.org.opendaylight.transportpce.portmapping.rev200429.network.nodes.Mapping;
import org.opendaylight.yang.gen.v1.http.org.opendaylight.transportpce.portmapping.rev200429.network.nodes.MappingBuilder;
import org.opendaylight.yang.gen.v1.http.org.openroadm.common.types.rev181019.Direction;
import org.opendaylight.yang.gen.v1.http.org.openroadm.common.types.rev181019.EquipmentTypeEnum;
import org.opendaylight.yang.gen.v1.http.org.openroadm.common.types.rev181019.FrequencyTHz;
import org.opendaylight.yang.gen.v1.http.org.openroadm.common.types.rev181019.OpticalControlMode;
import org.opendaylight.yang.gen.v1.http.org.openroadm.common.types.rev181019.PortQual;
import org.opendaylight.yang.gen.v1.http.org.openroadm.common.types.rev181019.PowerDBm;
import org.opendaylight.yang.gen.v1.http.org.openroadm.common.types.rev181019.R100G;
import org.opendaylight.yang.gen.v1.http.org.openroadm.common.types.rev181019.State;
import org.opendaylight.yang.gen.v1.http.org.openroadm.device.rev181019.LedControlInputBuilder;
import org.opendaylight.yang.gen.v1.http.org.openroadm.device.rev181019.circuit.pack.CircuitPackCategoryBuilder;
import org.opendaylight.yang.gen.v1.http.org.openroadm.device.rev181019.circuit.pack.ParentCircuitPackBuilder;
import org.opendaylight.yang.gen.v1.http.org.openroadm.device.rev181019.circuit.pack.Ports;
import org.opendaylight.yang.gen.v1.http.org.openroadm.device.rev181019.circuit.pack.PortsBuilder;
import org.opendaylight.yang.gen.v1.http.org.openroadm.device.rev181019.circuit.pack.ports.OtdrPort;
import org.opendaylight.yang.gen.v1.http.org.openroadm.device.rev181019.circuit.pack.ports.TransponderPort;
import org.opendaylight.yang.gen.v1.http.org.openroadm.device.rev181019.circuit.pack.ports.TransponderPortBuilder;
import org.opendaylight.yang.gen.v1.http.org.openroadm.device.rev181019.circuit.packs.CircuitPacks;
import org.opendaylight.yang.gen.v1.http.org.openroadm.device.rev181019.circuit.packs.CircuitPacksBuilder;
import org.opendaylight.yang.gen.v1.http.org.openroadm.device.rev181019.connection.DestinationBuilder;
import org.opendaylight.yang.gen.v1.http.org.openroadm.device.rev181019.connection.SourceBuilder;
import org.opendaylight.yang.gen.v1.http.org.openroadm.device.rev181019.interfaces.grp.Interface;
import org.opendaylight.yang.gen.v1.http.org.openroadm.device.rev181019.interfaces.grp.InterfaceBuilder;
import org.opendaylight.yang.gen.v1.http.org.openroadm.device.rev181019.interfaces.grp.InterfaceKey;
import org.opendaylight.yang.gen.v1.http.org.openroadm.device.rev181019.led.control.input.equipment.entity.ShelfBuilder;
import org.opendaylight.yang.gen.v1.http.org.openroadm.device.rev181019.org.openroadm.device.container.OrgOpenroadmDevice;
import org.opendaylight.yang.gen.v1.http.org.openroadm.device.rev181019.org.openroadm.device.container.OrgOpenroadmDeviceBuilder;
import org.opendaylight.yang.gen.v1.http.org.openroadm.device.rev181019.org.openroadm.device.container.org.openroadm.device.Info;
import org.opendaylight.yang.gen.v1.http.org.openroadm.device.rev181019.org.openroadm.device.container.org.openroadm.device.RoadmConnections;
import org.opendaylight.yang.gen.v1.http.org.openroadm.device.rev181019.org.openroadm.device.container.org.openroadm.device.RoadmConnectionsBuilder;
import org.opendaylight.yang.gen.v1.http.org.openroadm.device.rev181019.org.openroadm.device.container.org.openroadm.device.UsersBuilder;
import org.opendaylight.yang.gen.v1.http.org.openroadm.equipment.states.types.rev171215.AdminStates;
import org.opendaylight.yang.gen.v1.http.org.openroadm.equipment.states.types.rev171215.States;
import org.opendaylight.yang.gen.v1.http.org.openroadm.interfaces.rev170626.InterfaceType;
import org.opendaylight.yang.gen.v1.http.org.openroadm.interfaces.rev170626.NetworkMediaChannelConnectionTerminationPoint;
import org.opendaylight.yang.gen.v1.http.org.openroadm.interfaces.rev170626.OpenROADMOpticalMultiplex;
import org.opendaylight.yang.gen.v1.http.org.openroadm.interfaces.rev170626.OpticalChannel;
import org.opendaylight.yang.gen.v1.http.org.openroadm.lldp.rev181019.Protocols1;
import org.opendaylight.yang.gen.v1.http.org.openroadm.lldp.rev181019.lldp.container.lldp.PortConfig;
import org.opendaylight.yang.gen.v1.http.org.openroadm.optical.channel.interfaces.rev181019.Interface1Builder;
import org.opendaylight.yang.gen.v1.http.org.openroadm.optical.channel.interfaces.rev181019.och.container.OchBuilder;
import org.opendaylight.yang.gen.v1.http.org.openroadm.optical.transport.interfaces.rev181019.Interface1;
import org.opendaylight.yang.gen.v1.http.org.openroadm.otn.odu.interfaces.rev181019.odu.container.OduBuilder;
import org.opendaylight.yang.gen.v1.http.org.openroadm.port.types.rev181019.IfOCH;
import org.opendaylight.yang.gen.v1.http.org.openroadm.port.types.rev181019.PortWavelengthTypes;
import org.opendaylight.yang.gen.v1.http.org.openroadm.user.mgmt.rev171215.PasswordType;
import org.opendaylight.yang.gen.v1.http.org.openroadm.user.mgmt.rev171215.UsernameType;
import org.opendaylight.yang.gen.v1.http.org.openroadm.user.mgmt.rev171215.user.profile.User.Group;
import org.opendaylight.yang.gen.v1.http.org.openroadm.user.mgmt.rev171215.user.profile.UserBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNode;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.netconf.node.connection.status.available.capabilities.AvailableCapability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class TestMapper {

    private static final Logger LOG = LoggerFactory.getLogger(TestMapper.class);

    private static final String INFOSTRING = " {\n" + "    \"org-openroadm-device:info\": {\n"
            + "        \"current-datetime\": \"2017-10-22T15:23:43Z\",\n" + "        \"vendor\": \"vendorA\",\n"
            + "        \"model\": \"model2\",\n" + "        \"softwareVersion\": \"swversion1234\",\n"
            + "        \"max-degrees\": 2,\n" + "        \"current-ipAddress\": \"127.0.0.11\",\n"
            + "        \"node-id\": \"ROADM-B1\",\n" + "        \"current-defaultGateway\": \"127.0.0.20\",\n"
            + "        \"clli\": \"NodeB\",\n" + "        \"prefix-length\": 28,\n"
            + "        \"current-prefix-length\": 28,\n" + "        \"macAddress\": \"00:01:02:03:04:05\",\n"
            + "        \"source\": \"static\",\n" + "        \"openroadm-version\": \"2.2.1\",\n"
            + "        \"geoLocation\": {\n" + "            \"latitude\": 1.0000,\n"
            + "            \"longitude\": 2.0000\n" + "        },\n"
            + "        \"max-num-bin-24hour-historical-pm\": 7,\n" + "        \"serial-id\": \"0002\",\n"
            + "        \"max-srgs\": 3,\n" + "        \"max-num-bin-15min-historical-pm\": 32,\n"
            + "        \"template\": \"template_1\",\n" + "        \"node-type\": \"rdm\",\n"
            + "        \"node-number\": 3,\n" + "        \"ipAddress\": \"127.0.0.11\",\n"
            + "        \"defaultGateway\": \"127.0.0.20\"\n" + "    }\n" + "}";
    private static final String INFOSTRING_XML = "<info xmlns=\"http://org/openroadm/device\">\n"
            + "    <current-datetime>2017-10-22T15:23:43Z</current-datetime>\n" + "    <vendor>vendorA</vendor>\n"
            + "    <model>model2</model>\n" + "    <softwareVersion>swversion1234</softwareVersion>\n"
            + "    <max-degrees>2</max-degrees>\n" + "    <current-ipAddress>127.0.0.11</current-ipAddress>\n"
            + "    <node-id>ROADM-B1</node-id>\n" + "    <current-defaultGateway>127.0.0.20</current-defaultGateway>\n"
            + "    <clli>NodeB</clli>\n" + "    <prefix-length>28</prefix-length>\n"
            + "    <current-prefix-length>28</current-prefix-length>\n"
            + "    <macAddress>00:01:02:03:04:05</macAddress>\n" + "    <source>static</source>\n"
            + "    <openroadm-version>2.2.1</openroadm-version>\n" + "    <geoLocation>\n"
            + "        <latitude>1.0000</latitude>\n" + "        <longitude>2.0000</longitude>\n"
            + "    </geoLocation>\n" + "    <max-num-bin-24hour-historical-pm>7</max-num-bin-24hour-historical-pm>\n"
            + "    <serial-id>0002</serial-id>\n" + "    <max-srgs>3</max-srgs>\n"
            + "    <max-num-bin-15min-historical-pm>32</max-num-bin-15min-historical-pm>\n"
            + "    <template>template_1</template>\n" + "    <node-type>rdm</node-type>\n"
            + "    <node-number>3</node-number>\n" + "    <ipAddress>127.0.0.11</ipAddress>\n"
            + "    <defaultGateway>127.0.0.20</defaultGateway>\n" + "</info>";
    private static final String NODEINFO = "{\"network-topology:node\":[{\"node-id\":\"roadmaa\","
            + "\"netconf-node-topology:unavailable-capabilities\":{\"unavailable-capability\":"
            + "[{\"capability\":\"(http://openconfig.net/yang/telemetry?revision=2017-08-24)openconfig-telemetry\","
            + "\"failure-reason\":\"unable-to-resolve\"},{\"capability\":\"(http://openconfig.net/yang/telemetry-types?"
            + "revision=2017-08-24)openconfig-telemetry-types\",\"failure-reason\":\"missing-source\"},"
            + "{\"capability\":\"(http://org/openroadm/telemetry-types?revision=2019-11-29)org-openroadm-telemetry-types\","
            + "\"failure-reason\":\"unable-to-resolve\"}]},\"netconf-node-topology:reconnect-on-changed-schema\":false,"
            + "\"netconf-node-topology:password\":\"undefind\",\"netconf-node-topology:username\":\"undefind\","
            + "\"netconf-node-topology:max-connection-attempts\":100,\"netconf-node-topology:tcp-only\":false,"
            + "\"netconf-node-topology:keepalive-delay\":120,\"netconf-node-topology:available-capabilities\":"
            + "{\"available-capability\":[{\"capability-origin\":\"device-advertised\",\"capability\":"
            + "\"urn:ietf:params:netconf:capability:writable-running:1.0\"},{\"capability-origin\":"
            + "\"device-advertised\","
            + "\"capability\":\"urn:ietf:params:netconf:capability:notification:1.0\"},{\"capability-origin\":"
            + "\"device-advertised\",\"capability\":\"urn:ietf:params:netconf:base:1.0\"},{\"capability-origin\":"
            + "\"device-advertised\",\"capability\":\"(http://org/openroadm/common-state-types?revision=2019-11-29)"
            + "org-openroadm-common-state-types\"},{\"capability-origin\":\"device-advertised\",\"capability\":"
            + "\"(http://org/openroadm/common-attributes?revision=2019-11-29)org-openroadm-common-attributes\"},"
            + "{\"capability-origin\":\"device-advertised\",\"capability\":\"(http://org/openroadm/"
            + "media-channel-interfaces?"
            + "revision=2019-11-29)org-openroadm-media-channel-interfaces\"},{\"capability-origin\":"
            + "\"device-advertised\","
            + "\"capability\":\"(http://org/openroadm/common-equipment-types?revision=2019-11-29)"
            + "org-openroadm-common-"
            + "equipment-types\"},{\"capability-origin\":\"device-advertised\",\"capability\":"
            + "\"(http://org/openroadm/de/swdl?revision=2019-11-29)org-openroadm-swdl\"},{\"capability-origin\":"
            + "\"device-advertised\",\"capability\":\"(http://org/openroadm/key-chain?revision=2019-11-29)"
            + "org-openroadm-key-chain\"},{\"capability-origin\":\"device-advertised\",\"capability\":\"(urn:ietf:"
            + "params:xml:ns:yang:ietf-netconf-with-defaults?revision=2011-06-01)ietf-netconf-with-defaults\"},"
            + "{\"capability-origin\":\"device-advertised\",\"capability\":\"(http://org/openroadm/network-resource?"
            + "revision=2019-11-29)org-openroadm-network-resource\"},{\"capability-origin\":\"device-advertised\","
            + "\"capability\":\"(urn:ietf:params:xml:ns:yang:ietf-netconf-notifications?revision=2012-02-06)"
            + "ietf-netconf-notifications\"},{\"capability\":\"(http://org/openroadm/otn-odu-interfaces?"
            + "revision=2019-11-29)org-openroadm-otn-odu-interfaces\"},{\"capability-origin\":\"device-advertised\","
            + "\"capability\":\"(http://org/openroadm/flexo-interfaces?revision=2019-11-29)org-"
            + "openroadm-flexo-interfaces\"},"
            + "{\"capability-origin\":\"device-advertised\",\"capability\":\"(http://org/openroadm/otn-common-"
            + "types?revision=2019-11-29)org-openroadm-otn-common-types\"},{\"capability-origin\":"
            + "\"device-advertised\"," + "\"capability\":\"(http://org/openroadm/service-format?revision=2019-11-29)"
            + "org-openroadm-service-format\"},"
            + "{\"capability-origin\":\"device-advertised\",\"capability\":\"(http://org/openroadm/resource?"
            + "revision=2019-11-29)"
            + "org-openroadm-resource\"},{\"capability-origin\":\"device-advertised\",\"capability\":"
            + "\"(urn:ietf:params:xml:ns:yang:ietf-yang-types?revision=2013-07-15)ietf-yang-types\"},"
            + "{\"capability-origin\":\"device-advertised\",\"capability\":\"(http://openconfig.net/yang/openconfig-ext?"
            + "revision=2017-04-11)openconfig-extensions\"},{\"capability-origin\":\"device-advertised\","
            + "\"capability\":\"(http://org/openroadm/device-types?revision=2019-11-29)org-openroadm-device-types\"},"
            + "{\"capability-origin\":\"device-advertised\",\"capability\":\"(http://org/openroadm/equipment/states/types?"
            + "revision=2019-11-29)org-openroadm-equipment-states-types\"},{\"capability-origin\":"
            + "\"device-advertised\","
            + "\"capability\":\"(http://org/openroadm/network-media-channel-interfaces?revision=2019-11-29)"
            + "org-openroadm-network-"
            + "media-channel-interfaces\"},{\"capability-origin\":\"device-advertised\",\"capability\":"
            + "\"(http://org/openroadm/"
            + "prot/otn-linear-aps?revision=2019-11-29)org-openroadm-prot-otn-linear-aps\"},{\"capability-origin\":"
            + "\"device-advertised\",\"capability\":\"(http://org/openroadm/common-node-types?revision=2019-11-29)"
            + "org-openroadm-common-node-types\"},{\"capability-origin\":\"device-advertised\",\"capability\":"
            + "\"(urn:ietf:params:xml:ns:netconf:notification:1.0?revision=2008-07-14)notifications\"},"
            + "{\"capability-origin\":"
            + "\"device-advertised\",\"capability\":\"(http://org/openroadm/optical-channel-interfaces?revision=2019-11-29)"
            + "org-openroadm-optical-channel-interfaces\"},{\"capability-origin\":\"device-advertised\",\"capability\":"
            + "\"(http://org/openroadm/ip?revision=2019-11-29)org-openroadm-ip\"},{\"capability-origin\":"
            + "\"device-advertised\","
            + "\"capability\":\"(urn:ietf:params:xml:ns:yang:ietf-netconf-acm?revision=2018-02-14)ietf-netconf-acm\"},"
            + "{\"capability-origin\":\"device-advertised\",\"capability\":\"(http://org/openroadm/fwdl?revision=2019-11-29)"
            + "org-openroadm-fwdl\"},{\"capability-origin\":\"device-advertised\",\"capability\":\"(http://"
            + "org/openroadm/physical/types?revision=2019-11-29)org-openroadm-physical-types\"},{\"capability-origin\":"
            + "\"device-advertised\",\"capability\":\"(http://org/openroadm/manifest-file?revision=2019-11-29)org-"
            + "openroadm-manifest-file\"},{\"capability-origin\":\"device-advertised\",\"capability\":"
            + "\"(http://openconfig.net/yang/types/inet?revision=2017-08-24)openconfig-inet-types\"},"
            + "{\"capability-origin\":\"device-advertised\",\"capability\":\"(urn:ietf:params:"
            + "xml:ns:yang:iana-crypt-hash?"
            + "revision=2014-08-06)iana-crypt-hash\"},{\"capability-origin\":\"device-advertised\",\"capability\":"
            + "\"(http://org/openroadm/dhcp?revision=2019-11-29)org-openroadm-dhcp\"},{\"capability-origin\":"
            + "\"device-advertised\",\"capability\":\"(http://org/openroadm/database?revision=2019-11-29)"
            + "org-openroadm-database\"},{\"capability-origin\":\"device-advertised\",\"capability\":"
            + "\"(http://org/openroadm/optical-operational-interfaces?revision=2019-11-29)org-openroadm-optical-"
            + "operational-interfaces\"},{\"capability-origin\":\"device-advertised\",\"capability\":"
            + "\"(http://org/openroadm/de/operations?revision=2019-11-29)org-openroadm-de-operations\"},"
            + "{\"capability\":\"(http://org/openroadm/ethernet-interfaces?revision=2019-11-29)"
            + "org-openroadm-ethernet-interfaces\"},"
            + "{\"capability-origin\":\"device-advertised\",\"capability\":\"(http://org/openroadm/syslog?revision=2019-11-29)"
            + "org-openroadm-syslog\"},{\"capability-origin\":\"device-advertised\",\"capability\":"
            + "\"(http://org/openroadm/optical-"
            + "transport-interfaces?revision=2019-11-29)org-openroadm-optical-transport-interfaces\"},"
            + "{\"capability-origin\":"
            + "\"device-advertised\",\"capability\":\"(http://org/openroadm/user-mgmt?revision=2019-11-29)"
            + "org-openroadm-user-mgmt\"},"
            + "{\"capability\":\"(http://org/openroadm/gnmi?revision=2019-11-29)org-openroadm-gnmi\"},{\"capability-origin\":"
            + "\"device-advertised\",\"capability\":\"(http://org/openroadm/layerRate?revision=2019-11-29)org-openroadm-layerRate\"}"
            + ",{\"capability-origin\":\"device-advertised\",\"capability\":\"(http://org/openroadm/probableCause?"
            + "revision=2019-11-29)org-openroadm-probable-cause\"},{\"capability-origin\":\"device-advertised\","
            + "\"capability\":\"(http://org/openroadm/common-link-types?revision=2019-11-29)"
            + "org-openroadm-common-link-types\"},"
            + "{\"capability-origin\":\"device-advertised\",\"capability\":\"(urn:ietf:params:xml:ns:"
            + "netmod:notification?"
            + "revision=2008-07-14)nc-notifications\"},{\"capability-origin\":\"device-advertised\",\"capability\":"
            + "\"(http://org/openroadm/optical-channel-tributary-signal-interfaces?revision=2019-11-29)org-openroadm-optical-"
            + "tributary-signal-interfaces\"},{\"capability-origin\":\"device-advertised\",\"capability\":"
            + "\"(http://org/openroadm/otn-common?revision=2019-11-29)org-openroadm-otn-common\"},"
            + "{\"capability-origin\":\"device-advertised\",\"capability\":\"(urn:ietf:params:xml:ns:netconf:base:1.0?"
            + "revision=2011-06-01)ietf-netconf\"},{\"capability-origin\":\"device-advertised\",\"capability\":"
            + "\"(http://org/openroadm/port/types?revision=2019-11-29)org-openroadm-port-types\"},{\"capability-origin\":"
            + "\"device-advertised\",\"capability\":\"(http://org/openroadm/file-transfer?revision=2019-11-29)"
            + "org-openroadm-file-transfer\"},{\"capability\":\"(http://org/openroadm/maintenance-testsignal?"
            + "revision=2019-11-29)org-openroadm-maintenance-testsignal\"},{\"capability-origin\":"
            + "\"device-advertised\","
            + "\"capability\":\"(http://org/openroadm/alarm?revision=2019-11-29)org-openroadm-alarm\"},"
            + "{\"capability-origin\":\"device-advertised\",\"capability\":\"(urn:ietf:params:xml:ns:"
            + "yang:ietf-inet-types?"
            + "revision=2013-07-15)ietf-inet-types\"},{\"capability-origin\":\"device-advertised\",\"capability\":"
            + "\"(http://org/openroadm/otsigroup-capability?revision=2019-11-29)org-openroadm-otsigroup-capability\"},"
            + "{\"capability-origin\":\"device-advertised\",\"capability\":\"(http://org/openroadm/ppp-interfaces?revision="
            + "2019-11-29)org-openroadm-ppp-interfaces\"},{\"capability-origin\":\"device-advertised\",\"capability\":"
            + "\"(http://org/openroadm/common-amplifier-types?revision=2019-11-29)org-openroadm-common-amplifier-types\"},"
            + "{\"capability-origin\":\"device-advertised\",\"capability\":\"(http://org/openroadm/resource/types?revision="
            + "2019-11-29)org-openroadm-resource-types\"},{\"capability-origin\":\"device-advertised\",\"capability\":"
            + "\"(http://org/openroadm/device?revision=2019-11-29)org-openroadm-device\"},{\"capability-origin\":"
            + "\"device-advertised\",\"capability\":\"(http://org/openroadm/port-capability?revision=2019-11-29)"
            + "org-openroadm-port-capability\"},{\"capability-origin\":\"device-advertised\",\"capability\":"
            + "\"(http://org/openroadm/maintenance-loopback?revision=2019-11-29)org-openroadm-maintenance-loopback\"},"
            + "{\"capability-origin\":\"device-advertised\",\"capability\":\"(http://org/openroadm/pm-types?"
            + "revision=2019-11-29)org-openroadm-pm-types\"},{\"capability\":\"(http://org/openroadm/security?"
            + "revision=2019-11-29)org-openroadm-security\"},{\"capability-origin\":\"device-advertised\","
            + "\"capability\":"
            + "\"(urn:ietf:params:xml:ns:yang:ietf-system?revision=2014-08-06)ietf-system\"},{\"capability-origin\":"
            + "\"device-advertised\",\"capability\":\"(http://org/openroadm/wavelength-map?revision=2019-11-29)"
            + "org-openroadm-wavelength-map\"},{\"capability-origin\":\"device-advertised\",\"capability\":"
            + "\"(http://org/openroadm/otsi-group-interfaces?revision=2019-11-29)org-openroadm-otsi-group-interfaces\"},"
            + "{\"capability-origin\":\"device-advertised\",\"capability\":\"(http://org/openroadm/tca?revision=2019-11-29)"
            + "org-openroadm-tca\"},{\"capability-origin\":\"device-advertised\",\"capability\":"
            + "\"(http://org/openroadm/common-optical-channel-types?revision=2019-11-29)org-openroadm-common-"
            + "optical-channel-types\"},"
            + "{\"capability-origin\":\"device-advertised\",\"capability\":\"(http://org/openroadm/flexogroup-interfaces?"
            + "revision=2019-11-29)org-openroadm-flexogroup-interfaces\"},{\"capability-origin\":\"device-advertised\","
            + "\"capability\":\"(http://org/openroadm/interfaces?revision=2019-11-29)org-openroadm-interfaces\"},"
            + "{\"capability-origin\":\"device-advertised\",\"capability\":\"(http://org/openroadm/otn-otu-interfaces?"
            + "revision=2019-11-29)org-openroadm-otn-otu-interfaces\"},{\"capability-origin\":\"device-advertised\","
            + "\"capability\":\"(http://org/openroadm/common-types?revision=2019-11-29)org-openroadm-common-types\"},"
            + "{\"capability-origin\":\"device-advertised\",\"capability\":\"(http://org/openroadm/gcc-interfaces?"
            + "revision=2019-11-29)org-openroadm-gcc-interfaces\"},{\"capability-origin\":\"device-advertised\","
            + "\"capability\":\"(urn:ietf:params:xml:ns:yang:ietf-netconf-monitoring?revision=2010-10-04)ietf"
            + "-netconf-monitoring\"},"
            + "{\"capability-origin\":\"device-advertised\",\"capability\":\"(http://org/openroadm/switching-pool-types?"
            + "revision=2019-11-29)org-openroadm-switching-pool-types\"},{\"capability-origin\":\"device-advertised\","
            + "\"capability\":\"(http://org/openroadm/common-alarm-pm-types?revision=2019-11-29)org-openroadm-common"
            + "-alarm-pm-types\"},{\"capability-origin\":\"device-advertised\",\"capability\":\"(http://org/openroadm/"
            + "rstp?revision=2019-11-29)org-openroadm-rstp\"},{\"capability-origin\":\"device-advertised\","
            + "\"capability\":"
            + "\"(http://org/openroadm/pm?revision=2019-11-29)org-openroadm-pm\"}]},\"netconf-node-topology:sleep"
            + "-factor\":"
            + "1.5,\"netconf-node-topology:connection-timeout-millis\":20000,\"netconf-node-topology:port\":4000,"
            + "\"netconf-node-topology:host\":\"192.168.178.169\",\"netconf-node-topology:between-attempts-timeout"
            + "-millis\":" + "2000,\"netconf-node-topology:connection-status\":\"connected\"}]}";
    private static final String NODEINFO_XML = "<node xmlns=\"urn:TBD:params:xml:ns:yang:network-topology\">\n"
            + "    <node-id>roadmaa</node-id>\n"
            + "    <unavailable-capabilities xmlns=\"urn:opendaylight:netconf-node-topology\">\n"
            + "        <unavailable-capability>\n"
            + "            <capability>(http://openconfig.net/yang/telemetry?revision=2017-08-24)"
            + "openconfig-telemetry</capability>\n" + "            <failure-reason>unable-to-resolve</failure-reason>\n"
            + "        </unavailable-capability>\n" + "        <unavailable-capability>\n"
            + "            <capability>(http://openconfig.net/yang/telemetry-types?"
            + "revision=2017-08-24)openconfig-telemetry-types</capability>\n"
            + "            <failure-reason>missing-source</failure-reason>\n" + "        </unavailable-capability>\n"
            + "        <unavailable-capability>\n"
            + "            <capability>(http://org/openroadm/telemetry-types?revision=2019-11-29)"
            + "org-openroadm-telemetry-types</capability>\n"
            + "            <failure-reason>unable-to-resolve</failure-reason>\n" + "        </unavailable-capability>\n"
            + "    </unavailable-capabilities>\n"
            + "    <reconnect-on-changed-schema xmlns=\"urn:opendaylight:netconf-node-topology\">"
            + "false</reconnect-on-changed-schema>\n"
            + "    <password xmlns=\"urn:opendaylight:netconf-node-topology\">undefind</password>\n"
            + "    <username xmlns=\"urn:opendaylight:netconf-node-topology\">undefind</username>\n"
            + "    <max-connection-attempts xmlns=\"urn:opendaylight:netconf-node-topology\">"
            + "100</max-connection-attempts>\n"
            + "    <tcp-only xmlns=\"urn:opendaylight:netconf-node-topology\">false</tcp-only>\n"
            + "    <keepalive-delay xmlns=\"urn:opendaylight:netconf-node-topology\">120</keepalive-delay>\n"
            + "    <available-capabilities xmlns=\"urn:opendaylight:netconf-node-topology\">\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>urn:ietf:params:netconf:capability:writable-running:1.0</capability>\n"
            + "        </available-capability>\n" + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>urn:ietf:params:netconf:capability:notification:1.0</capability>\n"
            + "        </available-capability>\n" + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>urn:ietf:params:netconf:base:1.0</capability>\n"
            + "        </available-capability>\n" + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/common-state-types?"
            + "revision=2019-11-29)org-openroadm-common-state-types</capability>\n"
            + "        </available-capability>\n" + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/common-attributes?"
            + "revision=2019-11-29)org-openroadm-common-attributes</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/media-channel-interfaces?"
            + "revision=2019-11-29)org-openroadm-media-channel-interfaces</capability>\n"
            + "        </available-capability>\n" + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/common-equipment-types?"
            + "revision=2019-11-29)org-openroadm-common-equipment-types</capability>\n"
            + "        </available-capability>\n" + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/de/swdl?revision=2019-11-29)"
            + "org-openroadm-swdl</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/key-chain?revision=2019-11-29)"
            + "org-openroadm-key-chain</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(urn:ietf:params:xml:ns:yang:ietf-netconf-with-defaults?"
            + "revision=2011-06-01)ietf-netconf-with-defaults</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/network-resource?revision=2019-11-29)"
            + "org-openroadm-network-resource</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(urn:ietf:params:xml:ns:yang:ietf-netconf-notifications?"
            + "revision=2012-02-06)ietf-netconf-notifications</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability>(http://org/openroadm/otn-odu-interfaces?revision=2019-11-29)"
            + "org-openroadm-otn-odu-interfaces</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/flexo-interfaces?revision=2019-11-29)"
            + "org-openroadm-flexo-interfaces</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/otn-common-types?revision=2019-11-29)"
            + "org-openroadm-otn-common-types</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/service-format?revision=2019-11-29)"
            + "org-openroadm-service-format</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/resource?revision=2019-11-29)"
            + "org-openroadm-resource</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(urn:ietf:params:xml:ns:yang:ietf-yang-types?"
            + "revision=2013-07-15)ietf-yang-types</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://openconfig.net/yang/openconfig-ext?"
            + "revision=2017-04-11)openconfig-extensions</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/device-types?revision=2019-11-29)"
            + "org-openroadm-device-types</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/equipment/states/types?"
            + "revision=2019-11-29)org-openroadm-equipment-states-types</capability>\n"
            + "        </available-capability>\n" + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/network-media-channel-interfaces?"
            + "revision=2019-11-29)org-openroadm-network-media-channel-interfaces</capability>\n"
            + "        </available-capability>\n" + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/prot/otn-linear-aps?"
            + "revision=2019-11-29)org-openroadm-prot-otn-linear-aps</capability>\n"
            + "        </available-capability>\n" + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/common-node-types?"
            + "revision=2019-11-29)org-openroadm-common-node-types</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(urn:ietf:params:xml:ns:netconf:notification:1.0?"
            + "revision=2008-07-14)notifications</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/optical-channel-interfaces?"
            + "revision=2019-11-29)org-openroadm-optical-channel-interfaces</capability>\n"
            + "        </available-capability>\n" + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/ip?revision=2019-11-29)"
            + "org-openroadm-ip</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(urn:ietf:params:xml:ns:yang:ietf-netconf-acm?"
            + "revision=2018-02-14)ietf-netconf-acm</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/fwdl?revision=2019-11-29)"
            + "org-openroadm-fwdl</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/physical/types?revision=2019-11-29)"
            + "org-openroadm-physical-types</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/manifest-file?revision=2019-11-29)"
            + "org-openroadm-manifest-file</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://openconfig.net/yang/types/inet?"
            + "revision=2017-08-24)openconfig-inet-types</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(urn:ietf:params:xml:ns:yang:iana-crypt-hash?"
            + "revision=2014-08-06)iana-crypt-hash</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/dhcp?revision=2019-11-29)"
            + "org-openroadm-dhcp</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/database?revision=2019-11-29)"
            + "org-openroadm-database</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/optical-operational-interfaces?"
            + "revision=2019-11-29)org-openroadm-optical-operational-interfaces</capability>\n"
            + "        </available-capability>\n" + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/de/operations?"
            + "revision=2019-11-29)org-openroadm-de-operations</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n" + "            <capability>(http://org/openroadm/ethernet-interfaces?"
            + "revision=2019-11-29)org-openroadm-ethernet-interfaces</capability>\n"
            + "        </available-capability>\n" + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/syslog?revision=2019-11-29)"
            + "org-openroadm-syslog</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/optical-transport-interfaces?"
            + "revision=2019-11-29)org-openroadm-optical-transport-interfaces</capability>\n"
            + "        </available-capability>\n" + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/user-mgmt?revision=2019-11-29)"
            + "org-openroadm-user-mgmt</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability>(http://org/openroadm/gnmi?revision=2019-11-29)"
            + "org-openroadm-gnmi</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/layerRate?"
            + "revision=2019-11-29)org-openroadm-layerRate</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/probableCause?"
            + "revision=2019-11-29)org-openroadm-probable-cause</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/common-link-types?"
            + "revision=2019-11-29)org-openroadm-common-link-types</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(urn:ietf:params:xml:ns:netmod:notification?"
            + "revision=2008-07-14)nc-notifications</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/optical-channel-tributary-signal-interfaces?"
            + "revision=2019-11-29)org-openroadm-optical-tributary-signal-interfaces</capability>\n"
            + "        </available-capability>\n" + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/otn-common?revision=2019-11-29)"
            + "org-openroadm-otn-common</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(urn:ietf:params:xml:ns:netconf:base:1.0?"
            + "revision=2011-06-01)ietf-netconf</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/port/types?revision=2019-11-29)"
            + "org-openroadm-port-types</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/file-transfer?revision=2019-11-29)"
            + "org-openroadm-file-transfer</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability>(http://org/openroadm/maintenance-testsignal?"
            + "revision=2019-11-29)org-openroadm-maintenance-testsignal</capability>\n"
            + "        </available-capability>\n" + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/alarm?revision=2019-11-29)"
            + "org-openroadm-alarm</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(urn:ietf:params:xml:ns:yang:ietf-inet-types?"
            + "revision=2013-07-15)ietf-inet-types</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/otsigroup-capability?"
            + "revision=2019-11-29)org-openroadm-otsigroup-capability</capability>\n"
            + "        </available-capability>\n" + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/ppp-interfaces?revision=2019-11-29)"
            + "org-openroadm-ppp-interfaces</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/common-amplifier-types?"
            + "revision=2019-11-29)org-openroadm-common-amplifier-types</capability>\n"
            + "        </available-capability>\n" + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/resource/types?revision=2019-11-29)"
            + "org-openroadm-resource-types</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/device?revision=2019-11-29)"
            + "org-openroadm-device</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/port-capability?revision=2019-11-29)"
            + "org-openroadm-port-capability</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/maintenance-loopback?revision=2019-11-29)"
            + "org-openroadm-maintenance-loopback</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/pm-types?revision=2019-11-29)"
            + "org-openroadm-pm-types</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability>(http://org/openroadm/security?revision=2019-11-29)"
            + "org-openroadm-security</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(urn:ietf:params:xml:ns:yang:ietf-system?"
            + "revision=2014-08-06)ietf-system</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/wavelength-map?revision=2019-11-29)"
            + "org-openroadm-wavelength-map</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/otsi-group-interfaces?revision=2019-11-29)"
            + "org-openroadm-otsi-group-interfaces</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/tca?revision=2019-11-29)org-openroadm-tca</capability>\n"
            + "        </available-capability>\n" + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/common-optical-channel-types?revision=2019-11-29)"
            + "org-openroadm-common-optical-channel-types</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/flexogroup-interfaces?revision=2019-11-29)"
            + "org-openroadm-flexogroup-interfaces</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/interfaces?revision=2019-11-29)"
            + "org-openroadm-interfaces</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/otn-otu-interfaces?revision=2019-11-29)"
            + "org-openroadm-otn-otu-interfaces</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/common-types?revision=2019-11-29)"
            + "org-openroadm-common-types</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/gcc-interfaces?revision=2019-11-29)"
            + "org-openroadm-gcc-interfaces</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(urn:ietf:params:xml:ns:yang:ietf-netconf-monitoring?"
            + "revision=2010-10-04)ietf-netconf-monitoring</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/switching-pool-types?revision=2019-11-29)"
            + "org-openroadm-switching-pool-types</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/common-alarm-pm-types?revision=2019-11-29)"
            + "org-openroadm-common-alarm-pm-types</capability>\n" + "        </available-capability>\n"
            + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/rstp?revision=2019-11-29)org-openroadm-rstp</capability>\n"
            + "        </available-capability>\n" + "        <available-capability>\n"
            + "            <capability-origin>device-advertised</capability-origin>\n"
            + "            <capability>(http://org/openroadm/pm?revision=2019-11-29)org-openroadm-pm</capability>\n"
            + "        </available-capability>\n" + "    </available-capabilities>\n"
            + "    <sleep-factor xmlns=\"urn:opendaylight:netconf-node-topology\">1.5</sleep-factor>\n"
            + "    <connection-timeout-millis xmlns=\"urn:opendaylight:netconf-node-topology\">"
            + "20000</connection-timeout-millis>\n"
            + "    <port xmlns=\"urn:opendaylight:netconf-node-topology\">4000</port>\n"
            + "    <host xmlns=\"urn:opendaylight:netconf-node-topology\">192.168.178.169</host>\n"
            + "    <between-attempts-timeout-millis xmlns=\"urn:opendaylight:netconf-node-topology\">"
            + "2000</between-attempts-timeout-millis>\n"
            + "    <connection-status xmlns=\"urn:opendaylight:netconf-node-topology\">connected</connection-status>\n"
            + "</node>";

    private static final String NETCONFNODE_CONNECTING_XML =
            "<node xmlns=\"urn:TBD:params:xml:ns:yang:network-topology\">\n" + "  <node-id>onapextroadmb1</node-id>\n"
                    + "  <reconnect-on-changed-schema xmlns=\"urn:opendaylight:netconf-node-topology\">false</reconnect-on-changed-schema>\n"
                    + "  <sleep-factor xmlns=\"urn:opendaylight:netconf-node-topology\">1.5</sleep-factor>\n"
                    + "  <password xmlns=\"urn:opendaylight:netconf-node-topology\">asd</password>\n"
                    + "  <username xmlns=\"urn:opendaylight:netconf-node-topology\">asd</username>\n"
                    + "  <max-connection-attempts xmlns=\"urn:opendaylight:netconf-node-topology\">100</max-connection-attempts>\n"
                    + "  <connection-timeout-millis xmlns=\"urn:opendaylight:netconf-node-topology\">20000</connection-timeout-millis>\n"
                    + "  <tcp-only xmlns=\"urn:opendaylight:netconf-node-topology\">false</tcp-only>\n"
                    + "  <port xmlns=\"urn:opendaylight:netconf-node-topology\">4000</port>\n"
                    + "  <host xmlns=\"urn:opendaylight:netconf-node-topology\">172.29.0.7</host>\n"
                    + "  <between-attempts-timeout-millis xmlns=\"urn:opendaylight:netconf-node-topology\">2000</between-attempts-timeout-millis>\n"
                    + "  <keepalive-delay xmlns=\"urn:opendaylight:netconf-node-topology\">120</keepalive-delay>\n"
                    + "  <connection-status xmlns=\"urn:opendaylight:netconf-node-topology\">connecting</connection-status>\n"
                    + "</node>";

    // @Test
    public void testMapRoadmInfo()
            throws ClassNotFoundException, JsonParseException, JsonMappingException, IOException {
        OdlObjectMapper jsonMapper = new OdlObjectMapper();
        OdlObjectMapperXml xmlMapper = new OdlObjectMapperXml(true);
        LOG.info("outputjson={}", jsonMapper.readValue(INFOSTRING, Info.class, "org-openroadm-device:info"));
        LOG.info("outputxml={}", xmlMapper.readValue(INFOSTRING_XML, Info.class));
    }

    //    @Test
    public void testNodeInfo() throws JsonParseException, JsonMappingException, IOException {
        OdlObjectMapper jsonMapper = new OdlObjectMapper();
        OdlObjectMapperXml xmlMapper = new OdlObjectMapperXml(true);

        // LOG.info("outputjson={}", jsonMapper.readValue(NODEINFO, NetconfNode.class, "network-topology:node"));
        LOG.info("outputxml={}", xmlMapper.readValue(this.getTrimmedFileContent("/xml/roadm-device3.xml"), Info.class));

    }

    //@Test
    public void testRpcSerializer() {
        OdlXmlSerializer mapper = new OdlXmlSerializer();
        final LedControlInputBuilder builder = new LedControlInputBuilder();
        builder.setEnabled(true).setEquipmentEntity(new ShelfBuilder().setShelfName("1/0").build());
        String inputPayload = mapper.writeValueAsString(builder.build(), "input");
        LOG.info(inputPayload);
        try {
            assertTrue(XMLUnit.compareXML(this.getTrimmedFileContent("/xml/roadm-rpc-input1.xml"), inputPayload)
                    .similar());
        } catch (SAXException | IOException e) {
            fail(e.getMessage());
        }
    }

    //FIXME: problem with multiline resource loading
    //    @Test
    public void testLeafListSerializing() throws ParserConfigurationException, TransformerException {
        OrgOpenroadmDeviceBuilder builder = new OrgOpenroadmDeviceBuilder();
        builder.setUsers(new UsersBuilder().setUser(Arrays.asList(
                //new UserBuilder().setName(new UsernameType("openroadm")).setPassword(new PasswordType("openroadm")).setGroup(Group.Sudo).build(),
                new UserBuilder().setName(new UsernameType("openroadm2")).setPassword(new PasswordType("openroadm"))
                        .setGroup(Group.Sudo).build()))
                .build());

        OdlXmlSerializer mapper = new OdlXmlSerializer();
        OdlJsonSerializer mapper2 = new OdlJsonSerializer();

        String inputPayload = mapper.writeValueAsString(builder.build(), "org-openroadm-device");
        try {
            LOG.info(inputPayload);
            assertTrue(
                    XMLUnit.compareXML(this.getTrimmedFileContent("/xml/roadm-device2.xml"), inputPayload).similar());
        } catch (SAXException | IOException e) {

            fail(e.getMessage());
        }
        inputPayload = mapper2.writeValueAsString(builder.build(), "org-openroadm-device");
        LOG.info(inputPayload);
    }

    @Test
    public void testInterfaceConfigSerialization() throws JsonProcessingException {
        //<interface xmlns="http://org/openroadm/device">
        //  <administrative-state >inService</administrative-state>
        //  <circuit-id >   TBD    </circuit-id>
        //  <description >  TBD   </description>
        //  <name >XPDR1-NETWORK1-1</name>
        //  <operational-state >null</operational-state>
        //  <supporting-circuit-pack-name >1/0/1-PLUG-NET</supporting-circuit-pack-name>
        //  <supporting-interface >null</supporting-interface>
        //  <supporting-port >1</supporting-port>
        //  <type >interface org.opendaylight.yang.gen.v1.http.org.openroadm.interfaces.rev170626.OpticalChannel</type>
        //</interface>

        InterfaceBuilder builder = new InterfaceBuilder();
        builder.setAdministrativeState(AdminStates.InService);
        builder.setCircuitId("TBD");
        builder.setDescription("TBD");
        builder.setName("XPDR1-NETWORK1-1");
        builder.setOperationalState(null);
        builder.setSupportingCircuitPackName("1/0/1-PLUG-NET");
        builder.setSupportingInterface(null);
        builder.setSupportingPort(1);
        builder.setType(org.opendaylight.yang.gen.v1.http.org.openroadm.interfaces.rev170626.OpticalChannel.class);
        OdlRpcObjectMapperXml mapper = new OdlRpcObjectMapperXml();
        LOG.info("ifoutput = {}", mapper.writeValueAsString(builder.build()));
    }

    @Test
    public void testTopologyNodeDeser() throws IOException {

        String xml = this.getTrimmedFileContent("/xml/roadma-netconfnode.xml");
        OdlObjectMapperXml mapper = new OdlObjectMapperXml(true);
        NetconfNode nNode = mapper.readValue(xml, NetconfNode.class);
        LOG.debug("{}", nNode);
        assertNotNull(nNode.getAvailableCapabilities());
        @Nullable
        List<AvailableCapability> caps = nNode.getAvailableCapabilities().getAvailableCapability();
        assertTrue(caps.size() > 0);
        assertTrue(caps.stream().filter(new Predicate<AvailableCapability>() {
            @Override
            public boolean test(AvailableCapability arg0) {
                return arg0.getCapability().contains("org-openroadm-device");
            };
        }).count() > 0);
    }

    //@Test
    public void testProtocolAugment() throws IOException {
        String xml = this.getTrimmedFileContent("/xml/roadm-device-protocols.xml");
        OdlObjectMapperXml mapper = new OdlObjectMapperXml(true);
        Protocols1 data = mapper.readValue(xml, Protocols1.class);
        LOG.info("protocol={}", data);
        LOG.info("protocol lldp={}", data.getLldp());
        @Nullable
        List<PortConfig> cfgs = data.getLldp().getPortConfig();
        LOG.info("protocol lldp portconfig={}", cfgs);
        for (PortConfig portConfig : cfgs) {
            LOG.info("portconfig={} if={}", portConfig.getAdminStatus(), portConfig.getIfName());
        }
    }

    @Test
    public void testProtocolPortConfigDeser() throws JsonParseException, JsonMappingException, IOException {
        String xml = " <port-config>\n" + "          <ifName>1GE-interface-1</ifName>\n"
                + "          <adminStatus>txandrx</adminStatus>\n" + "        </port-config>";
        OdlObjectMapperXml mapper = new OdlObjectMapperXml(true);
        PortConfig portConfig = mapper.readValue(xml, PortConfig.class);
        LOG.info("portconfig={} if={}", portConfig.getAdminStatus(), portConfig.getIfName());
    }

    @Test
    public void testInterfaceDeser() throws IOException {
        String xml = this.getTrimmedFileContent("/xml/roadm-interfaces.xml");
        OdlObjectMapperXml mapper = new OdlObjectMapperXml(true);
        Interface intf = mapper.readValue(xml, Interface.class);
        LOG.info("interface={}", intf);
    }

    @Test
    public void testInterface2Deser() throws IOException {
        String xml = this.getTrimmedFileContent("/xml/roadm-interfaces2.xml");
        OdlObjectMapperXml mapper = new OdlObjectMapperXml(true);
        Interface intf = mapper.readValue(xml, Interface.class);
        LOG.info("interface2={}", intf);
        LOG.info("interface2aug={}", intf.augmentation(Interface1.class));
    }

    @Test
    public void testPortsDeser() throws IOException {
        String xml = this.getTrimmedFileContent("/xml/roadm-ports.xml");
        OdlObjectMapperXml mapper = new OdlObjectMapperXml(true);
        OtdrPort port = mapper.readValue(xml, OtdrPort.class);
        LOG.info("port={}", port);
    }

    @Test
    public void testCompleteDeser() throws IOException {
        String xml = this.getTrimmedFileContent("/xml/roadm-device-complete.xml");
        OdlObjectMapperXml mapper = new OdlObjectMapperXml(true);
        OrgOpenroadmDevice data = mapper.readValue(xml, OrgOpenroadmDevice.class);
        LOG.info("complete={}", data);
    }

    private String getTrimmedFileContent(String filename) throws IOException {
        ImmutableList<String> lines =
                Files.asCharSource(new File(TestMapper.class.getResource(filename).getFile()), StandardCharsets.UTF_8)
                        .readLines();
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line.trim());
        }
        LOG.info("input={}", sb.toString());
        return sb.toString();
    }

    private Reader getFileReader(String filename) throws FileNotFoundException {
        return new FileReader(new File(TestMapper.class.getResource(filename).getFile()));
    }

    //    @Test
    public void testNetconfNodeDeserializer() throws JsonParseException, JsonMappingException, IOException {
        OdlObjectMapperXml xmlMapper = new OdlObjectMapperXml(true);
        NetconfNode nNode = xmlMapper.readValue(NETCONFNODE_CONNECTING_XML, NetconfNode.class);
        LOG.info("res={}", nNode);
    }

    @Test
    public void testRoadmConnectionsSerializer() {
        final String connectionNumber = "DEG2-TTP-TXRX-SRG1-PP1-TXRX-1";
        final String srcTp = "DEG2-TTP-TXRX";
        final String waveNumber = "1";
        final String destTp = "SRG1-PP1-TXRX";
        RoadmConnectionsBuilder rdmConnBldr = new RoadmConnectionsBuilder().setConnectionName(connectionNumber)
                .setOpticalControlMode(OpticalControlMode.Off)
                .setSource(new SourceBuilder().setSrcIf(srcTp + "-nmc-" + waveNumber).build())
                .setDestination(new DestinationBuilder().setDstIf(destTp + "-nmc-" + waveNumber).build());
        OdlRpcObjectMapperXml mapper = new OdlRpcObjectMapperXml();
        String res = mapper.writeValueAsString(rdmConnBldr.build(), RoadmConnections.class);
        String fileContent = null;
        try {
            fileContent = this.getTrimmedFileContent("/xml/roadm-connections-put-request.xml");
        } catch (IOException e1) {
            fail(e1.getMessage());
        }
        LOG.info("src={}", fileContent);
        LOG.info("res={}", res);
        try {
            assertTrue(XMLUnit.compareXML(fileContent, res).similar());
        } catch (SAXException | IOException e) {
            LOG.error(e.getMessage());
            fail(e.getMessage());
        }

    }

    @Test
    public void testInterfaceSerializer() {
        InterfaceBuilder interfaceBuilder = new InterfaceBuilder().setDescription("  TBD   ").setCircuitId("   TBD    ")
                .setSupportingCircuitPackName("2/0").setSupportingPort("L1")
                .setAdministrativeState(AdminStates.InService)
                .setType(NetworkMediaChannelConnectionTerminationPoint.class).setName("DEG2-TTP-TXRX-nmc-1")
                .withKey(new InterfaceKey("DEG2-TTP-TXRX-nmc-1"));
        OdlRpcObjectMapperXml mapper = new OdlRpcObjectMapperXml();
        String res = mapper.writeValueAsString(interfaceBuilder.build(), Interface.class);
        String fileContent = null;
        try {
            fileContent = this.getTrimmedFileContent("/xml/roadm-interface-put-request.xml");
        } catch (IOException e1) {
            fail(e1.getMessage());
        }
        LOG.info("src={}", fileContent);
        LOG.info("res={}", res);
        try {
            assertTrue(XMLUnit.compareXML(fileContent, res).similar());
        } catch (SAXException | IOException e) {
            LOG.error(e.getMessage());
            fail(e.getMessage());
        }

    }
    @Test
    public void testInterfaceWithAugmentSerializer() {
        Mapping portMap = new MappingBuilder().setSupportingCircuitPackName("cpname")
                .setSupportingPort("C1")
                .build();


        // OCH interface specific data
        OchBuilder ocIfBuilder = new OchBuilder()
                .setFrequency(FrequencyTHz.getDefaultInstance("196.5"))
                .setRate(R100G.class)
                .setTransmitPower(new PowerDBm(new BigDecimal("-5")));

        String logicalConnPoint = "lcp";
        Long waveNumber = 1L;
        // Create generic interface
        InterfaceBuilder ochInterfaceBldr = createGenericInterfaceBuilder(portMap, OpticalChannel.class,
            createOpenRoadmOchInterfaceName(logicalConnPoint, waveNumber));
        // Create Interface1 type object required for adding as augmentation
        // TODO look at imports of different versions of class
        org.opendaylight.yang.gen.v1.http.org.openroadm.optical.channel.interfaces.rev181019.Interface1Builder
            ochIf1Builder = new org.opendaylight.yang.gen.v1.http.org.openroadm.optical.channel.interfaces.rev181019
            .Interface1Builder();
        ochInterfaceBldr.addAugmentation(
            org.opendaylight.yang.gen.v1.http.org.openroadm.optical.channel.interfaces.rev181019.Interface1.class,
            ochIf1Builder.setOch(ocIfBuilder.build()).build());
        OdlRpcObjectMapperXml mapper = new OdlRpcObjectMapperXml();
        Method[] ms = ochInterfaceBldr.getClass().getDeclaredMethods();
        for(Method m:ms) {
            if(m.getName().startsWith("augm")) {
                LOG.info("m={}",m.getName());
            }
        }
        String res = mapper.writeValueAsString(ochInterfaceBldr.build(), Interface.class);
        LOG.info("if with aug = {}",res);
    }
    public String createOpenRoadmOchInterfaceName(String logicalConnectionPoint, Long waveNumber) {
        return logicalConnectionPoint + "-" + waveNumber;
    }
    private InterfaceBuilder createGenericInterfaceBuilder(Mapping portMap, Class<? extends InterfaceType> type,
            String key) {
            InterfaceBuilder interfaceBuilder = new InterfaceBuilder()
                    .setDescription("  TBD   ")
                    .setCircuitId("   TBD    ")
                    .setSupportingCircuitPackName(portMap.getSupportingCircuitPackName())
                    .setSupportingPort(portMap.getSupportingPort())
                    .setAdministrativeState(AdminStates.InService)
                    .setType(type)
                    .setName(key)
                    .withKey(new InterfaceKey(key));
            return interfaceBuilder;
        }
    @Ignore
    @Test
    public void testPowerSerializer() {
        final String interfaceString = "<interface xmlns=\"http://org/openroadm/device\">"
                + "<name>SRG1-PP1-TXRX-nmc-1</name>"
                + "<supporting-circuit-pack-name>3/0</supporting-circuit-pack-name>" + "<circuit-id>TBD</circuit-id>"
                + "<administrative-state>inService</administrative-state>" + "<supporting-port>C1</supporting-port>"
                + "<type xmlns:x=\"http://org/openroadm/interfaces\">x:networkMediaChannelConnectionTerminationPoint</type>"
                + "<description>TBD</description>" + "</interface>";
        OdlObjectMapperXml mapper = new OdlObjectMapperXml(true);
        Interface interfaceObj = null;
        try {
            interfaceObj = mapper.readValue(interfaceString, Interface.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //    	InterfaceBuilder interfaceBuilder = new InterfaceBuilder()
        //                .setDescription("  TBD   ")
        //                .setCircuitId("   TBD    ")
        //                .setSupportingCircuitPackName("2/0")
        //                .setSupportingPort("L1")
        //                .setAdministrativeState(AdminStates.InService)
        //                .setType(NetworkMediaChannelConnectionTerminationPoint.class)
        //                .setName("DEG2-TTP-TXRX-nmc-1")
        //                .withKey(new InterfaceKey("DEG2-TTP-TXRX-nmc-1"));
        //

        InterfaceBuilder ochInterfaceBuilder = new InterfaceBuilder(interfaceObj);
        org.opendaylight.yang.gen.v1.http.org.openroadm.optical.channel.interfaces.rev181019.Interface1 if1 =
                ochInterfaceBuilder.augmentation(
                        org.opendaylight.yang.gen.v1.http.org.openroadm.optical.channel.interfaces.rev181019.Interface1.class);
        OchBuilder ochBuilder = new OchBuilder(if1.getOch());
        BigDecimal txPower = BigDecimal.valueOf(1);
        ochBuilder.setTransmitPower(new PowerDBm(txPower));
        ochInterfaceBuilder.addAugmentation(Interface1.class,
                new Interface1Builder().setOch(ochBuilder.build()).build());
    }

    @Test
    public void testAugmentDeserializerManual() {
        final String interfaceString = "<interface xmlns=\"http://org/openroadm/device\">\n"
                + "  <name>OTS-DEG2-TTP-TXRX</name>\n" + "  <operational-state>inService</operational-state>\n"
                + "  <supporting-circuit-pack-name>2/0</supporting-circuit-pack-name>\n"
                + "  <administrative-state>inService</administrative-state>\n"
                + "  <ots xmlns=\"http://org/openroadm/optical-transport-interfaces\">\n"
                + "    <ingress-span-loss-aging-margin>0.0</ingress-span-loss-aging-margin>\n"
                + "    <span-loss-receive>15.0</span-loss-receive>\n"
                + "    <span-loss-transmit>6.0</span-loss-transmit>\n" + "    <fiber-type>smf</fiber-type>\n"
                + "  </ots>\n" + "  <supporting-port>L1</supporting-port>\n"
                + "  <type xmlns:x=\"http://org/openroadm/interfaces\">x:opticalTransport</type>\n" + "</interface>";
        OdlObjectMapperXml mapper = new OdlObjectMapperXml(true);
        Interface interfaceObj = null;
        try {
            interfaceObj = mapper.readValue(interfaceString, Interface.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertNotNull(interfaceObj);
        try {
            InterfaceBuilder builder = new InterfaceBuilder(interfaceObj);
            builder.addAugmentation(Interface1.class, mapper.readValue(interfaceString, Interface1.class));
            interfaceObj = builder.build();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        LOG.info("if = {}", interfaceObj);
        LOG.info("if1 = {}", interfaceObj.augmentation(Interface1.class));

    }

    @Test
    public void testAugmentDeserializerAutomatic() {
        final String interfaceString = "<interface xmlns=\"http://org/openroadm/device\">\n"
                + "  <name>OTS-DEG2-TTP-TXRX</name>\n" + "  <operational-state>inService</operational-state>\n"
                + "  <supporting-circuit-pack-name>2/0</supporting-circuit-pack-name>\n"
                + "  <administrative-state>inService</administrative-state>\n"
                + "  <ots xmlns=\"http://org/openroadm/optical-transport-interfaces\">\n"
                + "    <ingress-span-loss-aging-margin>0.0</ingress-span-loss-aging-margin>\n"
                + "    <span-loss-receive>15.0</span-loss-receive>\n"
                + "    <span-loss-transmit>6.0</span-loss-transmit>\n" + "    <fiber-type>smf</fiber-type>\n"
                + "  </ots>\n" + "  <supporting-port>L1</supporting-port>\n"
                + "  <type xmlns:x=\"http://org/openroadm/interfaces\">x:opticalTransport</type>\n" + "</interface>";
        OdlObjectMapperXml mapper = new OdlObjectMapperXml();
        Interface interfaceObj = null;
        try {
            interfaceObj = mapper.readValue(interfaceString, Interface.class, Interface1.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertNotNull(interfaceObj);
        LOG.info("if = {}", interfaceObj);
        LOG.info("if1 = {}", interfaceObj.augmentation(Interface1.class));

    }

    @Test
    public void testInterfaceOduDeserializer() {
        OdlObjectMapperXml mapper = new OdlObjectMapperXml(true);
        Interface interfaceObj = null;
        String fileContent = null;
        try {
            fileContent = this.getTrimmedFileContent("/xml/roadm-interface-odu.xml");
        } catch (IOException e1) {
            fail(e1.getMessage());
        }
        try {
            interfaceObj = mapper.readValue(fileContent, Interface.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertNotNull(interfaceObj);
        LOG.info("if = {}", interfaceObj);
        org.opendaylight.yang.gen.v1.http.org.openroadm.otn.odu.interfaces.rev181019.Interface1Builder oduBuilder =
                new org.opendaylight.yang.gen.v1.http.org.openroadm.otn.odu.interfaces.rev181019.Interface1Builder(
                        interfaceObj.augmentation(org.opendaylight.yang.gen.v1.http.org.openroadm.otn.odu.interfaces.rev181019.Interface1.class));
        OduBuilder odu = new OduBuilder(oduBuilder.getOdu());
        LOG.info("odu = {}", odu.build());
    }

    @Test
    public void testCuircutPackSerializer() {
        CircuitPacksBuilder cpBldr = new CircuitPacksBuilder();
        cpBldr.setCircuitPackCategory(new CircuitPackCategoryBuilder().setType(EquipmentTypeEnum.CircuitPack).build())
        .setCircuitPackMode("NORMAL").setCircuitPackName("1/0/1-PLUG-NET")
        .setCircuitPackProductCode("Line_NW_P").setCircuitPackType("line_pluggable")
        .setEquipmentState(States.NotReservedInuse).setHardwareVersion("0.1")
        .setModel("CFP2").setOperationalState(State.InService)
        .setParentCircuitPack(new ParentCircuitPackBuilder().setCircuitPackName("1/0").setCpSlotName("1").build())
        .setPorts(Arrays.asList(new PortsBuilder()
                .setAdministrativeState(AdminStates.InService).setLabel("2")
                .setOperationalState(State.InService).setPortDirection(Direction.Bidirectional)
                .setPortName("1").setPortQual(PortQual.XpdrNetwork)
                .setPortType("CFP2").setPortWavelengthType(PortWavelengthTypes.Wavelength)
                .setSupportedInterfaceCapability(Arrays.asList(IfOCH.class))
                .setTransponderPort(new TransponderPortBuilder()
                        .setPortPowerCapabilityMaxRx(PowerDBm.getDefaultInstance("1.0"))
                        .setPortPowerCapabilityMaxTx(PowerDBm.getDefaultInstance("0.0"))
                        .setPortPowerCapabilityMinRx(PowerDBm.getDefaultInstance("22.0"))
                        .setPortPowerCapabilityMinTx(PowerDBm.getDefaultInstance("5.0"))
                        .build())
                .build()))
        .setProductCode("Line_NW_P").setSerialId("_1234_")
        .setShelf("1").setSlot("1").setSubSlot("1").setType("line/network pluggable")
        .setVendor("VendorA").setIsPluggableOptics(true).build();
        OdlRpcObjectMapperXml mapper = new OdlRpcObjectMapperXml();
        String res = mapper.writeValueAsString(cpBldr.build(), CircuitPacks.class);
        String fileContent = null;
        try {
            fileContent = this.getTrimmedFileContent("/xml/roadm-circuitpacks.xml");
        } catch (IOException e1) {
            fail(e1.getMessage());
        }
        LOG.info("src={}", fileContent);
        LOG.info("res={}", res);
        try {
            assertTrue(XMLUnit.compareXML(fileContent, res).similar());
        } catch (SAXException | IOException e) {
            LOG.error(e.getMessage());
            fail(e.getMessage());
        }
    }
    @Test
    public void testNodeDeserializer() {
        String fileContent = null;
        try {
            fileContent = this.getTrimmedFileContent("/xml/node.xml");
        } catch (IOException e1) {
            fail(e1.getMessage());
        }
        OdlObjectMapperXml mapper = new OdlObjectMapperXml(true);
        try {
            LOG.info("node={}",mapper.readValue(fileContent,NetconfNode.class));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void testRoadmPortDeserializer() {
        String fileContent = null;
        try {
            fileContent = this.getTrimmedFileContent("/xml/roadm-ports2.xml");
        } catch (IOException e1) {
            fail(e1.getMessage());
        }
        OdlObjectMapperXml mapper = new OdlObjectMapperXml(true);
        try {
            Ports port = mapper.readValue(fileContent,Ports.class);
            LOG.info("port={}",port);
            assertEquals("1",port.getPortName());
            LOG.info("portName: {}",port.getPortName());
            assertEquals(State.InService,port.getOperationalState());
            assertEquals(PortQual.XpdrNetwork,port.getPortQual());
            assertEquals(AdminStates.InService,port.getAdministrativeState());
            assertEquals(Direction.Bidirectional,port.getPortDirection());
            assertEquals("2",port.getLabel());
            assertEquals(Arrays.asList(IfOCH.class),port.getSupportedInterfaceCapability());
            assertEquals("CFP2",port.getPortType());
            assertEquals(PortWavelengthTypes.Wavelength,port.getPortWavelengthType());
            TransponderPort tport = port.getTransponderPort();
            assertNotNull(tport);
            assertEquals(PowerDBm.getDefaultInstance("-5.0"),tport.getPortPowerCapabilityMinTx());
            assertEquals(PowerDBm.getDefaultInstance("-22.0"),tport.getPortPowerCapabilityMinRx());
            assertEquals(PowerDBm.getDefaultInstance("1.0"),tport.getPortPowerCapabilityMaxRx());
            assertEquals(PowerDBm.getDefaultInstance("0.0"),tport.getPortPowerCapabilityMaxTx());


        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void testRoadmInterfaces3Deserializer() {
        String fileContent = null;
        try {
            fileContent = this.getTrimmedFileContent("/xml/roadm-interfaces3.xml");
        } catch (IOException e1) {
            fail(e1.getMessage());
        }
        OdlObjectMapperXml mapper = new OdlObjectMapperXml(true);
        try {
            Interface if3 = mapper.readValue(fileContent,Interface.class);
            LOG.info("if3={}",if3);
            assertEquals("OMS-DEG2-TTP-TXRX",if3.getName());
            assertEquals("2/0",if3.getSupportingCircuitPackName());
            assertEquals(State.InService,if3.getOperationalState());
            assertEquals("OTS-DEG2-TTP-TXRX",if3.getSupportingInterface());
            assertEquals("TBD",if3.getCircuitId());
            assertEquals(AdminStates.InService,if3.getAdministrativeState());
            assertEquals("L1",if3.getSupportingPort());
            assertEquals(OpenROADMOpticalMultiplex.class,if3.getType());
            assertEquals("TBD",if3.getDescription());

        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}
