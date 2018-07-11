package edu.oregonstate.mist.permissionsapi

import edu.oregonstate.mist.api.Configuration
import io.dropwizard.db.DataSourceFactory
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.Valid
import javax.validation.constraints.NotNull

class PermissionsConfiguration extends Configuration {
    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory database = new DataSourceFactory()

    /**
     *
     * @return DataSourceFactory
     */
    DataSourceFactory getDataSourceFactory() {
        database
    }
}
