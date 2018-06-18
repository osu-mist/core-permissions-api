package edu.oregonstate.mist.permissionsapi.resources

import edu.oregonstate.mist.api.Resource
import edu.oregonstate.mist.permissionsapi.db.PermissionsDAO

import javax.annotation.security.PermitAll
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path('/permissions')
@PermitAll
@Produces(MediaType.APPLICATION_JSON)
class PermissionsResource extends Resource {

    private final PermissionsDAO dao

    PermissionsResource(PermissionsDAO dao) {
        this.dao = dao
    }
}
