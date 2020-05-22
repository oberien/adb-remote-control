package de.oberien.adbremotecontrol.view;

import de.oberien.adbremotecontrol.adb.AdbDevice;
import de.oberien.adbremotecontrol.adb.AdbShell;
import de.oberien.adbremotecontrol.adb.AndroidKeyEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ScreenPanel extends JPanel {
    private BufferedImage screenshot;
    private int startX;
    private int startY;
    private long startTime;

    public ScreenPanel(AdbDevice device) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startX = e.getX();
                startY = e.getY();
                startTime = System.currentTimeMillis();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                double scale = getScale();
                double downX = startX / scale;
                double downY = startY / scale;
                long duration = System.currentTimeMillis() - startTime;
                if (startX == e.getX() && startY == e.getY() && duration < 100) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        // right mouse button click = long-press
                        device.swipe((int) downX, (int) downY, (int) downX, (int) downY, 800);
                    } else {
                        device.click((int) downX, (int) downY);
                    }
                    return;
                }
                double upX = e.getX() / scale;
                double upY = e.getY() / scale;
                if (e.getButton() == MouseEvent.BUTTON3) {
                    device.draganddrop((int) downX, (int) downY, (int) upX, (int) upY, duration);
                } else {
                    device.swipe((int) downX, (int) downY, (int) upX, (int) upY, duration);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                requestFocus();
                requestFocusInWindow();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("keyPressed: " + e);
                AndroidKeyEvent key = AndroidKeyEvent.fromAwtKeycode(e.getKeyCode());
                if (key != null) {
                    device.type(key);
                }

                // map Ctrl+d to delete 500
                if (e.getKeyCode() == KeyEvent.VK_D && e.isControlDown()) {
                    StringBuilder sb = new StringBuilder("input keyevent");
                    for (int i = 0; i < 500; i++) {
                        sb.append(" ").append(AndroidKeyEvent.KEY_DEL.getKeycode());
                    }
                    try {
                        new AdbShell().executeAsync(sb.toString());
                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    }
                }
                // catch Ctrl+V for pasting
                if (e.getKeyCode() == KeyEvent.VK_V && e.isControlDown()) {
                    try {
                        String text = (String) Toolkit.getDefaultToolkit()
                            .getSystemClipboard()
                            .getData(DataFlavor.stringFlavor);
                        device.text(text);
                    } catch (UnsupportedFlavorException | IOException e1) {
                        throw new RuntimeException(e1);
                    }
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
//                System.out.println("keyTyped: " + e);
                if (e.getKeyChar() >= 32 && e.getKeyChar() < 127) {
                    device.text(String.valueOf(e.getKeyChar()));
                }
            }
        });
    }

    private double getScale() {
        double scaleX = (double) getWidth() / screenshot.getWidth();
        double scaleY = (double) getHeight() / screenshot.getHeight();
        return Math.min(scaleX, scaleY);
    }

    public void setScreenshot(BufferedImage screenshot) {
        this.screenshot = screenshot;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (screenshot == null) {
            return;
        }

        double scale = getScale();
        double scaledWidth = screenshot.getWidth() * scale;
        double scaledHeight = screenshot.getHeight() * scale;

        g.drawImage(screenshot, 0, 0, (int) scaledWidth, (int) scaledHeight, null);
    }
}
