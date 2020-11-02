# Prometheus and Grafana Demo
Promethues SNMP_Exporter Blackbox_Exporter Grafana inclding image renderer

## Prerequisites
* `docker and docker-compose` see [See Docker installation] (https://docs.docker.com/get-docker/)
* `git` 
## Deploy the things
* clone repo down to your machine
  * enter the cli and run `git clone 
https://github.com/bagg3rs/prom-grafana`
  * `cd prom-grafana`
  * create a docker network `docker network create prometheus` 
  * start the containers  `docker-compose up`
* To stop run `control c` on keyboard
* access Grafana Dashboard - http://localhost:3000
  * username `admin` password `changeme`
* Access Prometheus Dashboard - http://localhost:9090 allows you to query and test metrics from Prometheus itself and other Exporters under `Status > Targets`
* Access Promethues SNMP_Exporter - http://localhost:9116
* Access Promethues Blackbox_Exporter - http://localhost:9115
