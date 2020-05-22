package de.oberien.adbremotecontrol;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config {
    public static final String adbPath;
    public static final long timeout;
    public static final String imageFormat;

    static {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("./config.properties")) {
            properties.load(fis);
        } catch (FileNotFoundException e) {
            System.err.println("Config File not found, falling back to defaults");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        adbPath = properties.getProperty("adbPath", "adb");
        timeout = Long.parseLong(properties.getProperty("timeout", "1"));
        imageFormat = properties.getProperty("imageFormat", "jpg");
    }
}

