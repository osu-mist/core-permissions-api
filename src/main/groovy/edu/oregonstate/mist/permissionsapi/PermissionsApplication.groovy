package edu.oregonstate.mist.permissionsapi

import edu.oregonstate.mist.api.Application
import edu.oregonstate.mist.permissionsapi.db.PermissionsDAO
import edu.oregonstate.mist.permissionsapi.resources.PermissionsResource
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import io.dropwizard.jdbi.DBIFactory
import org.skife.jdbi.v2.DBI

/**
 * Main application class.
 */
class PermissionsApplication extends Application<PermissionsConfiguration> {
    /**
     * Parses command-line arguments and runs the application.
     *
     * @param configuration
     * @param environment
     */
    @Override
    void initialize(Bootstrap<PermissionsConfiguration> bootstrap) {}

    @Override
    public void run(PermissionsConfiguration configuration, Environment environment) {
        final DBIFactory FACTORY = new DBIFactory()
        final DBI JDBI = FACTORY.build(environment, configuration.getDataSourceFactory(), "jdbi")
        final PermissionsDAO DAO = JDBI.onDemand(PermissionsDAO.class)

        environment.jersey().register(new PermissionsResource(DAO))

        this.setup(configuration, environment)
    }

    /**
     * Instantiates the application class with command-line arguments.
     *
     * @param arguments
     * @throws Exception
     */
    public static void main(String[] arguments) throws Exception {
        new PermissionsApplication().run(arguments)
    }
}
