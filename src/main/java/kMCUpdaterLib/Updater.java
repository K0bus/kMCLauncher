package kMCUpdaterLib;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import org.apache.commons.lang3.SystemUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Updater {

    private URL downloadServer;
    private File gameDir;
    private Version version;

    public Updater(URL downloadServer, File gameDir, String gameVersion) {
        this.downloadServer = downloadServer;
        this.gameDir = gameDir;
        try {
            this.version = new Version(gameVersion);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        this.configDir();
    }

    private void configDir() {
        Logger.info("Configure game path");
        if (!gameDir.exists()) {
            Logger.addDir(gameDir.getAbsolutePath());
            gameDir.mkdir();
            return;
        }
        Logger.info("Game path configured");
    }

    public boolean updateServer() {
        try {
            JSONObject manifest = JSONReader.readJsonFromUrl(new URL(this.downloadServer + "content.json"));
            for (Object o : manifest.getJSONArray("downloads")) {
                if(o instanceof JSONObject)
                {
                    JSONObject element = (JSONObject)o;
                    File file = new File(this.gameDir + File.separator + element.getString("path"));
                    boolean toDownload = true;
                    if(file.exists())
                    {
                        toDownload = !Hash.checkFile(file, element.getString("sha1"));
                    }
                    if(toDownload)
                    {
                        file.getParentFile().mkdirs();
                        Downloader.download(file, element.getString("url"), true);
                    }
                }
                else
                {
                    return false;
                }
            }
        } catch (JSONException | IOException e) {
            Logger.error("Unable to access content.json on update server");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateMojang() {
        try {
            JSONObject versionManifest = JSONReader.readJsonFromUrl(version.getUrl());
            // Create libs dir
            File libs = new File(this.gameDir, "libs");
            if (!libs.exists()) {
                libs.mkdir();
                Logger.addDir("libs");
            }
            Logger.info("Libs directory configured");
            // Fetching minecraft.jar
            if (!this.updateMinecraftJar(libs, versionManifest.getJSONObject("downloads").getJSONObject("client"))) {
                Logger.error("Error while updating minecraft.jar");
                return false;
            }
            if(!this.updateLibs(libs, versionManifest.getJSONArray("libraries"))){
                Logger.error("Error while updating libraries");
                return false;
            }
            // Create assets dir
            File assetsDir = new File(this.gameDir, "assets" + File.separator + "objects");
            if (!assetsDir.exists()) {
                assetsDir.mkdirs();
                Logger.addDir("assets/objects");
            }
            File assetsIndexesDir = new File(this.gameDir, "assets" + File.separator + "indexes");
            if (!assetsIndexesDir.exists()) {
                assetsIndexesDir.mkdirs();
                Logger.addDir("assets/indexes");
            }
            if(!Hash.checkFile(new File(assetsIndexesDir, versionManifest.getJSONObject("assetIndex").getString("id") + ".json"), versionManifest.getJSONObject("assetIndex").getString("sha1")))
            {
                Downloader.download(new File(assetsIndexesDir, versionManifest.getJSONObject("assetIndex").getString("id") + ".json"), versionManifest.getJSONObject("assetIndex").getString("url"), true);
            }
            Logger.info("Assets directory configured");
            if(!this.updateAssets(assetsDir, JSONReader.readJsonFromUrl(new URL(versionManifest.getJSONObject("assetIndex").getString("url"))).getJSONObject("objects")))
            {
                Logger.error("Error while updating assets");
                return false;
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean updateAssets(File assetsDir, JSONObject elements)
    {
        Iterator<String> temp = elements.keys();
        while (temp.hasNext()) {
            String key = temp.next();
            JSONObject value = elements.getJSONObject(key);
            File file = new File(assetsDir, value.getString("hash").substring(0,2) + File.separator + value.getString("hash"));
            file.getParentFile().mkdirs();
            boolean toDownload = false;
            if(file.exists())
            {
                toDownload = !Hash.checkFile(file, value.getString("hash"));
            }
            else
            {
                toDownload = true;
            }
            if(toDownload)
            {
                Logger.info("Downloading assets " + key);
                if(Downloader.download(file, "http://resources.download.minecraft.net/" + value.getString("hash").substring(0,2) + "/" + value.getString("hash"), false))
                {
                    Logger.info("Assets " + key + " downloaded successfully");
                    continue;
                }
                else
                {
                    Logger.error(key + " download fail");
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean updateLibs(File libs, JSONArray elements)
    {
        Logger.info("Updating Mojang libraries");
        for(Object o: elements){
            if ( o instanceof JSONObject ) {
                JSONObject info = (JSONObject)o;
                JSONObject artifact;
                if(info.getJSONObject("downloads").has("classifiers"))
                {
                    if(SystemUtils.IS_OS_WINDOWS)
                    {
                        if(info.getJSONObject("downloads").getJSONObject("classifiers").has("natives-windows"))
                        {
                            artifact = info.getJSONObject("downloads").getJSONObject("classifiers").getJSONObject("natives-windows");
                        }
                        else if(info.getJSONObject("downloads").has("artifact"))
                        {
                            artifact = info.getJSONObject("downloads").getJSONObject("artifact");
                        }
                        else
                        {
                            Logger.warn(info.getString("name") + " not found for this system");
                            continue;
                        }
                    }else if(SystemUtils.IS_OS_LINUX)
                    {
                        if(info.getJSONObject("downloads").getJSONObject("classifiers").has("natives-linux"))
                        {
                            artifact = info.getJSONObject("downloads").getJSONObject("classifiers").getJSONObject("natives-linux");
                        }
                        else if(info.getJSONObject("downloads").has("artifact"))
                        {
                            artifact = info.getJSONObject("downloads").getJSONObject("artifact");
                        }
                        else
                        {
                            Logger.warn(info.getString("name") + " not found for this system");
                            continue;
                        }
                    }
                    else
                    {
                        if(info.getJSONObject("downloads").getJSONObject("classifiers").has("natives-osx"))
                        {
                            artifact = info.getJSONObject("downloads").getJSONObject("classifiers").getJSONObject("natives-osx");
                        }
                        else if(info.getJSONObject("downloads").has("artifact"))
                        {
                            artifact = info.getJSONObject("downloads").getJSONObject("artifact");
                        }
                        else
                        {
                            Logger.warn(info.getString("name") + " not found for this system");
                            continue;
                        }
                    }
                }
                else
                {
                    artifact = info.getJSONObject("downloads").getJSONObject("artifact");
                }
                File file = new File(libs, artifact.getString("path"));
                boolean toDownload = false;
                if(file.exists())
                {
                    toDownload = !Hash.checkFile(file, artifact.getString("sha1"));
                }
                else
                {
                    toDownload = true;
                }
                if(toDownload)
                {
                    if(Downloader.download(new File(libs, artifact.getString("path")), artifact.getString("url"), true))
                    {
                        continue;
                    }
                    else
                    {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean updateMinecraftJar(File libs, JSONObject manifest) {
        File minecraftjar = new File(libs, "minecraft.jar");
        if(!Hash.checkFile(minecraftjar, manifest.getString("sha1")))
        {  
            if(!minecraftjar.exists())
            {
                Logger.info("Downloading minecraft.jar");
            }
            else
            {
                Logger.warn("minecraft.jar corrupted or modified, fetching official minecraft.jar");
            }
            if(Downloader.download(new File(libs, "minecraft.jar"), manifest.getString("url"), true))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            Logger.info("minecraft.jar already downloaded");
            return true;
        }
    }
}