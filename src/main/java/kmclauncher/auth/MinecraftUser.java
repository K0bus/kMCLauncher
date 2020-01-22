package kmclauncher.auth;

import org.json.JSONObject;

import kmclauncher.utils.Logger;

public class MinecraftUser{

    private String username;
    private String accessToken;
    private String clientToken;
    private String uuid;

    public MinecraftUser()
    {
        
    }
    public boolean isAuth()
    {
        if(this.accessToken !=null && this.clientToken != null)
            return this.validate(this.username, this.accessToken, this.clientToken, this.uuid);
        else
            return false;
    }

    public boolean auth(String username, String password)
    {
        JSONObject object = new JSONObject();
        JSONObject agent = new JSONObject();
        agent.put("name", "Minecraft");
        agent.put("version", 1);

        object.put("agent", agent);
        object.put("username", username);
        object.put("password", password);
        object.put("clientToken", "");
        object.put("requestUser", true);
        AuthRequest authentificator = new AuthRequest(AuthType.AUTH, object);
        JSONObject data = authentificator.send();
        if(data != null)
        {
            this.username = data.getJSONObject("selectedProfile").getString("name");
            this.accessToken = data.getString("accessToken");
            this.clientToken = data.getString("clientToken");
            this.uuid = data.getJSONObject("selectedProfile").getString("id");
            Logger.info("Account logged with user " + this.username + " and uuid " + this.uuid);
            return true;
        }
        else
        {
            return false;
        }
    }
    public boolean validate(String username, String accessToken, String clientToken, String uuid)
    {
        JSONObject object = new JSONObject();
        object.put("accessToken", accessToken);
        object.put("clientToken", clientToken);
        AuthRequest authentificator = new AuthRequest(AuthType.VALIDATE, object);
        JSONObject data = authentificator.send();
        if(data != null)
        {
            this.username = username;
            this.accessToken = accessToken;
            this.clientToken = clientToken;
            this.uuid = uuid;
            Logger.info("Account validate with user " + this.username + " and uuid " + this.uuid);
            return true;
        }
        else
        {
            return false;
        }
    }
    public String getUsername()
    {
        return this.username;
    }
    public String getAccessToken()
    {
        return this.accessToken;
    }
    public String getClientToken()
    {
        return this.clientToken;
    }
    public String getUuid()
    {
        return this.uuid;
    }
}