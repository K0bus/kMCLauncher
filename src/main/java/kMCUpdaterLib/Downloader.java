package kMCUpdaterLib;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;


public class Downloader {
    public static boolean download(File path, String link, boolean log)
    {
        try {
            path.getParentFile().mkdirs();
            URL url = new URL(link);
            BufferedInputStream in = new BufferedInputStream(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            fileOutputStream.close();
            if(log)
            {
                Logger.info(path.getName() + " downloaded successfully");
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}