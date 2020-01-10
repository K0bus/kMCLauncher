package kMCUpdaterLib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.json.JSONException;

public class Hash
{
    public static String fileSHA1(File file) throws FileNotFoundException,
        IOException, NoSuchAlgorithmException {

        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        try (InputStream input = new FileInputStream(file)) {

            byte[] buffer = new byte[8192];
            int len = input.read(buffer);

            while (len != -1) {
                sha1.update(buffer, 0, len);
                len = input.read(buffer);
            }

            return new HexBinaryAdapter().marshal(sha1.digest()).toLowerCase();
        }
    }
    public static boolean checkFile(File file, String hash)
    {
        try {
            if (Hash.fileSHA1(file).equals(hash)) {
                return true;
            } else {
                Logger.info(file.getName() + " corrupted or modified, fetching official " + file.getName());
                return false;
            }
        } catch (NoSuchAlgorithmException | JSONException | IOException e) {
            Logger.info(file.getName() + " corrupted or modified, fetching official " + file.getName());
            e.printStackTrace();
            return false;
        }
    }
}