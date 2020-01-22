package kmclauncher;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import kmclauncher.auth.MinecraftUser;
import kmclauncher.launch.LaunchType;
import kmclauncher.launch.Launcher;
import kmclauncher.update.Updater;
import kmclauncher.utils.Configurator;
import kmclauncher.utils.Logger;

public final class AppTest {
    private AppTest() {
    }

    private static File MC_GAMEDIR = new File(System.getenv("APPDATA") + File.separator + ".akuragaming");
    private static String MC_VERSION = "1.12.2";
    private static String DL_URL = "http://127.0.0.1/kMCUpdate-Server/";

    public static void main(String[] args) {
        try {
            Updater updater = new Updater(new URL(DL_URL), MC_GAMEDIR, MC_VERSION);
            if (updater.removeBadFile("mods")) {
                Logger.info("Remover successfully runned");
            } else {
                Logger.error("Error with Remover");
            }
            if (updater.updateMojang()) {
                Logger.info("Mojang updater was successfully done");
            } else {
                Logger.error("Error with Mojang Updater");
            }
            if (updater.updateServer()) {
                Logger.info("Modpack update done");
            } else {
                Logger.error("Error with Modpack update");
            }
            Configurator config = new Configurator(MC_GAMEDIR, "launcher_options.json");
            MinecraftUser user = config.getUser();
            user.isAuth();
            Launcher launcher = new Launcher(LaunchType.Vanilla, MC_GAMEDIR, "1.12.2", user);
            launcher.run();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
