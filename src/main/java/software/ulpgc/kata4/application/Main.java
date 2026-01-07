package software.ulpgc.kata4.application;

import software.ulpgc.kata4.architecture.model.Movie;
import software.ulpgc.kata4.architecture.viewmodel.Histogram;
import software.ulpgc.kata4.architecture.viewmodel.HistogramBuilder;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Movie> movies = new RemoteMovieLoader(TsvMovieParser::from).loadAll();
        Histogram histogram = new HistogramBuilder(m -> (m.year() / 10) * 10).buildWith(movies);
        for (int bin: histogram) {
            System.out.println(bin + ": " + histogram.count(bin));
        }
    }
}