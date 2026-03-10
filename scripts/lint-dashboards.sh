#!/bin/bash
# Lint Grafana dashboard JSON files for common issues
# Run: ./scripts/lint-dashboards.sh
# Exit code: 0 = pass, 1 = failures found

set -euo pipefail

DASHBOARD_DIR="grafana/conf/provisioning/dashboards"
ERRORS=0

# Known valid datasource UIDs (update when adding new datasources)
VALID_DS_UIDS="PBFA97CFB590B2093 tempo ffetanr16zi0we"

echo "Linting dashboards in $DASHBOARD_DIR..."
echo ""

for f in "$DASHBOARD_DIR"/*.json; do
  [ -f "$f" ] || continue
  name=$(basename "$f")

  # 1. Valid JSON
  if ! python3 -m json.tool "$f" > /dev/null 2>&1; then
    echo "FAIL [$name] Invalid JSON"
    ERRORS=$((ERRORS + 1))
    continue
  fi

  # 2. Stale metric prefix (hass_ without homeassistant_)
  if grep -qE '"hass_sensor_|"hass_binary_sensor_|"hass_switch_' "$f"; then
    echo "FAIL [$name] Stale metric prefix: uses hass_* instead of homeassistant_*"
    grep -nE '"hass_sensor_|"hass_binary_sensor_|"hass_switch_' "$f" | head -3
    ERRORS=$((ERRORS + 1))
  fi

  # 3. Datasource UIDs
  ds_uids=$(python3 -c "
import json,sys
def find_uids(obj):
    if isinstance(obj, dict):
        if 'uid' in obj and obj.get('type') in ('prometheus','tempo','loki'):
            print(obj['uid'])
        for v in obj.values():
            find_uids(v)
    elif isinstance(obj, list):
        for v in obj:
            find_uids(v)
with open(sys.argv[1]) as fh:
    find_uids(json.load(fh))
" "$f" 2>/dev/null | sort -u)

  for uid in $ds_uids; do
    found=0
    for valid in $VALID_DS_UIDS; do
      if [ "$uid" = "$valid" ]; then
        found=1
        break
      fi
    done
    if [ "$found" -eq 0 ]; then
      echo "FAIL [$name] Unknown datasource UID: $uid"
      ERRORS=$((ERRORS + 1))
    fi
  done

  # 4. Empty queries in panels with targets
  empty_count=$(python3 -c "
import json,sys
with open(sys.argv[1]) as fh:
    d = json.load(fh)
count = 0
for p in d.get('panels', []):
    for t in p.get('targets', []):
        expr = t.get('expr', '') or t.get('query', '')
        qt = t.get('queryType', '')
        sn = t.get('serviceName', '')
        if not expr and not sn and qt not in ('nativeSearch', 'traceqlSearch', 'search', ''):
            count += 1
            title = p.get('title', '?')
            ref = t.get('refId', '?')
            print(f'  Panel: {title} target {ref}', file=sys.stderr)
print(count)
" "$f" 2>/dev/null)

  if [ "${empty_count:-0}" -gt 0 ] 2>/dev/null; then
    echo "FAIL [$name] $empty_count empty query target(s)"
    ERRORS=$((ERRORS + 1))
  fi

  # 5. TraceQL uses resource.service.name (not bare service.name)
  bad_traceql=$(python3 -c "
import json,sys,re
with open(sys.argv[1]) as fh:
    d = json.load(fh)
for p in d.get('panels', []):
    for t in p.get('targets', []):
        q = t.get('query', '')
        if 'service.name' in q and 'resource.service.name' not in q:
            title = p.get('title', '?')
            print(f'  Panel: {title}')
" "$f" 2>/dev/null)

  if [ -n "$bad_traceql" ]; then
    echo "FAIL [$name] TraceQL uses service.name without resource. prefix:"
    echo "$bad_traceql"
    ERRORS=$((ERRORS + 1))
  fi

  echo "OK   [$name]"
done

echo ""
if [ "$ERRORS" -eq 0 ]; then
  echo "All dashboards passed lint checks"
  exit 0
else
  echo "$ERRORS issue(s) found"
  exit 1
fi
