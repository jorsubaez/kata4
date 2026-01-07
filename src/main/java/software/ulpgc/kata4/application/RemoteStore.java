package software.ulpgc.kata4.application;

import software.ulpgc.kata4.architecture.io.Store;
import software.ulpgc.kata4.architecture.model.Movie;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.zip.GZIPInputStream;

public class RemoteStore implements Store {
    private static final String RemoteUrl = "https://datasets.imdbws.com/title.basics.tsv.gz";
    private static final int BufferSize = 65536;
    private final Function<String, Movie> deserialize;

    public RemoteStore(Function<String, Movie> deserialize) {
        this.deserialize = deserialize;
    }

    @Override
    public List<Movie> movies() {
        try {
            return loadAllFrom(new URL(RemoteUrl));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Movie> loadAllFrom(URL url) throws IOException {
        return loadAllFrom(url.openConnection());
    }

    private List<Movie> loadAllFrom(URLConnection connection) throws IOException {
        try (InputStream is = new GZIPInputStream(new BufferedInputStream(connection.getInputStream(), BufferSize))) {
            return loadAllFrom(is);
        }
    }

    private List<Movie> loadAllFrom(InputStream inputStream) throws IOException {
        return loadFrom(new BufferedReader(new InputStreamReader(inputStream)));
    }

    private List<Movie> loadFrom(BufferedReader reader) throws IOException {
        List<Movie> list = new ArrayList<>();
        reader.readLine();
        while (true) {
            String line = reader.readLine();
            if (line == null) break;
            list.add(deserialize.apply(line));
        }
        return list;
    }
}