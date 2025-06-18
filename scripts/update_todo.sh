#!/bin/bash

curl -v http://localhost:8080/todos/1 \
  -X PATCH \
  -d @todo.json \
  -H "Accept: application/json" \
  -H "Content-Type: application/json"