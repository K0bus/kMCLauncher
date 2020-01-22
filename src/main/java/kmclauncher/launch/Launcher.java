package kmclauncher.launch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import kmclauncher.auth.MinecraftUser;
import kmclauncher.utils.Configurator;
import kmclauncher.utils.JSONReader;
import kmclauncher.utils.Logger;
import kmclauncher.utils.Version;

public class Launcher
{

    private JSONObject manifest;
    private LaunchType launchType;
    private String command;
    private File gameDir;
    private Version version;
    private MinecraftUser user;
    private Configurator config;
    private List<String> libraries;
    private Process pr;

    public Launcher(LaunchType launchType, File gameDir, String gameVersion, MinecraftUser user, Configurator config)
    {
        this.launchType = launchType;
        this.user = user;
        this.gameDir = gameDir;
        this.config = config;
        try {
            this.version = new Version(gameVersion);
            this.manifest = JSONReader.readJsonFromUrl(version.getUrl());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        this.libraries = new ArrayList<String>();
        this.command = this.getCommand();
    }
    public void run()
    {
        this.generateCommand(this.config.getParams().getInt("minRAM"), this.config.getParams().getInt("maxRAM"), this.gameDir);
        Logger.info(this.command);
        Runtime rt = Runtime.getRuntime();
        try {
            this.pr = rt.exec(this.command, null, this.gameDir);
        } catch (IOException e) {
            Logger.error("Unable to launch Minecraft");
            e.printStackTrace();
        }
    }
    private void generateCommand(int minRAM, int maxRAM, File gameDir)
    {
        String java = '"' + System.getProperty("java.home") + File.separator + "bin" + File.separator + "java" + '"';
        String javaArgs = "-XX:-UseAdaptiveSizePolicy -XX:+UseConcMarkSweepGC -Xms" + minRAM + "M -Xmx" + maxRAM + "M -Duser.dir="+this.gameDir.getAbsolutePath();
        String natives = "-Djava.library.path=" + new File(gameDir, "natives").getAbsolutePath();
        this.getLibs(new File(this.gameDir, "libs"));
        String libs = "-cp ";
        for(String s : this.libraries)
        {
            libs = libs + s + ";";
        }
        libs = libs + new File(this.gameDir, "minecraft.jar").getAbsolutePath();
        String launchClass = manifest.getString("mainClass");
        if(this.launchType == LaunchType.Forge)
            launchClass = "net.minecraft.launchwrapper.Launch";

        String username = "--username=" + this.user.getUsername();
        String token = "--accessToken " + this.user.getAccessToken();
        String vers = "--version " + this.version.getVersion();
        String dir = "--gameDir " + this.gameDir.getAbsolutePath() + " --assetsDir " + new File(this.gameDir, "assets").getAbsolutePath();
        String assetIndex = "--assetIndex " + manifest.getJSONObject("assetIndex").getString("id");
        String userProperties = "--userProperties {}";
        String uuid = "--uuid " + this.user.getUuid();
        String userType = "--userType legacy";
        String tweakClass = "";
        if(this.launchType == LaunchType.Forge)
            tweakClass = "--tweakClass net.minecraftforge.fml.common.launcher.FMLTweaker";

        this.command = java + " " + javaArgs + " " + natives + " " + libs + " " + launchClass + " " + username + " " + token + " " + vers + " " + dir + " " + assetIndex + " " + userProperties + " " + uuid + " " + userType + " " + tweakClass;
    }
    private void getLibs(File dir)
    {
        for(File f : dir.listFiles())
        {
            if(f.isDirectory())
            {
                this.getLibs(f);
            }
            else
            {
                if(f.getName().substring(f.getName().length()-3).equals("jar"))
                {
                    this.libraries.add(f.getAbsolutePath());
                }
            }
        }
    }
    public String getCommand()
    {
        return this.command;
    }
    public Process getProcess()
    {
        return this.pr;
    }
}