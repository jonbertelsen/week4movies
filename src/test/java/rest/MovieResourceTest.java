package rest;

import edu.emory.mathcs.backport.java.util.Arrays;
import entities.Movie;
import entities.MovieDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

class MovieResourceTest
{
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api/";
    private static Movie m1, m2;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass()
    {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    @BeforeEach
    void setUp()
    {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Movie.deleteAllRows").executeUpdate();
            m1 = new Movie(1988,
                    "Die Hard I", Arrays.asList(new String[]{"Bruce Willis", "Alan Rickman", "Paul Gleeson"}));
            m2 = new Movie(1982,
                    "Rambo - First Blood", Arrays.asList(new String[]{"Sly Stallone", "Brian Dennehy", "Jack Starrett"}));
            em.persist(m1);
            em.persist(m2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void getAll()
    {
        List<MovieDTO> movieDTOs;

        movieDTOs = given()
                .contentType("application/json")
                .when()
                .get("/movie/all")
                .then()
                .extract().body().jsonPath().getList("", MovieDTO.class);

        MovieDTO m1DTO = new MovieDTO(m1);
        MovieDTO m2DTO = new MovieDTO(m2);
        assertThat(movieDTOs, containsInAnyOrder(m1DTO, m2DTO));
    }

    @Test
    void getCount()
    {
        given()
                .contentType(ContentType.JSON)
                .get("movie/count")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(2));
    }

    @Test
    void getById()
    {
        given()
            .contentType(ContentType.JSON)
            .get("movie/{id}", m1.getId())
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK_200.getStatusCode())
            .body("id", equalTo(m1.getId()))
            .body("title", equalTo(m1.getTitle()))
            .body("actors", hasItems("Bruce Willis","Alan Rickman","Paul Gleeson"));
    }

    @Test
    void getByTitle()
    {
        given()
                .contentType(ContentType.JSON)
                .get("movie/title/{title}", m1.getTitle())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", equalTo(m1.getId()))
                .body("title", equalTo(m1.getTitle()))
                .body("actors", hasItems("Bruce Willis","Alan Rickman","Paul Gleeson"));
    }
}