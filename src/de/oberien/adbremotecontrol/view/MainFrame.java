package de.oberien.adbremotecontrol.view;

import de.oberien.adbremotecontrol.adb.AdbDevice;
import de.oberien.adbremotecontrol.thread.ScreenshotThread;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("ADB Remote Control");
        AdbDevice device = new AdbDevice();

        ScreenPanel screenPanel = new ScreenPanel(device);
        add(screenPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(630, 1120);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        new ScreenshotThread(screenPanel, device).start();
    }
}
