# Prometheus and Grafana Demo
Promethues SNMP_Exporter Blackbox_Exporter Grafana and image renderer

## Prerequisites
* install docker and docker-compose see [See Docker installation] (https://docs.docker.com/get-docker/)
* install git
## Deploy the things
* clone repo
* enter the cli or download github app
* via cli run from a folder of your choice, cloning creates a folder of the repo for you
* git clone https://github.com/bagg3rs/prom-grafana
* cd prom-grafana
* docker network create prometheus
* docker-compose up
* to stop run control c
* access Grafana Dashboard - http://localhost:3000
  * username _admin_ password _changeme_
* access Prometheus Dashboard - http://localhost:9090 allows you to query and test metrics from Prometheus itself and other Exporters under Status > Targets.
* access Promethues SNMP_Exporter - http://localhost:9116
* access Promethues Blackbox_Exporter - http://localhost:9115
