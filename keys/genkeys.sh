#!/bin/bash

openssl ecparam -name prime256v1 -genkey -noout -out login-ms-jwt-key.pem
openssl pkcs8 -topk8 -nocrypt -in login-ms-jwt-key.pem -out login-ms-jwt-key-pkcs8.pem
openssl ec -in login-ms-jwt-key.pem -pubout -out login-ms-jwt-key-pub.pem
