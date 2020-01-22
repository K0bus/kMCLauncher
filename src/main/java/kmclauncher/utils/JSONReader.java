package kmclauncher.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONReader {
    private static String readAll(Reader rd) {
        StringBuilder sb = new StringBuilder();
        int cp;
        try {
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
        } catch (IOException e) {
            Logger.warn("Can't read data");
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(URL url) throws IOException, JSONException {
        InputStream is = url.openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public static JSONObject readJsonFromFile(File file) {
        BufferedReader rd;
        try {
            rd = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            Logger.warn("File not found : " + file.getName() + " in " + file.getParentFile().getPath());
            return null;
        }
        String jsonText = readAll(rd);
        JSONObject json = new JSONObject(jsonText);
        return json;
    }
    public static JSONObject findArray(JSONArray obj, String id, List<String> values)
    {
        int length = obj.length();
        int i = 0;
        while(i<=length) {
            if (obj.get(i) instanceof JSONObject) {
                JSONObject element = obj.getJSONObject(i);
                if(values.contains(element.getString(id)))
                {
                    return element;
                }
            }
            i++;
        }
        return null;
    }
}