package edu.oregonstate.mist.api.permissions

import com.google.common.base.Optional
import edu.oregonstate.mist.permissionsapi.db.PermissionsDAO
import edu.oregonstate.mist.permissionsapi.core.Permissions
import edu.oregonstate.mist.permissionsapi.resources.PermissionsResource
import org.junit.Test
import groovy.mock.interceptor.MockFor

class PermissionsResourceTest {

    String id = "123456789"
    String username = "user"
    Permissions permissions = new Permissions(
            id: id,
            username: username,
            hrPermissionLevel: 1,
            financialPermissionLevel: 1,
            studentPermissionLevel: 1
    )
    URI baseURI = new URI("https://localhost:8080/v1/")

    // Test getPermissions
    @Test
    void testGetPermissions() {
        def mockDao = new MockFor(PermissionsDAO)
        mockDao.demand.getPermissions (0..4)
                { String id, String username -> permissions }
        def dao = mockDao.proxyInstance()
        PermissionsResource resource = new PermissionsResource(dao, baseURI)

        Optional.with {
            // Valid request using id
            def validID = resource.getPermissions(of(id), absent())
            validateResponse(validID, 200, null)

            // Valid request using username
            def validUser = resource.getPermissions(absent(), of(username))
            validateResponse(validUser, 200, null)

            // Invalid request using username and id
            def bothFields = resource.getPermissions(of(id), of(username))
            validateResponse(bothFields, 400, "Exactly one of")

            // Invalid request using neither username or id
            def noFields = resource.getPermissions(absent(), absent())
            validateResponse(noFields, 400, "Exactly one of")
        }
    }

    // Test getPermissionsById

    // Valid id
    @Test
    void testValidId() {
        def mockDao = new MockFor(PermissionsDAO)
        mockDao.demand.getPermissions (0..1)
                { String id, String username -> permissions }
        def dao = mockDao.proxyInstance()
        PermissionsResource resource = new PermissionsResource(dao, baseURI)

        def validId = resource.getPermissionsById(id)
        validateResponse(validId, 200, null)
    }

    // Invalid id
    @Test
    void testInvalidId() {
        def mockDao = new MockFor(PermissionsDAO)
        mockDao.demand.getPermissions (0..1)
                { String id, String username -> }
        def dao = mockDao.proxyInstance()
        PermissionsResource resource = new PermissionsResource(dao, baseURI)

        def invalidId = resource.getPermissionsById(id)
        validateResponse(invalidId, 404, null)
    }

    void validateResponse(def response, Integer status, String message) {

        if(status) {
            assert response.status == status
        }
        if(message) {
            assert response.entity.developerMessage.contains(message)
        }
    }
}
