
import org.junit.rules.ExternalResource;
import org.sql2o.*;

public class DatabaseRule extends ExternalResource {

  @Override
  public void before() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/band_tracker_test", null, null);
  }

  @Override
  public void after() {
    try(Connection con = DB.sql2o.open()) {
      String deleteVenuesQuery = "DELETE FROM venues *;";
      String deleteBandsQuery = "DELETE FROM bands *;";
      con.createQuery(deleteVenuesQuery).executeUpdate();
      con.createQuery(deleteBandsQuery).executeUpdate();
    }
  }
}
