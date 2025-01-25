package br.com.brainyit.util;

import java.util.Map;

import com.google.gson.Gson;

public class TestUtils {

    public static final String CUSTOMER_ID = "5";

    public static final String SUBSCRIBER = "1";

    private static final Gson GSON = new Gson();

    private TestUtils() {
        throw new UnsupportedOperationException("This is a utility class and should not be instantiated");
    }


    @SuppressWarnings("unchecked")
    public static Map<String, Object> convertResponseToMap(final String response) {
        return GSON.fromJson(response, Map.class);
    }

}
