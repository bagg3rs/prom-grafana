# Prometheus and Grafana Demo
Promethues SNMP_Exporter Blackbox_Exporter Grafana and image renderer

## Prerequisites
* install docker and docker-compose see [See Docker installation] (https://docs.docker.com/get-docker/)
* install git
## Deploy the things
* clone repo
  * enter the cli and run `git clone 
https://github.com/bagg3rs/prom-grafana`
  * `cd prom-grafana`
  * `docker network create prometheus`
  * to start the containers run `docker-compose up`
* to stop run control c
* access Grafana Dashboard - http://localhost:3000
  * username `admin` password `changeme`
* access Prometheus Dashboard - http://localhost:9090 allows you to query and test metrics from Prometheus itself and other Exporters under Status > Targets.
* access Promethues SNMP_Exporter - http://localhost:9116
* access Promethues Blackbox_Exporter - http://localhost:9115
