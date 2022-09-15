package facades;

import edu.emory.mathcs.backport.java.util.Arrays;
import entities.Movie;
import entities.MovieDTO;
import entities.RenameMe;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.WebApplicationException;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

class MovieFacadeTest
{
    private static EntityManagerFactory emf;
    private static MovieFacade facade;
    private Movie m1, m2;

    @BeforeAll
    public static void setUpClass()
    {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = MovieFacade.getInstance(emf);
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
    void getAll()
    {
        List<MovieDTO> movieDTOList = facade.getAll();
        int expected = 2;
        int actual = movieDTOList.size();
        assertEquals(expected, actual);
        assertThat(movieDTOList, containsInAnyOrder(new MovieDTO(m1), new MovieDTO(m2)));
    }

    @Test
    void countAll()
    {
        long expected = 2;
        long actual = facade.countAll();
        assertEquals(expected, actual);
    }

    @Test
    void getMovieById()
    {
        MovieDTO actual = facade.getMovieById(m1.getId());
        MovieDTO expected = new MovieDTO(m1);
        assertEquals(expected, actual);
    }

    @Test
    void getMovieByIdException()
    {
        assertThrows(WebApplicationException.class, () -> {
            MovieDTO actual = facade.getMovieById(34);
        });

    }

    @Test
    void getMovieByTitle()
    {
        MovieDTO actual = facade.getMovieByTitle(m1.getTitle());
        MovieDTO expected = new MovieDTO(m1);
        assertEquals(expected, actual);
    }
}