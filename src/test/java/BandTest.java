import org.junit.*;
import org.sql2o.*;
import static org.junit.Assert.*;
import java.sql.Timestamp;
import java.util.Date;



public class BandTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void Band_instantiatesCorrectly_true(){
    Band myBand = new Band("sakdjas");
    assertTrue(myBand instanceof Band);
  }

  @Test
  public void getCreatedAtAndgetUpdatedAt_returnTimestamps_hours(){
    Band myBand = new Band("dave");
    Timestamp testCreatedAt = new Timestamp(new Date().getTime());
    assertEquals(myBand.getCreatedAt().getHours(), testCreatedAt.getHours());
    assertEquals(myBand.getUpdatedAt().getHours(), testCreatedAt.getHours());
  }


  @Test
  public void equals_returnsTrueIfDescriptionsAretheSame() {
    Band firstBand = new Band("dave");
    Band secondBand = new Band("dave");
    assertTrue(firstBand.equals(secondBand));
  }

  @Test
  public void save_returnsTrueIfNamesAretheSame_Band() {
    Band testBand = new Band("dave");
    testBand.save();
    assertTrue(Band.all().get(0).equals(testBand));
  }

  @Test
  public void find_returnsCorrectBandSearchedFor_Band() {
    Band testBand = new Band("dave");
    testBand.save();
    assertEquals(Band.find(testBand.getId()), testBand);
  }

  @Test
  public void remove_removesCorrectVenuesAndJoinTableRows(){
    Band testBand = new Band("dave");
    testBand.save();
    Venue testVenue = new Venue("flint");
    testVenue.save();

    testBand.addVenue(testVenue);


    testBand.remove();
    assertEquals(0, testVenue.thisVenuesBands().size());
  }

  @Test
  public void getVenue_returnsAllVenue_List() {
    Band myBand = new Band("Household chores");
    myBand.save();
    Venue myVenue = new Venue("Mow the lawn");
    myVenue.save();
    myVenue.addBand(myBand);
    assertEquals(1, myBand.thisBandsVenues().size());
  }
}
