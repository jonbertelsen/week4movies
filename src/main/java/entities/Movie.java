package entities;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQuery(name = "Movie.deleteAllRows", query = "DELETE from Movie")
@Table(name = "movie")
public class Movie
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private int year;
    private String title;
    @ElementCollection
    private List<String> actors;

    public Movie()
    {
    }

    public Movie(int year, String title, List<String> actors)
    {
        this.year = year;
        this.title = title;
        this.actors = actors;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
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
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return Objects.equals(getId(), movie.getId());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getId());
    }

    @Override
    public String toString()
    {
        return "Movie{" +
                "id=" + id +
                ", year=" + year +
                ", title='" + title + '\'' +
                ", actors=" + actors +
                '}';
    }
}