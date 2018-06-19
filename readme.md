# CORE Permissions API

This API retrieves CORE permissions information on different users. More information on CORE permissions can be found [here](https://fa.oregonstate.edu/bic/core/core-access-and-security-structure).

## Endpoints

### /permissions/{osuID}

#### GET
This endpoint will return the HR, Student, and Finance permission levels of a person specified by their OSU ID. Each of these values will be an integer from 1 to 5.

##### Example of response
```json
{
  "data": {
    "id": 123456789,
    "type": "permissions",
    "attributes": {
      "HRPermissions": 4,
      "StudentPermissions": 3,
      "FinancePermissions": 5
    },
    "links": {
      "self": "https://api.oregonstate.edu/v1/permissions/123456789"
    }
  }
}
```

### /permissions

#### GET
This endpoint will return multiple permissions for users specified by parameters.

#### Example of response
```json
{
  "data": [
    {
      "id": 123456789,
      "type": "permissions",
      "attributes": {
       "HRPermissions": 4,
       "StudentPermissions": 3,
       "FinancePermissions": 5
      },
      "links": {
       "self": "https://api.oregonstate.edu/v1/permissions/123456789"
      }
    }
  ]
}
```