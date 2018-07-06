package edu.oregonstate.mist.permissionsapi.core

import com.fasterxml.jackson.annotation.JsonIgnore

class Permissions {
    @JsonIgnore
    String id

    String username

    int hrPermissionLevel

    int studentPermissionLevel

    int financialPermissionLevel
}
