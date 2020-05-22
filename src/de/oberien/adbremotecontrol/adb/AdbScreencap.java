package de.oberien.adbremotecontrol.adb;

import de.oberien.adbremotecontrol.Config;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AdbScreencap extends AdbExecOut {
    private BufferedImage screenshot;
    private boolean useBase64;

    public AdbScreencap(boolean useBase64) {
        super("screencap "
            + (Config.imageFormat.equals("jpg") ? "-j" : "-p")
            + (useBase64 ? " | base64 -w 0" : "")
        );
        this.useBase64 = useBase64;
    }

    public BufferedImage getScreenshot() {
        if (screenshot != null) {
            return screenshot;
        }
        try {
            byte[] img = readToEnd();
            if (this.useBase64) {
                // convert to string to handle UTF-16 correctly on Windows
                String correctEncoding = new String(img);
                img = correctEncoding.getBytes(StandardCharsets.ISO_8859_1);
                Base64.getDecoder().decode(img, img);
            }
            ByteArrayInputStream bis = new ByteArrayInputStream(img);
            screenshot = ImageIO.read(bis);
            return screenshot;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
