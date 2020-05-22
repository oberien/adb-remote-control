package de.oberien.adbremotecontrol.adb;

import de.oberien.adbremotecontrol.Config;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
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
                img = Base64.getDecoder().decode(img);
            }
            ByteArrayInputStream bis = new ByteArrayInputStream(img);
            screenshot = ImageIO.read(bis);
            return screenshot;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
