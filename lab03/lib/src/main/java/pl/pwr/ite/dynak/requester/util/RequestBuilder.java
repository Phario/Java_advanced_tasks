package pl.pwr.ite.dynak.requester.util;

import pl.pwr.ite.dynak.requester.enums.RequestType;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class RequestBuilder {
    // Musiałem zmienić bo swapi.dev padło po 20/04
    private static final String BASE_URL = "https://swapi.info/api/";
    protected static final int MAX_ID = 15;

    public static URL buildInfoURL(int id, RequestType type) {
        if (id <= 0 || id > MAX_ID) id = 1;
        if (type == null) throw new IllegalArgumentException("RequestType cannot be null");

        URI uri = URI.create(BASE_URL + type.toString().toLowerCase() + "/" + id);
        try {
            return uri.toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}
