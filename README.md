# my-cfhn-login
Login microservice for the my cfhn platform

# Setup
Before running, you need to set the ldap password and the JWT keys.
You can generate the keys with the keys/genkeys.sh script. The public key is inside the `login-ms-jwt-key-pub.pem` file,
and the private key is inside the `login-ms-jwt-key-pkcs8.pem` file.

# Build
```
mvn clean install
docker-compose build
```

# Run
The default run configuration needs traefik to expose the service. To change that, you can add a port mapping inside the docker-compose file.
Then, run it with
```
docker-compose up -d
```