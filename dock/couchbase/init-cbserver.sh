#!/bin/bash
# used to start couchbase server - can't get around this as docker compose only allows you to start one command - so we have to start couchbase like the standard couchbase Dockerfile would
# https://github.com/couchbase/docker/blob/master/enterprise/couchbase-server/7.0.3/Dockerfile#L82

/entrypoint.sh couchbase-server &

# track if setup is complete so we don't try to setup again
FILE=/opt/couchbase/init/setupComplete.txt

echo "Setup couchbase server"

if ! [ -f "$FILE" ]; then
  # used to automatically create the cluster based on environment variables
  # https://docs.couchbase.com/server/current/cli/cbcli/couchbase-cli-cluster-init.html


  sleep 60s

  echo "Try to create cluster"

  /opt/couchbase/bin/couchbase-cli cluster-init -c 127.0.0.1 \
  --cluster-username "$COUCHBASE_ADMINISTRATOR_USERNAME" \
  --cluster-password "$COUCHBASE_ADMINISTRATOR_PASSWORD" \
  --services data,index,query \
  --cluster-ramsize "$COUCHBASE_RAM_SIZE" \
  --cluster-index-ramsize "$COUCHBASE_INDEX_RAM_SIZE" \
  --index-storage-setting default

  sleep 5s

  # used to auto create the bucket based on environment variables
  # https://docs.couchbase.com/server/current/cli/cbcli/couchbase-cli-bucket-create.html

  echo "Try to create bucket"

  /opt/couchbase/bin/couchbase-cli bucket-create -c localhost:8091 \
  --username "$COUCHBASE_ADMINISTRATOR_USERNAME" \
  --password "$COUCHBASE_ADMINISTRATOR_PASSWORD" \
  --bucket "$COUCHBASE_BUCKET" \
  --bucket-ramsize "$COUCHBASE_BUCKET_RAMSIZE" \
  --bucket-type couchbase

  sleep 5s

  echo "Try to create index for product name"

  # create indexes using the QUERY REST API
  /opt/couchbase/bin/curl -v http://localhost:8093/query/service \
  -u "$COUCHBASE_ADMINISTRATOR_USERNAME":"$COUCHBASE_ADMINISTRATOR_PASSWORD" \
  -d 'statement=CREATE INDEX idx_products_name on products(name-q)'

  sleep 5s

  echo "Try to create index for product creator"

  /opt/couchbase/bin/curl -v http://localhost:8093/query/service \
  -u "$COUCHBASE_ADMINISTRATOR_USERNAME":"$COUCHBASE_ADMINISTRATOR_PASSWORD" \
  -d 'statement=CREATE INDEX idx_products_creator on products(creator)'

  # create file so we know that the cluster is setup and don't run the setup again
  touch $FILE
fi
  # docker compose will stop the container from running unless we do this
  # known issue and workaround
  tail -f /dev/null
