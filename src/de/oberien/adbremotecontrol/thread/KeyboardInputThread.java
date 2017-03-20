package de.oberien.adbremotecontrol.thread;

import de.oberien.adbremotecontrol.adb.AdbShell;
import de.oberien.adbremotecontrol.adb.AndroidKeyEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;

public class KeyboardInputThread extends Thread {
    private Semaphore sem;
    private ArrayBlockingQueue<String> texts;
    private ArrayBlockingQueue<AndroidKeyEvent> keys;
    private AdbShell ph;

    public KeyboardInputThread() {
        super("KeyboardInputThread");
        sem = new Semaphore(0);
        texts = new ArrayBlockingQueue<>(100);
        keys = new ArrayBlockingQueue<>(100);
        ph = new AdbShell();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                // wait for next event
                sem.acquire();
                // keyevents
                ArrayList<AndroidKeyEvent> keys = new ArrayList<>();
                this.keys.drainTo(keys);
                if (keys.size() > 0) {
                    sem.acquire(keys.size() - 1);
                    StringBuilder sb = new StringBuilder("input keyevent");
                    for (AndroidKeyEvent key : keys) {
                        sb.append(" ").append(key.getKeycode());
                    }
                    ph.executeSync(sb.toString());
                }
                // text events
                ArrayList<String> texts = new ArrayList<>();
                this.texts.drainTo(texts);
                if (texts.size() > 0) {
                    sem.acquire(texts.size() - 1);
                    StringBuilder sb = new StringBuilder("input text \"");
                    for (String text : texts) {
                        sb.append(text.replaceAll("\"", "\\\""));
                    }
                    sb.append("\"");
                    ph.executeSync(sb.toString());
                }
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void addText(String text) {
        texts.add(text);
        sem.release();
    }

    public void addKey(AndroidKeyEvent key) {
        keys.add(key);
        sem.release();
    }
}
