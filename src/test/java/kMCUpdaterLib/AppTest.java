package kMCUpdaterLib;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Hello world!
 */
public final class AppTest {
    private AppTest() {
    }

    /**
     * Says hello to the world.
     * 
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        try {
            Updater updater = new Updater(new URL("http://127.0.0.1/kMCUpdate-Server/"),
                    new File(System.getenv("APPDATA") + File.separator + ".test"), "1.12.2");
            if(updater.updateMojang()){
                Logger.info("Mojang updater was successfully done");
            }
            else
            {
                Logger.error("Error with Mojang Updater");
            }
            if(updater.updateServer()){
                Logger.info("Modpack update done");
            }
            else
            {
                Logger.error("Error with Modpack update");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}