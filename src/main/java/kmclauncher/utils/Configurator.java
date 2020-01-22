package kmclauncher.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONObject;

import kmclauncher.auth.MinecraftUser;
import kmclauncher.utils.JSONReader;
import kmclauncher.utils.Logger;

public class Configurator
{

    MinecraftUser user;
    File gameDir;
    File file;
    JSONObject config;

    public Configurator(File gameDir, String file)
    {
        this.user = new MinecraftUser();
        this.gameDir = gameDir;
        this.file = new File(gameDir, file);
        this.load();
    }
    private void generate()
    {
        this.config = new JSONObject();
        Logger.info("Generating default configuration");
        JSONObject param = new JSONObject();
        param.put("maxRam", 4096);
        param.put("minRam", 1024);
        param.put("resX", 1280);
        param.put("resY", 720);
        param.put("console", false);
        param.put("sendCrash", false); // Not used now
        param.put("saveAuth", false);
        this.config.put("options", param);
        this.saveUser(this.user);
        Logger.info("Default configuration generated");
        this.save();
    }
    public void saveUser(MinecraftUser user)
    {
        JSONObject userSave = new JSONObject();
        if(user.isAuth() && this.config.getJSONObject("options").getBoolean("saveAuth"))
        {
            userSave.put("username", this.user.getUsername());
            userSave.put("uuid", this.user.getUuid());
            userSave.put("accessToken", this.user.getAccessToken());
            userSave.put("clientToken", this.user.getClientToken());
        }
        if(this.config.has("user"))
            this.config.remove("user");
        this.config.put("user", userSave);
    }
    public MinecraftUser getUser()
    {
        JSONObject userConfig = this.config.getJSONObject("user");
        MinecraftUser tempUser = new MinecraftUser();
        if(!userConfig.isEmpty())
            tempUser.validate(userConfig.getString("username"), userConfig.getString("accessToken"), userConfig.getString("clientToken"), userConfig.getString("uuid"));
        return tempUser;
    }
    public JSONObject getParams()
    {
        return this.config.getJSONObject("options");
    }
    public void setParamInt(String key, int value)
    {
        if(this.config.getJSONObject("options").has(key))
            this.config.getJSONObject("options").remove(key);
        this.config.getJSONObject("options").put(key, value);
    }
    public void setParamBoolean(String key, boolean value)
    {
        if(this.config.getJSONObject("options").has(key))
            this.config.getJSONObject("options").remove(key);
        this.config.getJSONObject("options").put(key, value);
    }
    public void setUser(MinecraftUser user)
    {
        this.user = user;
        this.saveUser(user);
    }
    private void load()
    {
        if(!this.file.exists())
        {
            Logger.warn("Configuration file " + this.file.getName() + " not found");
            this.generate();
            this.validate();
            return;
        }
        this.config = JSONReader.readJsonFromFile(this.file);
        if(this.config == null)
        {
            Logger.warn("Configuration file " + this.file.getName() + " corrupted or empty");
            this.generate();
            this.validate();
            return;
        }
        this.validate();
            
    }
    private void validate()
    {
        Logger.info("Check configuration integrity");
        boolean t = true;
        t = this.config.has("user") && this.config.has("options");
        if(t)
        {
            JSONObject param = this.config.getJSONObject("options");
            t = param.has("minRam") && param.has("maxRam") && param.has("resX") && param.has("resY") && param.has("console") && param.has("sendCrash") && param.has("saveAuth");
            if(!t)
                Logger.warn("Options corrupted regenerating config");
        }
        else
            Logger.warn("Configuration structure corrupted regenerating config");
        if(!t)
            this.generate();
    }
    public void save()
    {
        Logger.info("Saving configuration");
        try (FileWriter file = new FileWriter(this.file)) {
			file.write(this.config.toString());
        }catch (IOException e) {
            Logger.error("Can't write configuration to " + this.file.getName());
            e.printStackTrace();
        }
        Logger.info("Successfully saved configuration file to " + this.file.getName());
    }

}