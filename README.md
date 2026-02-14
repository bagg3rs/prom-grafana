# Prometheus & Grafana Monitoring Stack

A comprehensive monitoring stack using Docker Compose with Prometheus, Grafana, and multiple exporters for system, network, and application monitoring.

## 🚀 Features

- **Grafana** - Visualization and dashboards with image renderer for rich notifications
- **Prometheus** - Time-series database and metrics collection
- **Blackbox Exporter** - HTTP/HTTPS/ICMP probing
- **SNMP Exporter** - Network device monitoring
- **cAdvisor** - Container metrics
- **Speedtest Exporter** - Internet bandwidth monitoring
- **Uptime Kuma** - Uptime monitoring with notifications
- **Traefik Integration** - All services can be exposed via reverse proxy

## 📋 Prerequisites

- Docker & Docker Compose installed ([installation guide](https://docs.docker.com/get-docker/))
- Git
- (Optional) Traefik reverse proxy for HTTPS access

## 🛠️ Quick Start

### 1. Clone the repository

```bash
git clone https://github.com/bagg3rs/prom-grafana
cd prom-grafana
```

### 2. Create the Docker network

```bash
docker network create monitor-net
```

### 3. Configure environment variables

Copy the example environment file:

```bash
cp .env.example .env
```

Edit `.env` and configure:

**Required:**
- `GF_SECURITY_ADMIN_USER` - Grafana admin username (default: admin)
- `GF_SECURITY_ADMIN_PASSWORD` - **Change this!** (default: changeme)

**Optional (if using Traefik):**
- `GRAFANA` - Grafana domain (e.g., grafana.yourdomain.com)
- `PROMETHEUS` - Prometheus domain
- `SNMP` - SNMP Exporter domain
- `BLACKBOX` - Blackbox Exporter domain
- `CADVISOR` - cAdvisor domain
- `UPTIME` - Uptime Kuma domain

**Optional (if monitoring Home Assistant):**
- `HASS_TOKEN` - Home Assistant Long-Lived Access Token
- `HASS_URL` - Home Assistant URL (e.g., http://192.168.1.100:8123)

### 4. Configure monitoring targets

Edit `prometheus/prometheus.yml` to add your monitoring targets:

**SNMP devices:**
```yaml
- job_name: 'snmp_devices'
  static_configs:
    - targets:
      - firewall.local
      - switch.local
      - access-point.local
```

**HTTP endpoints:**
```yaml
- job_name: 'blackbox'
  static_configs:
    - targets:
      - https://yoursite.com
      - https://api.yourservice.com
```

**ICMP ping targets:**
```yaml
- job_name: blackbox-ping
  static_configs:
    - targets:
      - 192.168.1.1
      - your-server.local
```

### 5. Start the stack

```bash
docker-compose up -d
```

## 🌐 Access Services

**Without Traefik (local access):**
- Grafana: http://localhost:3000 (admin / changeme)
- Prometheus: http://localhost:9090
- Uptime Kuma: http://localhost:3001
- SNMP Exporter: http://localhost:9116
- Blackbox Exporter: http://localhost:9115
- cAdvisor: http://localhost:8080

**With Traefik:**
- Access via your configured domains (HTTPS with Let's Encrypt)

## 📊 Default Metrics

Out of the box, the stack monitors:

- **Container metrics** - CPU, memory, network for all Docker containers
- **Internet speed** - Hourly bandwidth tests
- **Public DNS** - 8.8.8.8 (Google), 1.1.1.1 (Cloudflare)
- **Public sites** - github.com, google.com

## 🔧 Configuration

### Adding Grafana Datasources

Datasources are auto-provisioned from `grafana/provisioning/datasources/`. Prometheus is configured by default.

### Adding Grafana Dashboards

Place dashboard JSON files in `grafana/provisioning/dashboards/` - they'll be auto-imported on startup.

### Home Assistant Integration

1. Generate a Long-Lived Access Token in Home Assistant:
   - Settings → Profile → Long-Lived Access Tokens → Create Token
2. Add to `.env`:
   ```
   HASS_TOKEN=your_token_here
   HASS_URL=http://your-home-assistant:8123
   ```
3. Uncomment the `hass` job in `prometheus/prometheus.yml`
4. Restart: `docker-compose restart prometheus`

### Data Retention

Prometheus retains metrics for **1 year** by default. Change in `docker-compose.yml`:

```yaml
- '--storage.tsdb.retention.time=1y'  # Change to 30d, 6m, 2y, etc.
```

## 🔒 Security Notes

1. **Change default Grafana password** - Set `GF_SECURITY_ADMIN_PASSWORD` in `.env`
2. **Never commit `.env`** - Contains sensitive tokens and passwords
3. **Use Traefik with auth** - Add basic auth middleware for external access
4. **Revoke old tokens** - If you ever commit tokens accidentally, revoke them immediately
5. **Consider making repo private** - If storing personal network configs

## 🐛 Troubleshooting

**Network not found:**
```bash
docker network create monitor-net
```

**Permission denied on volumes:**
```bash
sudo chown -R 472:472 ./grafana
sudo chown -R 65534:65534 ./prometheus
```

**Grafana login fails:**
- Check `.env` for correct credentials
- Default: admin / changeme
- Reset: `docker-compose down -v` (⚠️ deletes data!)

**Prometheus targets down:**
- Check `http://localhost:9090/targets`
- Verify network connectivity to target hosts
- Check firewall rules

**SNMP not working:**
- Verify SNMP enabled on target devices
- Check community strings match
- Update `snmp.yml` with correct modules

## 📁 Directory Structure

```
.
├── config/              # Blackbox exporter config
├── grafana/
│   └── provisioning/    # Auto-provisioned datasources & dashboards
├── prometheus/          # Prometheus config & targets
├── snmp_exporter/       # SNMP exporter config
├── uptime-kuma/         # Uptime Kuma data
├── docker-compose.yml   # Main compose file
└── .env.example         # Environment variables template
```

## 🔄 Updates

Update all services to latest versions:

```bash
docker-compose pull
docker-compose up -d
```

**Note:** Using `:latest` tags means automatic updates. Pin specific versions in production.

## 📝 License

MIT

## 🤝 Contributing

Issues and PRs welcome!

## ⚠️ Important Notes

- This stack is designed for **internal networks** or behind a **reverse proxy with authentication**
- **Never expose Prometheus/Grafana directly to the internet** without authentication
- Monitor your disk space - metrics data can grow large with many targets
- Speedtest exporter runs hourly to avoid excessive bandwidth usage
