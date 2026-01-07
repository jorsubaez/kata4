package software.ulpgc.kata4.application;

import software.ulpgc.kata4.architecture.io.Store;
import software.ulpgc.kata4.architecture.model.Movie;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

public class RemoteStore implements Store {
    private static final String RemoteUrl = "https://datasets.imdbws.com/title.basics.tsv.gz";
    private static final int BufferSize = 65536;
    private final Function<String, Movie> deserialize;

    public RemoteStore(Function<String, Movie> deserialize) {
        this.deserialize = deserialize;
    }

    @Override
    public Stream<Movie> movies() {
        try {
            return loadAllFrom(new URL(RemoteUrl));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<Movie> loadAllFrom(URL url) throws IOException {
        return loadAllFrom(url.openConnection());
    }

    private Stream<Movie> loadAllFrom(URLConnection connection) throws IOException {
        try (InputStream is = new GZIPInputStream(new BufferedInputStream(connection.getInputStream(), BufferSize))) {
            return loadAllFrom(is);
        }
    }

    private Stream<Movie> loadAllFrom(InputStream inputStream) throws IOException {
        return loadFrom(new BufferedReader(new InputStreamReader(inputStream)));
    }

    private Stream<Movie> loadFrom(BufferedReader reader) throws IOException {
        return reader.lines().skip(1).map(deserialize);
    }
}