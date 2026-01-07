package software.ulpgc.kata4.architecture.io;

import software.ulpgc.kata4.architecture.model.Movie;

import java.util.List;

public interface MovieLoader {
    List<Movie> loadAll();
}