package de.oberien.adbremotecontrol.model;

import de.oberien.adbremotecontrol.adb.AndroidKeyEvent;

/**
 * Created by <a href="jaro.fietz@gmx.de">Jaro Fietz</a>
 */
public class KeyboardInput {
    private String text;
    private AndroidKeyEvent key;

    public KeyboardInput(String text) {
        this.text = text;
    }

    public KeyboardInput(AndroidKeyEvent key) {
        this.key = key;
    }

    public String getText() {
        return text;
    }

    public AndroidKeyEvent getKey() {
        return key;
    }
}
