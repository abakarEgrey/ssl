#!/bin/bash
set -x

curl --location --request POST 'http://localhost:8080/ssl/certificate/fetch' \
--header 'Content-Type: application/json' \
--data-raw '{
    "url": "'$1'"
}'