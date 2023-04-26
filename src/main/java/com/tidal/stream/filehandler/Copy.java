package com.tidal.stream.filehandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class Copy {
    static void bufferStreamCopy(InputStream in, OutputStream out) throws IOException {
        synchronized (in) {
            synchronized (out) {
                byte[] buffer = new byte[256];
                while (true) {
                    int bytesRead = in.read(buffer);
                    if (bytesRead == -1) {
                        break;
                    }
                    out.write(buffer, 0, bytesRead);
                }
            }
        }
    }
}
