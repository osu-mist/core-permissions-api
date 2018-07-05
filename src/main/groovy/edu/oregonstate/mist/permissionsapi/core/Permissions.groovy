package edu.oregonstate.mist.permissionsapi.core

import com.fasterxml.jackson.annotation.JsonIgnore

class Permissions {
    @JsonIgnore
    String osuID

    String onid

    int hrPermissionLevel

    int studentPermissionLevel

    int financialPermissionLevel
}
