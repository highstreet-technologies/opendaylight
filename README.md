# Opendaylight

## odl-ready

A http service for Opendaylight to show if every bundle is started fine.

GET /ready

200 => ready
!200 (401|404) => not ready

## odl-client

A client which can connect to an instance of SDN-R to provide a southbound Restconf interface instead of southbound NETCONF interface.
