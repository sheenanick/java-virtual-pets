import org.junit.rules.ExternalResource;
import org.sql2o.*;

public class DatabaseRule extends ExternalResource {

  @Override
  protected void before() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/pocket_monsters_test", null, null);
  }

  @Override
  protected void after() {
    try(Connection con = DB.sql2o.open()) {
      String deleteTrainersQuery = "DELETE FROM trainers *;";
      String deleteMonstersQuery = "DELETE FROM monsters *;";
      con.createQuery(deleteTrainersQuery).executeUpdate();
      con.createQuery(deleteMonstersQuery).executeUpdate();
    }
  }

}
