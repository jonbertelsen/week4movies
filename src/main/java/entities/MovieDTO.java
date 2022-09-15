package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link Movie} entity
 */
public class MovieDTO implements Serializable
{
    private final Integer id;
    private final int year;
    private final String title;
    private final List<String> actors;

    public MovieDTO(Movie m)
    {
        this.id = m.getId();
        this.year = m.getYear();
        this.title = m.getTitle();
        this.actors = m.getActors();
    }

    public MovieDTO(Integer id, int year, String title, List<String> actors)
    {
        this.id = id;
        this.year = year;
        this.title = title;
        this.actors = actors;
    }

    public MovieDTO(int year, String title, List<String> actors)
    {
        this.id = 0;
        this.year = year;
        this.title = title;
        this.actors = actors;
    }

    public Integer getId()
    {
        return id;
    }

    public int getYear()
    {
        return year;
    }

    public String getTitle()
    {
        return title;
    }

    public List<String> getActors()
    {
        return actors;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieDTO entity = (MovieDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.year, entity.year) &&
                Objects.equals(this.title, entity.title) &&
                Objects.equals(this.actors, entity.actors);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, year, title, actors);
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "year = " + year + ", " +
                "title = " + title + ", " +
                "actors = " + actors + ")";
    }

    public static List<MovieDTO> getDTOs(List<Movie> movies)
    {
        List<MovieDTO> movieDTOList = new ArrayList<>();
        movies.forEach(m -> movieDTOList.add(new MovieDTO(m)));
        return movieDTOList;
    }
}