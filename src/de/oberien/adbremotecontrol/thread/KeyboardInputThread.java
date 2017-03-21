package de.oberien.adbremotecontrol.thread;

import de.oberien.adbremotecontrol.adb.AdbShell;
import de.oberien.adbremotecontrol.adb.AndroidKeyEvent;
import de.oberien.adbremotecontrol.model.KeyboardInput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class KeyboardInputThread extends Thread {
    private ArrayBlockingQueue<KeyboardInput> inputs;
    private AdbShell shell;

    public KeyboardInputThread() {
        super("KeyboardInputThread");
        inputs = new ArrayBlockingQueue<>(1000);
        shell = new AdbShell();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                // wait for next event
                KeyboardInput input = inputs.take();
                // get all available events
                ArrayList<KeyboardInput> inputs = new ArrayList<>();
                inputs.add(input);
                this.inputs.drainTo(inputs);
                boolean lastText;
                StringBuilder sb;
                if (input.getText() != null) {
                    lastText = true;
                    sb = new StringBuilder("input text \"");
                } else {
                    lastText = false;
                    sb = new StringBuilder("input keyevent ");
                }
                for (KeyboardInput inp : inputs) {
                    if (inp.getText() != null) {
                        if (!lastText) {
                            lastText = true;
                            shell.executeSync(sb.toString());
                            sb = new StringBuilder("input text \"");
                        }
                        sb.append(inp.getText().replaceAll("\"", "\\\""));
                    } else {
                        if (lastText) {
                            lastText = false;
                            shell.executeSync(sb.append("\"").toString());
                            sb = new StringBuilder("input keyevent ");
                        }
                        sb.append(" ").append(inp.getKey().getKeycode());
                    }
                }
                if (lastText) {
                    sb.append("\"");
                }
                shell.executeSync(sb.toString());
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void addText(String text) {
        inputs.add(new KeyboardInput(text));
    }

    public void addKey(AndroidKeyEvent key) {
        inputs.add(new KeyboardInput(key));
    }
}
