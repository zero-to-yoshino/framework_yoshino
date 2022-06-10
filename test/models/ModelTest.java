package models;

import play.Application;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.*;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import io.ebean.DB;

import java.beans.Transient;
import java.util.List;


import org.junit.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static play.test.Helpers.*;

public class ModelTest extends WithApplication{

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
  public void NotNullが正しくできているか() throws Exception {
    Entry entry = new Entry(null, "title1", "message1", null, null);
    DB.save(entry);
    List<Entry> Entries = DB.find(Entry.class).findList();
    String name = Entries.get(0).getName();
    assertEquals(Entries.size(), 1);
  }

  // @Test
  // public void 名前にnull値の保存を認めない() throws Exception {
  //   Connection connection = database.getConnection();
  //   try {
  //       connection.prepareStatement("insert into entry values (10, null, 'title', 'message')").execute();
  //   } catch(org.h2.jdbc.JdbcSQLException e) {       
  //       assertThat(e.getMessage(), is(containsString("列 \"NAME\" にはnull値が許されていません")));
  //   }
  // }

  // @Test
  // public void タイトルにnull値の保存を認めない() throws Exception {
  //   Connection connection = database.getConnection();
  //   try {
  //       connection.prepareStatement("insert into entry values (10, 'name', null, 'message')").execute();
  //   } catch(org.h2.jdbc.JdbcSQLException e) {       
  //       assertThat(e.getMessage(), is(containsString("列 \"TITLE\" にはnull値が許されていません")));
  //   }
  // }

  // @Test
  // public void メッセージにnull値の保存を認めない() throws Exception {
  //   Connection connection = database.getConnection();
  //   try {
  //       connection.prepareStatement("insert into entry values (10, 'name', 'title', null)").execute();
  //   } catch(org.h2.jdbc.JdbcSQLException e) { 
  //       assertThat(e.getMessage(), is(containsString("列 \"MESSAGE\" にはnull値が許されていません")));
  //   }
  // }
}
