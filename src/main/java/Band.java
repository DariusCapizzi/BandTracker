import org.sql2o.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;


public class Band {

  private int id;
  private String band_name;
  private Timestamp created_at;
  private Timestamp updated_at;

  public Band(String band_name) {
    this.band_name = band_name;
    created_at = new Timestamp(new Date().getTime());
    updated_at = new Timestamp(new Date().getTime());
  }

  //create
  public void save(){
  try(Connection con = DB.sql2o.open()) {
    String sql = "INSERT INTO bands ( band_name, created_at, updated_at) VALUES ( :band_name, :created_at, :updated_at)";
    this.id = (int) con.createQuery(sql, true)
      .addParameter("band_name", band_name)
      .addParameter("created_at", created_at)
      .addParameter("updated_at", updated_at)
      .executeUpdate()
      .getKey();
    }
  }

  public void addVenue(Venue venue) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO bands_venues (band_id, venue_id) VALUES (:band_id, :venue_id)";
      con.createQuery(sql)
        .addParameter("band_id", this.getId())
        .addParameter("venue_id", venue.getId())
        .executeUpdate();
    }
  }

  //read
  public static List<Band> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM bands";
     return con.createQuery(sql).executeAndFetch(Band.class);
    }
  }

  public static Band find(int id){
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM bands WHERE id=:id";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Band.class);
    }
  }

  public List<Venue> thisBandsVenues(){
    try(Connection con = DB.sql2o.open()) {
      String getIdsFromJoinTableQuery = "SELECT venue_id FROM bands_venues WHERE band_id = :id";
      List<Integer> venueIds = con.createQuery(getIdsFromJoinTableQuery)
       .addParameter("id", this.id)
       .executeAndFetch(Integer.class);

      List<Venue> venues = new ArrayList<Venue>();
      for (Integer venueId : venueIds){
        String getVenueFromIdsQuery = "SELECT * FROM venues WHERE id = :id";
        Venue venue = con.createQuery(getVenueFromIdsQuery)
         .addParameter("id", venueId)
         .executeAndFetchFirst(Venue.class);
         venues.add(venue);
      }
      return venues;
    }
  }

  //update
  public void updateName(String newValue){
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE bands SET band_name = :newValue WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("newValue", newValue)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public void updateFull(String thingToUpdate, String newValue){
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE bands SET :thingToUpdate = :newValue WHERE id = :id;";
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
      String deleteFromJoinTableQuery = "DELETE FROM bands_venues WHERE band_id = :id";
      con.createQuery(deleteFromJoinTableQuery)
        .addParameter("id", this.id)
        .executeUpdate();

      String deleteFromBandTableQuery = "DELETE FROM bands WHERE id=:id";
      con.createQuery(deleteFromBandTableQuery)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }


  @Override
  public boolean equals(Object otherBand) {
    if(!(otherBand instanceof Band)) {
      return false;
    } else {
      Band newBand = (Band) otherBand;
      return this.getId() == newBand.getId()
      && this.getBandName().equals(newBand.getBandName());
    }
  }

  public int getId(){
    return id;
  }

  public String getBandName(){
    return band_name;
  }

  public Timestamp getCreatedAt(){
    return created_at;
  }

  public Timestamp getUpdatedAt(){
    return updated_at;
  }
}
