package de.oberien.adbremotecontrol.adb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AdbExecOut extends AdbProcess {
    public AdbExecOut(String... command) {
        super(combine("shell", command));
    }

    protected byte[] readToEnd() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int len;
        while ((len = in.read(buf)) != -1) {
            bos.write(buf, 0, len);
        }
        return bos.toByteArray();
    }
}
