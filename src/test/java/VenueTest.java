import org.junit.*;
import org.sql2o.*;
import static org.junit.Assert.*;
import java.sql.Timestamp;
import java.util.Date;



public class VenueTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();


  @Test
  public void Venue_instantiatesCorrectly_true(){
    Venue myVenue = new Venue("the Moon");
    assertTrue(myVenue instanceof Venue);
  }

  @Test
  public void getCreatedAtAndgetUpdatedAt_returnTimestamps_hours(){
    Venue myVenue = new Venue("the Moon");
    Timestamp testCreatedAt = new Timestamp(new Date().getTime());
    assertEquals(myVenue.getCreatedAt().getHours(), testCreatedAt.getHours());
    assertEquals(myVenue.getUpdatedAt().getHours(), testCreatedAt.getHours());
  }


  @Test
  public void equals_returnsTrueIfDescriptionsAretheSame() {
    Venue firstVenue = new Venue("the Moon");
    Venue secondVenue = new Venue("the Moon");
    assertTrue(firstVenue.equals(secondVenue));
  }

  @Test
  public void save_returnsTrueIfNamesAretheSame_Venue() {
    Venue testVenue = new Venue("the Moon");
    testVenue.save();
    assertTrue(Venue.all().get(0).equals(testVenue));
  }

  @Test
  public void find_returnsCorrectVenueSearchedFor_Venue() {
    Venue testVenue = new Venue("the Moon");
    testVenue.save();
    assertEquals(Venue.find(testVenue.getId()), testVenue);
  }

  @Test
  public void thisVenuesBands_returnsAllBands_List() {
    Band myBand = new Band("Household chores");
    myBand.save();
    Venue myVenue = new Venue("Mow the lawn");
    myVenue.save();
    myVenue.addBand(myBand);
    assertEquals(1, myVenue.thisVenuesBands().size());
  }

  @Test
  public void remove_removesCorrectVenueAndJoinTableRows(){
    Band myBand = new Band("Household chores");
    myBand.save();
    Venue testVenue = new Venue("the Moon");
    testVenue.save();
    testVenue.addBand(myBand);

    testVenue.remove();
    assertEquals(0, testVenue.thisVenuesBands().size());
  }
}
