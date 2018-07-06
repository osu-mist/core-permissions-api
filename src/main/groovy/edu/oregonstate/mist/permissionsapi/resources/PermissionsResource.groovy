package edu.oregonstate.mist.permissionsapi.resources

import com.google.common.base.Optional

import edu.oregonstate.mist.api.Resource
import edu.oregonstate.mist.api.jsonapi.ResourceObject
import edu.oregonstate.mist.api.jsonapi.ResultObject
import edu.oregonstate.mist.permissionsapi.core.Permissions
import edu.oregonstate.mist.permissionsapi.db.PermissionsDAO
import edu.oregonstate.mist.permissionsapi.PermissionsURIBuilder

import javax.annotation.security.PermitAll
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/core-permissions")
@PermitAll
@Produces(MediaType.APPLICATION_JSON)
class PermissionsResource extends Resource {

    private final PermissionsDAO dao
    private URI baseURI
    private permissionsURIBuilder

    PermissionsResource(PermissionsDAO dao, URI baseURI) {
        this.dao = dao
        this.baseURI = baseURI
        this.permissionsURIBuilder = new PermissionsURIBuilder(baseURI)
    }

    /**
     * Endpoint for retrieving permissions by OSU ID
     *
     * @param id
     * @return Response
     */
    @GET
    @Path("{id}")
    Response getPermissionsById(@PathParam("id") String id) {
        Permissions permissions = dao.getPermissions(id, "")
        if (!permissions) {
            return notFound().build()
        }
        ResultObject permissionsResult = permissionsResultObject(permissions)
        ok(permissionsResult).build()
    }

    /**
     * Endpoint for retrieving permissions by parameters
     *
     * @param id
     * @param username
     * @return Response
     */
    @GET
    Response getPermissions(@QueryParam("id") Optional<String> id,
                            @QueryParam("username") Optional<String> username) {
        if (!(id.isPresent() ^ username.isPresent())) {
            return badRequest("Exactly one of [id, username] must be used").build()
        }

        Permissions permissions = dao.getPermissions(id.or(""), username.or(""))
        if (!permissions) {
            return notFound().build()
        }
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
                id: permissions.id,
                type: "permissions",
                attributes: permissions,
                links: ["self": permissionsURIBuilder.permissionsURI(permissions.id)]
        )
    }

    /**
     * Builds JSON API ResultObject from permissions object
     *
     * @param permissions
     * @return ResultObject
     */
    ResultObject permissionsResultObject(Permissions permissions) {
        new ResultObject(data: permissionsResourceObject(permissions), links: null)
    }
}
