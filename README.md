# Opendaylight

## odl-ready

A http service for Opendaylight to show if every bundle is started fine. So to be more specific:
  * no bundle is on failure state
  * all bundles which names contains 'mdsal', 'netconf', 'restconf', 'ccsdk' or 'devicemanager' are on active state (resolved is not enough)

GET /ready

  * 200 => ready
  * !200 (401|404) => not ready

## odl-client

A client which can connect to an instance of SDN-R to provide a southbound Restconf interface instead of southbound NETCONF interface.


## Versioning

| version  | odl-release |
| -------- | ----------- |
| 2.1.X    | aluminium-SR1 |
| 3.1.X    | silicon-SR1 |
| 3.2.X    | silicon-SR2 |