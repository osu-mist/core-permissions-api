package edu.oregonstate.mist.permissionsapi.core

import com.fasterxml.jackson.annotation.JsonIgnore

import javax.validation.constraints.NotNull

class Permissions {
    @JsonIgnore
    int osuID

    @NotNull
    int hrPermissionLevel

    @NotNull
    int studentPermissionLevel

    @NotNull
    int financialPermissionLevel
}
