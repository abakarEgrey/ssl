#!/bin/bash
set -x

if [ "$#" -ne 1 ]; then
  echo "you must give one argument : for example https://swisscom.com"
  exit 1
fi

curl --location --request POST 'http://localhost:8080/ssl/certificate/fetch' \
--header 'Content-Type: application/json' \
--data-raw '{
    "url": "'$1'"
}'