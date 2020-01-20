package kmclauncher;

import org.json.JSONObject;

public class Launcher
{

    private JSONObject manifest;
    private LaunchType launchType;
    private String command;

    public Launcher(JSONObject manifest, LaunchType launchType)
    {
        this.manifest = manifest;
        this.launchType = launchType;
        this.command = this.getCommand();
    }
    public void run()
    {
        this.command = this.getCommand();
    }
    public String getCommand()
    {
        return null;
    }
    public String getJava()
    {
        return null;
    }
    public String getArgs()
    {
        return null;
    }
    public String getLibraries()
    {
        return null;
    }
}