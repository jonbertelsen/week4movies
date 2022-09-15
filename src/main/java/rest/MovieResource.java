package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.MovieDTO;
import facades.FacadeExample;
import facades.MovieFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/movie")
public class MovieResource
{
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final MovieFacade FACADE =  MovieFacade.getInstance(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces("text/plain")
    public String hello()
    {
        return "Hello, Movie World!";
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAll()
    {
        List<MovieDTO> movieDTOList = FACADE.getAll();
        return GSON.toJson(movieDTOList);
    }

    @GET
    @Path("populate")
    @Produces(MediaType.APPLICATION_JSON)
    public String populate()
    {
        FACADE.populate();
        return "\"message\":\"DB populated with data\"";
    }

    @GET
    @Path("count")
    @Produces({MediaType.APPLICATION_JSON})
    public String getCount() {
        long count = FACADE.countAll();
        return "{\"count\":"+count+"}";
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public String getById(@PathParam("id") int id)
    {
        MovieDTO movieDTO = FACADE.getMovieById(id);
        return GSON.toJson(movieDTO);
    }

    @GET
    @Path("title/{name}")
    @Produces({MediaType.APPLICATION_JSON})
    public String getByTitle(@PathParam("name") String name)
    {
        MovieDTO movieDTO = FACADE.getMovieByTitle(name);
        return GSON.toJson(movieDTO);
    }



}