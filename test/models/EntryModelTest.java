package models;

import play.db.Database;
import play.db.Databases;
import play.db.evolutions.*;

import java.beans.Transient;
import java.sql.Connection;

import org.junit.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static play.test.Helpers.*;

public class EntryModelTest {

  Database database;

//   エントリモデルのエボリューション
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
  public void 同じIDのデータを入れようとするとエラーを返す() throws Exception {
    Connection connection = database.getConnection();
    connection.prepareStatement("insert into entry values (10, 'test-name', 'title', 'message')").execute();
    try {
        connection.prepareStatement("insert into entry values (10, 'test-name', 'title', 'message')").execute();
    } catch(org.h2.jdbc.JdbcSQLException e) {       
        assertThat(e.getMessage(), is(containsString("ユニークインデックス、またはプライマリキー違反")));
    }
  }

  @Test
  public void 名前にnull値の保存を認めない() throws Exception {
    Connection connection = database.getConnection();
    try {
        connection.prepareStatement("insert into entry values (10, null, 'title', 'message')").execute();
    } catch(org.h2.jdbc.JdbcSQLException e) {       
        assertThat(e.getMessage(), is(containsString("列 \"NAME\" にはnull値が許されていません")));
    }
  }

  @Test
  public void タイトルにnull値の保存を認めない() throws Exception {
    Connection connection = database.getConnection();
    try {
        connection.prepareStatement("insert into entry values (10, 'name', null, 'message')").execute();
    } catch(org.h2.jdbc.JdbcSQLException e) {       
        assertThat(e.getMessage(), is(containsString("列 \"TITLE\" にはnull値が許されていません")));
    }
  }

  @Test
  public void メッセージにnull値の保存を認めない() throws Exception {
    Connection connection = database.getConnection();
    try {
        connection.prepareStatement("insert into entry values (10, 'name', 'title', null)").execute();
    } catch(org.h2.jdbc.JdbcSQLException e) { 
        assertThat(e.getMessage(), is(containsString("列 \"MESSAGE\" にはnull値が許されていません")));
    }
  }
}
