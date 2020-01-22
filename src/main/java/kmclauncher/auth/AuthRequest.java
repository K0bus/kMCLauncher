package kmclauncher.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import kmclauncher.utils.Logger;

public class AuthRequest {

    private String url;
    private String path;
    private JSONObject object;

    public AuthRequest(AuthType type, JSONObject object) {
        this.url = "https://authserver.mojang.com/";
        this.path = type.toString();
        this.object = object;
    }

    public JSONObject send() {
        try {
            URL url = new URL(this.url + this.path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            String data = object.toString();
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = data.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    if(response.toString().length()>0)
                    {
                        return new JSONObject(response.toString());
                    }
                    else
                    {
                        return new JSONObject().put("good", true);
                    }
                    
            }
        } catch (IOException e) {
            Logger.error("Invalid credential !");
            e.printStackTrace();
        }


        return null;
    }
}