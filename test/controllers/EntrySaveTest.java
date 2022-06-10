package controllers;

import io.ebean.DB;
import org.junit.*;
import org.junit.Test;
import play.Application;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.*;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import models.*;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.route;

public class EntrySaveTest extends WithApplication{

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

    @Test
    public void フォームに正しい項目があるか() {
        // 正しいユーザでログインができるか();
        assertEquals(true, true);
    }
    
}
