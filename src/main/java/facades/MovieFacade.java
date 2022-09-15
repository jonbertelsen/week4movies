package facades;

import edu.emory.mathcs.backport.java.util.Arrays;
import entities.Movie;
import entities.MovieDTO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class MovieFacade
{
    private static MovieFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private MovieFacade() {}

    public static MovieFacade getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new MovieFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public List<MovieDTO> getAll()
    {
        EntityManager em = getEntityManager();
        try
        {
            TypedQuery<Movie> query = em.createQuery("select m from Movie m", Movie.class);
            List<Movie> movies = query.getResultList();
            return MovieDTO.getDTOs(movies);
        }
        finally
        {
            em.close();
        }
    }

    public long countAll()
    {
        EntityManager em = getEntityManager();
        long count;
        TypedQuery<Long> query = em.createQuery("select count(m) from Movie m", Long.class);
        count = query.getSingleResult();
        em.close();
        return count;
    }

    public void populate()
    {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
            em.persist(new Movie(1988,
                    "Die Hard I", Arrays.asList(new String[]{"Bruce Willis", "Alan Rickman", "Paul Gleeson"})));
            em.persist(new Movie(1982,
                "Rambo - First Blood", Arrays.asList(new String[]{"Sly Stallone", "Brian Dennehy", "Jack Starrett"})));
        em.getTransaction().commit();
        em.close();
    }
}
