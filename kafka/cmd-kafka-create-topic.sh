#!/bin/bash

docker exec mcm-demo-broker kafka-topics --create --topic created-users-topic --bootstrap-server mcm-demo-broker:9093 --partitions 1 --replication-factor 1