#!/bin/bash

# taken from:
# https://github.com/docker-library/postgres/issues/151

set -e

POSTGRES="psql --username ${POSTGRES_USER} -d ${POSTGRES_DB}"

echo "Creating database: ${POSTGRES_DB}"

$POSTGRES <<EOSQL
CREATE DATABASE ${POSTGRES_DB} OWNER ${POSTGRES_USER};
\connect ${POSTGRES_DB};
CREATE EXTENSION postgis;
EOSQL
