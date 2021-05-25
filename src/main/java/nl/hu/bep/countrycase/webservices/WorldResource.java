package nl.hu.bep.countrycase.webservices;

import nl.hu.bep.countrycase.model.Country;
import nl.hu.bep.countrycase.model.World;

import javax.annotation.security.RolesAllowed;
import javax.json.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.util.AbstractMap;
import java.util.List;

@Path("/countries")
public class WorldResource {
    public static final String MESSAGE = "message";
    private World world = World.getWorld();

    @GET
    @RolesAllowed("admin")
    @Produces("application/json")
    public Response getCountries() {
        List<Country> allCountries = world.getAllCountries();
        return !allCountries.isEmpty() ? Response.ok(allCountries).build() : Response.status(Response.Status.NO_CONTENT).entity(new AbstractMap.SimpleEntry<>(MESSAGE, "There were no countries")).build();
    }

    @GET
    @Path("/{code}")
    @Produces("application/json")
    public Response getCountry(@PathParam("code") String countryCode) {
        if (countryCode.isBlank() || !countryCode.toUpperCase().matches("[A-Z][A-Z]]"))
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new AbstractMap.SimpleEntry<>("error", "you should specify a valid code"))
                    .build();
        var country = world.getCountryByCode(countryCode);
        if (country == null) return Response.status(Response.Status.NOT_FOUND)
                .entity(new AbstractMap.SimpleEntry<>("error", "No country found by that code"))
                .build();
        return Response.ok(country).build();
    }

    @GET
    @Path("/errorme/{vraag: [0-9]}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response stommeVraag(@PathParam("vraag") int vraag){
        return Response.ok().build();
    }

    @GET
    @Path("/errorme/{vraag}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response stommeVraag2(@PathParam("vraag") String vraag){
        return Response.noContent().build();
    }

    @GET
    @Path("/largestpopulations")
    @Produces("application/json")
    public Response get10LargestPopulations() {
        List<Country> allCountries = world.get10LargestPopulations();
        return !allCountries.isEmpty() ? Response.ok(allCountries).build() : Response.status(Response.Status.NO_CONTENT).entity(new AbstractMap.SimpleEntry<>(MESSAGE, "There were no countries")).build();
    }

    @GET
    @Path("/largestsurfaces")
    @Produces("application/json")
    public Response getLargestSurfaces() {
        List<Country> allCountries = world.get10LargestSurfaces();
        return !allCountries.isEmpty() ? Response.ok(allCountries).build() : Response.status(Response.Status.NO_CONTENT).entity(new AbstractMap.SimpleEntry<>(MESSAGE, "There were no countries")).build();
    }

    @POST
    @Produces("application/json")
    public Response addCountry(String jsonBody) {
        Response.ResponseBuilder response = null;
        StringReader strReader = new StringReader(jsonBody);
        JsonReader jsonReader = Json.createReader(strReader);

        try {
            JsonStructure jsonValue = jsonReader.read();

            if (jsonValue.getValueType() == JsonValue.ValueType.OBJECT) {
                JsonObject country = (JsonObject) jsonValue;

                String code = country.getString("code");
                String iso3 = country.getString("iso3");
                String nm = country.getString("name");
                String cap = country.getString("capital");
                String ct = country.getString("continent");
                String reg = country.getString("region");
                double sur = Double.parseDouble(country.getString("surface"));
                int pop = Integer.parseInt(country.getString("population"));
                String gov = country.getString("government");
                double lat = Double.parseDouble(country.getString("latitude"));
                double lng = Double.parseDouble(country.getString("longitude"));

                World.getWorld().addCountry(code, iso3, nm, cap, ct, reg, sur, pop,
                        gov, lat, lng);
                response = Response.ok(new AbstractMap.SimpleEntry<>(MESSAGE, "country added"));
            } else {
                response = Response.status(Response.Status.BAD_REQUEST).entity(new AbstractMap.SimpleEntry<>(MESSAGE, "expected a JsonObject"));
            }
        } catch (Exception e) {
            response = Response.status(Response.Status.CONFLICT).entity(new AbstractMap.SimpleEntry<>(MESSAGE, "Error: "+ e.getMessage()));
        } finally {
            jsonReader.close();
        }

        return response.build();
    }

}
