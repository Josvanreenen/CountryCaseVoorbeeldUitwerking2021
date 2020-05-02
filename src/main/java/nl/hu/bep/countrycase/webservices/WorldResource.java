package nl.hu.bep.countrycase.webservices;

import nl.hu.bep.countrycase.model.Country;
import nl.hu.bep.countrycase.model.World;

import javax.json.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

@Path("/countries")
public class WorldResource {
    private World world = World.getWorld();

    @GET
    @Produces("application/json")
    public String getCountries() {
        JsonArray countryArray = buildJsonCountryArray(world.getAllCountries());
        return countryArray.toString();
    }

    @GET
    @Path("/{code}")
    @Produces("application/json")
    public String getCountry(@PathParam("code") String countryCode) {
        return buildJsonCountryObject(world.getCountryByCode(countryCode)).toString();
    }

    @GET
    @Path("/largestpopulations")
    @Produces("application/json")
    public String get10LargestPopulations() {
        List<Country> countries = world.get10LargestPopulations();
        JsonArray countryArray = buildJsonCountryArray(countries);

        return countryArray.toString();
    }

    @GET
    @Path("/largestsurfaces")
    @Produces("application/json")
    public String getLargestSurfaces() {
        List<Country> countries = world.get10LargestSurfaces();
        JsonArray countryArray = buildJsonCountryArray(countries);

        return countryArray.toString();
    }

    private JsonArray buildJsonCountryArray(List<Country> countries) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        for (Country c : countries) {
            jsonArrayBuilder.add(buildJsonCountryObject(c));
        }

        return jsonArrayBuilder.build();
    }

    private JsonObject buildJsonCountryObject(Country c) {
        JsonObjectBuilder job = Json.createObjectBuilder();

        job.add("code", c.getCode());
        job.add("iso3", c.getIso3());
        job.add("name", c.getName());
        job.add("continent", c.getContinent());
        job.add("capital", c.getCapital());
        job.add("region", c.getRegion());
        job.add("surface", c.getSurface());
        job.add("population", c.getPopulation());
        job.add("government", c.getGovernment());
        job.add("lat", c.getLatitude());
        job.add("lng", c.getLongitude());

        return job.build();
    }
}
