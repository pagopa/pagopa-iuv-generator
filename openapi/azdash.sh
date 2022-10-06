#!/bin/bash

# 1 - retivere del openapi

# 2 - crea il file cfg
cat <<EOF > config.yaml
oa3_spec: https://raw.githubusercontent.com/pagopa/pagopa-iuv-generator/fix-deploy/openapi/openapi.json
name: [UAT] IUV Generation service
location: West Europe
timespan: 5m
resources:
  - /subscriptions/26abc801-0d8f-4a6e-ac5f-8e81bcc09112/resourceGroups/pagopa-u-vnet-rg/providers/Microsoft.Network/applicationGateways/pagopa-u-app-gw
EOF


# 3 - crea la dash
docker run -v $(pwd):/home/nonroot/resources:Z \
  ghcr.io/pagopa/opex-dashboard:latest generate \
  --template-name azure-dashboard \
  --config-file /home/nonroot/resources/config.yaml > uat_dash_gps.json