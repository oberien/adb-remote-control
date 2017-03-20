package de.oberien.adbremotecontrol.adb;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class AdbScreencap extends AdbExecOut {
    private BufferedImage screenshot;

    public AdbScreencap() {
        super("screencap", "-p");
    }

    public BufferedImage getScreenshot() {
        if (screenshot != null) {
            return screenshot;
        }
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(readToEnd());
            screenshot = ImageIO.read(bis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return screenshot;
    }
}
