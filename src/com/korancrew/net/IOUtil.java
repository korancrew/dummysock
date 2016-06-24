package com.korancrew.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.Socket;

/**
 *
 * @author Korancrew
 */
public final class IOUtil {

    private IOUtil() {
    }
    
    public static byte[] read(InputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            int i = -1;
            byte[] buffer = new byte[1024*4];
            while( (i = in.read(buffer)) != -1 ) {
                baos.write(buffer, 0, i);
            }
            baos.flush();
        } finally {
            close(in);
        }
        return baos.toByteArray();
    }
    
    public static void close(Socket sock) {
        if(sock != null) {
            try {
                sock.close();
            } catch(IOException ignore) {}
            sock = null;
        }
    }
    
    public static void close(InputStream in) {
        if(in != null) {
            try {
                in.close();
            } catch(IOException ignore) {}
            in = null;
        }
    }
    
    public static void close(OutputStream out) {
        if(out != null) {
            try {
                out.close();
            } catch(IOException ignore) {}
            out = null;
        }
    }
    
    public static void close(Reader reader) {
        if(reader != null) {
            try {
                reader.close();
            } catch(IOException ignore) {}
            reader = null;
        }
    }
    
    public static void write(String text, OutputStream out) throws IOException {
        out.write(text.getBytes());
    }
}
