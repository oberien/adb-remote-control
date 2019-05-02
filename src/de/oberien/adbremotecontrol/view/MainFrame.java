package de.oberien.adbremotecontrol.view;

import de.oberien.adbremotecontrol.adb.AdbDevice;
import de.oberien.adbremotecontrol.thread.ScreenshotThread;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("ADB Remote Control");
        AdbDevice device = new AdbDevice();

        ScreenPanel screenPanel = new ScreenPanel(device);
        add(screenPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        BufferedImage screenshot = device.screenshot();
        System.out.println("Device screen size " + screenshot.getWidth() + " x " + screenshot.getHeight());
        int width = screenshot.getWidth(), height = screenshot.getHeight();
        double aspectRatio = (double) width / (double) height;

        if (width >= screenSize.width || height >= screenSize.height) {
            if (aspectRatio > 1) {
                // landscape
                width = (int) (screenSize.width * 0.8);
                height = (int) (width / aspectRatio);
            } else {
                // portrait
                height = (int) (screenSize.height * 0.8);
                width = (int) (height * aspectRatio);
            }
        }
        System.out.println("Initial window size " + width + " x " + height);
        setSize(width, height);
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);

        new ScreenshotThread(screenPanel, device).start();
    }
}
