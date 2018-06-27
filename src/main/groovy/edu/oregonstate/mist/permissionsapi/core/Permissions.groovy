package edu.oregonstate.mist.permissionsapi.core

import com.fasterxml.jackson.annotation.JsonIgnore

import javax.validation.constraints.NotNull

class Permissions {
    @JsonIgnore
    String osuID

    @NotNull
    String onid

    @NotNull
    int hrPermissionLevel

    @NotNull
    int studentPermissionLevel

    @NotNull
    int financialPermissionLevel
}
