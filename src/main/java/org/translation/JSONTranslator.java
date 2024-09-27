package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    private final Map<String, JSONObject> data = new HashMap<String, JSONObject>();

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                data.put(jsonObject.getString("alpha3"), jsonObject);
                jsonObject.remove("alpha3");
                jsonObject.remove("alpha2");
                jsonObject.remove("id");


            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        for (Map.Entry<String, JSONObject> entry : data.entrySet()) {
            if (entry.getKey().equals(country)) {
                return new ArrayList<>(entry.getValue().keySet());
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getCountries() {
        for (Map.Entry<String, JSONObject> entry : data.entrySet()) {
            return new ArrayList<>(data.keySet());
        }
        return new ArrayList<>();
    }

    @Override
    public String translate(String country, String language) {
        for (Map.Entry<String, JSONObject> entry : data.entrySet()) {
            if (entry.getKey().equals(country)) {
                JSONObject jsonObject = entry.getValue();
                return jsonObject.getString(language);
            }
        }
        return null;
    }
}
