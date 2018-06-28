# CORE Permissions API

This API retrieves CORE permissions information on different users. More information on CORE permissions can be found [here](https://fa.oregonstate.edu/bic/core/core-access-and-security-structure).

## Skeleton
This API is based on the web-api-skeleton. For more documentation on the skeleton and the framework, see the github repo: https://github.com/osu-mist/web-api-skeleton

## Generate Keys

HTTPS is required for Web APIs in development and production. Use keytool(1) to generate public and private keys.

Generate key pair and keystore:

```
$ keytool \
  -genkeypair \
  -dname "CN=Jane Doe, OU=Enterprise Computing Services, O=Oregon State University, L=Corvallis, S=Oregon, C=US" \
  -ext "san=dns:localhost,ip:127.0.0.1" \
  -alias doej \
  -keyalg RSA \
  -keysize 2048 \
  -sigalg SHA256withRSA \
  -validity 365 \
  -keystore doej.keystore
```

Export certificate to file:

```
$ keytool \
  -exportcert \
  -rfc \
  -alias "doej" \
  -keystore doej.keystore \
  -file doej.pem
```

Import certificate into truststore:

```
$ keytool \
  -importcert \
  -alias "doej" \
  -file doej.pem \
  -keystore doej.truststore
```

## Gradle

This project uses the build automation tool Gradle. Use the [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) to download and install it automatically:

```
$ ./gradlew
```

The Gradle wrapper installs Gradle in the directory `~/.gradle`. To add it to your `$PATH`, add the following line to `~/.bashrc`:

```
$ export PATH=$PATH:/home/user/.gradle/wrapper/dists/gradle-2.4-all/WRAPPER_GENERATED_HASH/gradle-2.4/bin
```

The changes will take effect once you restart the terminal or `source ~/.bashrc`.

## Tasks

List all tasks runnable from root project:

```
$ gradle tasks
```

## IntelliJ IDEA

Generate IntelliJ IDEA project:

```
$ gradle idea
```

Open with `File` -> `Open Project`.

## Configure

Copy [configuration-example.yaml](configuration-example.yaml) to `configuration.yaml`. Modify as necessary, being careful to avoid committing sensitive data.

Please refer to [Location Frontend API](https://wiki.library.oregonstate.edu/confluence/display/CO/Location+Frontend+API) for `locations` session configuration

## Build

Build the project:

```
$ gradle build
```

JARs [will be saved](https://github.com/johnrengelman/shadow#using-the-default-plugin-task) into the directory `build/libs/`.

## Run

Run the project:

```
$ gradle run
```

## Endpoints

### /core-permissions/{osuID}

#### GET
This endpoint will return the HR, Student, and Finance permission levels of a person specified by their OSU ID. Each of these values will be an integer from 0 to 5.

##### Example of response
```json
{
  "data": {
    "id": "123456789",
    "type": "permissions",
    "attributes": {
      "HRPermissions": 4,
      "StudentPermissions": 3,
      "FinancePermissions": 2
    },
    "links": {
      "self": "https://api.oregonstate.edu/v1/permissions/123456789"
    }
  }
}
```

### /core-permissions

#### GET
This endpoint will return multiple permissions for users specified by parameters.

##### Example of response
```json
{
  "data": [
    {
      "id": "123456789",
      "type": "permissions",
      "attributes": {
       "HRPermissions": 4,
       "StudentPermissions": 3,
       "FinancePermissions": 2
      },
      "links": {
       "self": "https://api.oregonstate.edu/v1/permissions/123456789"
      }
    }
  ]
}
```
