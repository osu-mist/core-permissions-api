package edu.oregonstate.mist.permissionsapi

import javax.ws.rs.core.UriBuilder

class PermissionsURIBuilder {

    URI baseURI

    PermissionsURIBuilder(URI baseURI) {
        this.baseURI = baseURI
    }

    /**
     * Builds a URI for a self link to a permissions object
     *
     * @param osuID
     * @return URI of self link to permissions object
     */
    URI permissionsURI(String osuID) {
        UriBuilder.fromUri(this.baseURI)
                  .path("core-permissions/{osuID}")
                  .build(osuID)
    }
}
