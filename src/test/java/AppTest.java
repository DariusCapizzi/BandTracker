
import org.sql2o.*;
import org.fluentlenium.adapter.FluentTest;
import static org.fluentlenium.core.filter.FilterConstructor.*;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import org.junit.*;
import static org.junit.Assert.*;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  @Override
  public WebDriver getDefaultDriver() {
    return webDriver;
  }

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @ClassRule
  public static ServerRule server = new ServerRule();

  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("add a new band:");
  }

  @Test
  public void bandsAreListedTest() {
    Band testBand = new Band("dave");
    testBand.save();
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("dave");
  }

  @Test
  public void createBand() {
    goTo("http://localhost:4567/");
    fill("#band-name").with("dave");
    submit("#band-btn");
    assertThat(pageSource()).contains("dave");
  }

  @Test
  public void createVenue() {
    goTo("http://localhost:4567/");
    fill("#new-venue-name").with("flint");
    submit("#new-venue-btn");
    assertThat(pageSource()).contains("flint");
  }

  @Test
  public void deleteBand() {
    Band testBand = new Band("dave");
    testBand.save();

    goTo("http://localhost:4567/");
    submit("#remove-band");
    assertThat(pageSource()).doesNotContain("dave");
  }

  @Test
  public void deleteVenue() {
    Band testBand = new Band("dave");
    testBand.save();
    Venue myVenue = new Venue("flint");
    myVenue.save();

    goTo("http://localhost:4567/");
    submit("#remove-venue");
    assertThat(pageSource()).doesNotContain("flint");
  }

  @Test
  public void seeThisBand() {
    Band thisBand = new Band("daave");
    thisBand.save();

    goTo("http://localhost:4567/");
    click("a", withText("daave"));
    assertThat(pageSource()).contains("daave");
  }

  @Test
  public void seeThisVenue() {
    Venue myVenue = new Venue("flint");
    myVenue.save();

    goTo("http://localhost:4567/");
    click("a", withText("flint"));
    assertThat(pageSource()).contains("flint");
  }

  @Test
  public void updateBandName() {
    Band testBand = new Band("dave");
    testBand.save();

    goTo("http://localhost:4567/bands/"+testBand.getId());
    fill("#change_band_name").with("carl");
    submit("#changeBandName");
    assertThat(pageSource()).contains("carl");
  }

  @Test
  public void updateBandsAddVenue() {
    Band testBand = new Band("dave");
    Venue myVenue = new Venue("carl");
    testBand.save();
    myVenue.save();
    goTo("http://localhost:4567/bands/"+testBand.getId());
    fill("#carl");
    submit("#add_venue_button");
    assertThat(pageSource()).contains("carl");
  }

  @Test
  public void updateVenueName() {
    Band testBand = new Band("dave");
    testBand.save();
    Venue myVenue = new Venue("flint");
    myVenue.save();

    goTo("http://localhost:4567/venues/"+myVenue.getId());
    fill("#change_venue_name").with("clint");
    submit("#changeVenueName");
    assertThat(pageSource()).contains("clint");
  }


  @Test
  public void updateVenueAddBand() {
    Band testBand = new Band("dave");
    testBand.save();
    Venue myVenue = new Venue("flint");
    myVenue.save();

    goTo("http://localhost:4567/venues/"+myVenue.getId());
    fill("#dave");
    submit("#add_band_button");
    assertThat(pageSource()).contains("dave");
  }



}
