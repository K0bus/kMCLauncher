package kMCUpdaterLib;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class Version {
    private String id;
    private String type;
    private URL url;
    private String time;
    private String releaseTime;

    public Version(String gameVersion) throws JSONException, IOException
    {
        JSONObject manifest;
        JSONObject versionInfo;
        manifest = JSONReader.readJsonFromUrl(new URL("https://launchermeta.mojang.com/mc/game/version_manifest.json"));
        List<String> version = new ArrayList<String>();
        version.add(gameVersion);
        versionInfo = JSONReader.findArray(manifest.getJSONArray("versions"), "id", version);

        this.id = versionInfo.getString("id");
        this.type = versionInfo.getString("type");
        this.time = versionInfo.getString("time");
        this.releaseTime = versionInfo.getString("releaseTime");
        
        this.url = new URL(versionInfo.getString("url"));
    }
    public URL getUrl()
    {
        return this.url;
    }
    public String getTime()
    {
        return this.time;
    }
    public String getId()
    {
        return this.id;
    }
    public String getType()
    {
        return this.type;
    }
    public String releaseTime()
    {
        return this.releaseTime;
    }
}