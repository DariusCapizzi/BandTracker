import java.util.Map;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    //root
    get("/", (req, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      if(Band.all().size() > 0){
        model.put("bands", Band.all());
      }
      if(Venue.all().size() > 0){
        model.put("venues", Venue.all());
      }
      model.put("template", "templates/home.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //CREATE
    post("/band/new", (req, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String inBandName = req.queryParams("band-name");
      Band newBand = new Band(inBandName);
      newBand.save();
      response.redirect("/");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/venue/new", (req, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String inVenueName = req.queryParams("new-venue-name");
      Venue newVenue = new Venue(inVenueName);
      newVenue.save();
      response.redirect("/");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


    //READ
    get("/bands/:band_id", (req, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      if(Venue.all().size() > 0){
        model.put("venues", Venue.all());
      }
      Band thisBand = Band.find(Integer.parseInt(req.params(":band_id")));
      model.put("band", thisBand);
      model.put("template", "templates/band.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


    get("/venues/:venue_id", (req, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      if(Band.all().size() > 0){
        model.put("bands", Band.all());
      }
      Venue thisVenue = Venue.find(Integer.parseInt(req.params(":venue_id")));
      model.put("venue",thisVenue);
      model.put("template", "templates/venue.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


    //UPDATE
    post("/bands/:band_id/edit", (req, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Band thisBand = Band.find(Integer.parseInt(req.params(":band_id")));
      if(req.queryParams().contains("change_band_name")){
        String newValue = req.queryParams("change_band_name");
        thisBand.updateName(newValue);
      }
      if(req.queryParams().contains("add_venue")){
        String[] sArray = req.queryParamsValues("add_venue");
        for(int i = 0; i < sArray.length; i++){
          //if the venue isnt already added
          if (thisBand.thisBandsVenues().contains(Venue.find(Integer.parseInt(sArray[i])))){
            continue;
          } else {
            thisBand.addVenue(Venue.find(Integer.parseInt(sArray[i])));
          }
        }
      }
      response.redirect("/bands/"+ req.params(":band_id"));
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

  //   post("/venues/:venue_id/edit", (req, response) -> {
  //     Map<String, Object> model = new HashMap<String, Object>();
  //     Venue thisVenue = Venue.find(Integer.parseInt(req.params(":venue_id")));
  //
  //     // // someday this might work?
  //     // for (int i : req.queryParams()){
  //     //   if( req.queryParams(i) != void ){
  //     //     thisVenue.updateAll( req.queryParams(), req.queryParams(i));
  //     //   }
  //     // }
  //
  //     String newValue = req.queryParams("change_venue_name");
  //
  //     thisVenue.updateName(newValue);
  //     response.redirect("/venues/" + req.params(":venue_id"));
  //     return new ModelAndView(model, layout);
  //   }, new VelocityTemplateEngine());
  //
  //
    //DELETE
    post("/bands/:band_id/remove", (req, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Band thisBand = Band.find(Integer.parseInt(req.params(":band_id")));
      thisBand.remove();
      response.redirect("/");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/venues/:venue_id/remove", (req, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Venue thisVenue = Venue.find(Integer.parseInt(req.params(":venue_id")));
      thisVenue.remove();
      response.redirect("/");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

  }
}
