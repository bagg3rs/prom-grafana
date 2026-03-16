# 📊 Prometheus + Grafana Stack

[![Last Commit](https://img.shields.io/github/last-commit/bagg3rs/prom-grafana?style=flat-square)](https://github.com/bagg3rs/prom-grafana/commits)
[![Stars](https://img.shields.io/github/stars/bagg3rs/prom-grafana?style=flat-square)](https://github.com/bagg3rs/prom-grafana/stargazers)
[![Shell](https://img.shields.io/badge/shell-bash-green?style=flat-square&logo=gnu-bash)](https://www.gnu.org/software/bash/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg?style=flat-square)](LICENSE)
[![Docker](https://img.shields.io/badge/docker-compose-2496ED?style=flat-square&logo=docker)](https://docs.docker.com/compose/)

A production-ready monitoring stack using **Prometheus**, **Grafana**, and friends via docker-compose.

Perfect for home lab environments, development setups, or learning modern observability practices.

## 🎯 Features

- **Prometheus** - Time-series metrics collection and alerting
- **Grafana** - Beautiful dashboards and visualizations
- **Node Exporter** - System metrics collection
- **cAdvisor** - Container metrics
- **AlertManager** - Alert routing and management
- **Docker Compose** - Simple deployment and orchestration

## 🚀 Quick Start

```bash
# Clone the repository
git clone https://github.com/bagg3rs/prom-grafana.git
cd prom-grafana

# Start the stack
docker-compose up -d

# Access services
# Grafana: http://localhost:3000 (admin/admin)
# Prometheus: http://localhost:9090
# AlertManager: http://localhost:9093
```

## 📦 What's Included

- Pre-configured Prometheus targets
- Grafana dashboards for system and container monitoring
- Alert rules for common scenarios
- Persistent data storage
- Network isolation for security

## 🛠️ Configuration

Edit `prometheus/prometheus.yml` to add your own targets and scrape configs.

Custom Grafana dashboards can be added to `grafana/dashboards/`.

## 📚 Documentation

See [Prometheus Documentation](https://prometheus.io/docs/) and [Grafana Documentation](https://grafana.com/docs/) for more details.

## 🤝 Contributing

Contributions welcome! Feel free to open issues or submit pull requests.

## 📄 License

MIT License - see [LICENSE](LICENSE) file for details.
