#!/bin/bash

set -x
echo 'build images ...'
docker-compose build
echo 'show images'
docker images
set +x