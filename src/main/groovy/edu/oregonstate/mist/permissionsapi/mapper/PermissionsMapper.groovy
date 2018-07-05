package edu.oregonstate.mist.permissionsapi.mapper

import edu.oregonstate.mist.permissionsapi.core.Permissions
import org.skife.jdbi.v2.StatementContext
import org.skife.jdbi.v2.tweak.ResultSetMapper
import java.sql.ResultSet
import java.sql.SQLException

class PermissionsMapper implements ResultSetMapper<Permissions> {
    Permissions map(int i, ResultSet rs, StatementContext sc) throws SQLException {
        new Permissions(
                osuID: rs.getString("OsuID"),
                onid: rs.getString("Onid"),
                hrPermissionLevel: rs.getInt("HumanResourcesSecurityPermissionLevel"),
                studentPermissionLevel: rs.getInt("StudentSecurityPermissionLevel"),
                financialPermissionLevel: rs.getInt("FinanceSecurityPermissionLevel")
        )
    }
}
