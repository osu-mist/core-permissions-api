package edu.oregonstate.mist.permissionsapi.resources

import edu.oregonstate.mist.api.Resource
import edu.oregonstate.mist.api.jsonapi.ResourceObject
import edu.oregonstate.mist.api.jsonapi.ResultObject
import edu.oregonstate.mist.permissionsapi.core.Permissions
import edu.oregonstate.mist.permissionsapi.db.PermissionsDAO

import com.google.common.base.Optional
import io.dropwizard.jersey.params.NonEmptyStringParam

import javax.annotation.security.PermitAll
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path('/permissions')
@PermitAll
@Produces(MediaType.APPLICATION_JSON)
class PermissionsResource extends Resource {

    private final PermissionsDAO dao

    PermissionsResource(PermissionsDAO dao) {
        this.dao = dao
    }

    /**
     * Endpoint for retrieving permissions by OSU ID
     *
     * @param osuID
     * @return Response
     */
    @GET
    @Path("{osuID}")
    Response getPermissionsById(@PathParam("osuID") NonEmptyStringParam osuID) {
        Permissions permissions = dao.getPermissionsById(osuID.get())
        if(!permissions) {
            return notFound().build()
        }
        ResultObject permissionsResult = permissionsResultObject(permissions)
        ok(permissionsResult).build()
    }

    /**
     * Endpoint for retrieving permissions by parameters
     *
     * @param osuID
     * @param onid
     * @return Response
     */
    @GET
    Response getPermissions(@QueryParam("osuID") Optional<String> osuID,
                            @QueryParam("onid") Optional<String> onid) {
        if(!(osuID.isPresent() ^ onid.isPresent())) {
            return badRequest("Exactly one of [osuID, onid] must be used").build()
        }

        List<Permissions> permissions = dao.getPermissions(osuID.orNull(), onid.or(""))
        ResultObject permissionsResult = permissionsResultObject(permissions)
        ok(permissionsResult).build()
    }

    /**
     * Builds JSON API ResourceObject from permissions object
     *
     * @param permissions
     * @return ResourceObject
     */
    ResourceObject permissionsResourceObject(Permissions permissions) {
        new ResourceObject(
                type: "permissions",
                attributes: permissions,
                links: null
                // TODO: Add self link
        )
    }

    /**
     * Builds JSON API ResultObject from permissions object
     *
     * @param permissions
     * @return ResultObject
     */
    ResultObject permissionsResultObject(Permissions permissions) {
        new ResultObject(data: permissionsResourceObject(permissions))
    }

    /**
     * Builds JSON API ResultObject with list of ResourceObjects using
     *      a list of permissions objects
     *
     * @param permissions
     * @return ResultObject
     */
    ResultObject permissionsResultObject(List<Permissions> permissions) {
        new ResultObject(data: permissions.collect { permissionsResourceObject(it) })
    }
}
