package controllers;

import play.db.Database;
import play.db.Databases;
import play.db.evolutions.*;
import play.Application;
import org.junit.*;
import org.junit.Test;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;

public class DatabasePreparation extends WithApplication {
    Database database;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Before
    public void setupDatabase() {
        database = Databases.inMemory();
        Evolutions.applyEvolutions(database);
    }

    @After
    public void shutdownDatabase() {
        Evolutions.cleanupEvolutions(database);
        database.shutdown();
    }
}
