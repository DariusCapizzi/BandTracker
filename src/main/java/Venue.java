import org.sql2o.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Venue {

  private int id;
  private String venue_name;
  private Timestamp created_at;
  private Timestamp updated_at;

  public Venue(String venue_name) {
    this.venue_name = venue_name;
    created_at = new Timestamp(new Date().getTime());
    updated_at = new Timestamp(new Date().getTime());
  }

  //create
  public void save(){
  try(Connection con = DB.sql2o.open()) {
    String sql = "INSERT INTO venues ( venue_name, created_at, updated_at) VALUES ( :venue_name, :created_at, :updated_at)";
    this.id = (int) con.createQuery(sql, true)
      .addParameter("venue_name", venue_name)
      .addParameter("created_at", created_at)
      .addParameter("updated_at", updated_at)
      .executeUpdate()
      .getKey();
    }
  }

  public void addBand(Band band) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO bands_venues (band_id, venue_id) VALUES (:band_id, :venue_id)";
      con.createQuery(sql)
        .addParameter("band_id", band.getId())
        .addParameter("venue_id", this.getId())
        .executeUpdate();
    }
  }

  //read
  public static List<Venue> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM venues";
      return con.createQuery(sql).executeAndFetch(Venue.class);
    }
  }

  public static Venue find(int id){
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM venues WHERE id=:id";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Venue.class);
    }
  }

  public List<Band> thisVenuesBands(){
    try(Connection con = DB.sql2o.open()) {
      String getIdsFromJoinTableQuery = "SELECT band_id FROM bands_venues WHERE venue_id=:id";
      List<Integer> bandIds = con.createQuery(getIdsFromJoinTableQuery)
        .addParameter("id", this.id)
        .executeAndFetch(Integer.class);

      List<Band> bands = new ArrayList<Band>();
      for (Integer bandId: bandIds){
        String getBandFromJoinTableQuery =  "SELECT * FROM bands WHERE id=:band_id";
        Band band = con.createQuery(getBandFromJoinTableQuery)
          .addParameter("band_id", bandId)
          .executeAndFetchFirst(Band.class);
        bands.add(band);
      }

      return bands;
    }
  }

  //update
  public void updateName(String newValue){
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE venues SET venue_name = :newValue WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("newValue", newValue)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public void updateAll(String thingToUpdate, String newValue){
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE venues SET :thingToUpdate = :newValue WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("newValue", newValue)
        .addParameter("thingToUpdate", thingToUpdate)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }


  //delete
  public void remove(){
    try(Connection con = DB.sql2o.open()) {
      String deleteFromJoinTableQuery = "DELETE FROM bands_venues WHERE venue_id = :id";
      con.createQuery(deleteFromJoinTableQuery)
        .addParameter("id", this.id)
        .executeUpdate();

      String sql = "DELETE FROM venues WHERE id=:id";
      con.createQuery(sql)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }




  @Override
  public boolean equals(Object otherVenue) {
    if(!(otherVenue instanceof Venue)) {
      return false;
    } else {
      Venue newVenue = (Venue) otherVenue;
      return this.getVenueName().equals(newVenue.getVenueName()) &&
      this.getId() == newVenue.getId();
    }
  }

  public int getId(){
    return id;
  }

  public String getVenueName(){
    return venue_name;
  }

  public Timestamp getCreatedAt(){
    return created_at;
  }

  public Timestamp getUpdatedAt(){
    return updated_at;
  }


}
