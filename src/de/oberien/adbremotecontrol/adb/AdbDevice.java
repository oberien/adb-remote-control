package de.oberien.adbremotecontrol.adb;

import de.oberien.adbremotecontrol.thread.KeyboardInputThread;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class AdbDevice {
    private KeyboardInputThread keyboardThread;

    public AdbDevice() {
        this.keyboardThread = new KeyboardInputThread();
        keyboardThread.start();
    }

    public BufferedImage screenshot() {
        System.out.println("Get Screenshot");
        try (AdbScreencap screencap = new AdbScreencap()) {
            return screencap.getScreenshot();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void text(String text) {
        keyboardThread.addText(text);
    }

    public void type(AndroidKeyEvent key) {
        keyboardThread.addKey(key);
    }

    public void click(int x, int y) {
        try (AdbShell shell = new AdbShell()) {
            shell.executeAsync("input tab " + x + " " + y);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void swipe(int downX, int downY, int upX, int upY) {
        try (AdbShell shell = new AdbShell()) {
            shell.executeAsync("input swipe " + downX + " " + downY + " " + upX + " " + upY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
