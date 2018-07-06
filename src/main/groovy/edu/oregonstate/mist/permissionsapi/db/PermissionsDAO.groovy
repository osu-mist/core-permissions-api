package edu.oregonstate.mist.permissionsapi.db

import edu.oregonstate.mist.permissionsapi.core.Permissions
import edu.oregonstate.mist.permissionsapi.mapper.PermissionsMapper

import org.skife.jdbi.v2.sqlobject.Bind
import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper

@RegisterMapper(PermissionsMapper)
interface PermissionsDAO extends Closeable {

    @SqlQuery("""
        SELECT OsuId,
               Onid,
               HumanResourcesSecurityPermissionLevel,
               StudentSecurityPermissionLevel,
               FinanceSecurityPermissionLevel
               
        FROM CoreDataWarehouse.sys2sys.CoreUserSecurityDomainPermissionLevels_EcsApi_1_1_0
        WHERE OsuId = :id
        OR Onid = :username
    """)
    Permissions getPermissions(@Bind("id") String id,
                               @Bind("username") String username)

    @Override
    void close()
}
