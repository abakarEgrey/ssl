#!/bin/bash
set -x
set -e

psql -c "ALTER USER postgres WITH PASSWORD 'ssl'"
psql -f create_db.sql  -d postgres