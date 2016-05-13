import java.util.Map;
import java.util.HashMap;
import java.lang.Boolean;
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
      if(req.session().attribute("isError") == null){
        req.session().attribute("isError", false);
      }
      if( (boolean) req.session().attribute("isError") == true){
        model.put("isError", (boolean)req.session().attribute("isError"));
        req.session().attribute("isError", false);
      }
      if(Band.all().size() > 0){
        model.put("bands", Band.all());
      }
      if(Venue.all().size() > 0){
        model.put("venues", Venue.all());
      }
      req.session().attribute("OldRoute", req.uri());
      req.session().attribute("OldTemplate", "templates/home.vtl");
      model.put("template", "templates/home.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //error
    get("/error", (req, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      req.session().attribute("isError", true);
      response.redirect(req.session().attribute("OldRoute"));
      return null;
    });

    //CREATE
    post("/band/new", (req, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String inBandName = req.queryParams("band-name").trim();
      if (inBandName.equals("")){
        response.redirect("/error");
      } else {
        Band newBand = new Band(inBandName);
        newBand.save();
        response.redirect(req.session().attribute("OldRoute"));
      }
      return null;
    });

    post("/venue/new", (req, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();

      String inVenueName = req.queryParams("new-venue-name").trim();
      if (inVenueName.equals("")){
        response.redirect("/error");
      } else {
        Venue newVenue = new Venue(inVenueName);
        newVenue.save();
        response.redirect(req.session().attribute("OldRoute"));
      }
      return null;
    });


    //READ
    get("/bands/:band_id", (req, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      if(req.session().attribute("isError") == null){
        req.session().attribute("isError", false);
      }
      if( (boolean) req.session().attribute("isError") == true){
        model.put("isError", (boolean)req.session().attribute("isError"));
        req.session().attribute("isError", false);
      }
      req.session().attribute("OldRoute", req.uri());
      req.session().attribute("OldTemplate", "templates/band.vtl");
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
      if(req.session().attribute("isError") == null){
        req.session().attribute("isError", false);
      }
      if( (boolean) req.session().attribute("isError") == true){
        model.put("isError", (boolean)req.session().attribute("isError"));
        req.session().attribute("isError", false);
      }
      req.session().attribute("OldRoute", req.uri());
      req.session().attribute("OldTemplate", "templates/venue.vtl");
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
        if (req.queryParams("change_band_name").trim().equals("")){
          response.redirect("/error");
        } else {
          String newValue = req.queryParams("change_band_name");
          thisBand.updateName(newValue);
        }
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
      response.redirect(req.session().attribute("OldRoute"));
      return null;
    });

    post("/venues/:venue_id/edit", (req, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Venue thisVenue = Venue.find(Integer.parseInt(req.params(":venue_id")));


      if(req.queryParams().contains("change_venue_name")){
        if (req.queryParams("change_venue_name").trim().equals("")){
          response.redirect("/error");
        } else {
          String newValue = req.queryParams("change_venue_name");
          thisVenue.updateName(newValue);
        }
      }

      if(req.queryParams().contains("add_band")){
        String[] sArray = req.queryParamsValues("add_band");
        for(int i = 0; i < sArray.length; i++){
          //if the venue isnt already added
          if (thisVenue.thisVenuesBands().contains(Band.find(Integer.parseInt(sArray[i])))){
            continue;
          } else {
            thisVenue.addBand(Band.find(Integer.parseInt(sArray[i])));
          }
        }
      }
      response.redirect(req.session().attribute("OldRoute"));
      return null;
    });


    //DELETE
    post("/bands/:band_id/remove", (req, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Band thisBand = Band.find(Integer.parseInt(req.params(":band_id")));
      thisBand.remove();
      response.redirect(req.session().attribute("OldRoute"));
      return null;
    });

    post("/venues/:venue_id/remove", (req, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Venue thisVenue = Venue.find(Integer.parseInt(req.params(":venue_id")));
      thisVenue.remove();
      response.redirect(req.session().attribute("OldRoute"));
      return null;
    });

  }
}
