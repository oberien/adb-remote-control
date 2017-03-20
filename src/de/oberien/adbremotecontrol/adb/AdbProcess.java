package de.oberien.adbremotecontrol.adb;

import de.oberien.adbremotecontrol.Config;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class AdbProcess implements Closeable {
    public BufferedInputStream in;
    public BufferedWriter out;
    private Process process;

    public AdbProcess(String... command) {
        try {
            process = new ProcessBuilder(combine(Config.adbPath, command))
                .redirectInput(ProcessBuilder.Redirect.PIPE)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .start();
            in = new BufferedInputStream(process.getInputStream());
            out = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static String[] combine(String a, String[] b) {
        ArrayList<Object> combined = new ArrayList<>();
        combined.add(a);
        Collections.addAll(combined, b);
        return combined.toArray(new String[combined.size()]);
    }

    public byte[] readUntil(byte... until) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while (true) {
            in.mark(4096);
            byte[] buf = new byte[4096];
            int len = in.read(buf);
            // if currently not enough is available, block until further is
            if (len < until.length) {
                in.read();
                in.reset();
                continue;
            }
            int pos = -1;
            for (int i = 0; i < len - until.length + 1; i++) {
                boolean success = true;
                for (int j = 0; j < until.length; j++) {
                    if (buf[i + j] != until[j]) {
                        success = false;
                        break;
                    }
                }
                if (success) {
                    pos = i;
                    break;
                }
            }
            in.reset();
            if (pos != -1) {
                bos.write(buf, 0, pos);
                in.skip(pos + until.length);
                break;
            }
            bos.write(buf, 0, len - until.length + 1);
            in.skip(len - until.length + 1);
        }
        bos.close();
        return bos.toByteArray();
    }

    @Override
    public void close() throws IOException {
        in.close();
        out.close();
    }
}
