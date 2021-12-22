# Prometheus and Grafana Demo
Promethues, SNMP_Exporter, Blackbox_Exporter and Grafana which includes the image renderer container which enables rich notifications in slack/telegram etc

## Prerequisites
* `docker and docker-compose` see [See Docker installation] (https://docs.docker.com/get-docker/)
* `git` 
## Deploy the things
* clone repo down to your machine
  * enter the cli and run `git clone 
https://github.com/bagg3rs/prom-grafana`
  * `cd prom-grafana`
  * create a docker network `docker network create monitor-net` 
  * start the containers  `docker-compose up`
* the output from the containers will be shown in the terminal, to access grafana dashboard - open browser  http://localhost:3000
 username `admin` password `changeme`
* access Prometheus Dashboard - http://localhost:9090 allows you to query and test metrics from Prometheus itself and other Exporters under `Status > Targets`
* access Promethues SNMP_Exporter - http://localhost:9116
* access Promethues Blackbox_Exporter - http://localhost:9115
