package com.korancrew.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Korancrew
 */
public final class StreamUtil {

    private StreamUtil() {
    }
    public static byte[] read(InputStream is) throws IOException {
        return read(is, true);
    }
    
    public static byte[] read(InputStream is, boolean close) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[10*1024];
            int i = -1;
            while( (i = is.read(buffer)) > 0 ) {
                baos.write(buffer, 0, i);
            }
            baos.flush();
        } finally {
            if(is != null && close) {
                try {
                    is.close();
                } catch(IOException ignore) {}
                is = null;
            }
        }
        return baos.toByteArray();
    }
    
    public static void write(OutputStream os, byte[] bytes, boolean close) throws IOException {
        try {
            int maxPerWrite = 10*1024;
            int len = bytes.length;
            int written = 0;
            while(written < len) {
                int write = (len - written) >= maxPerWrite ? maxPerWrite : (len - written);
                os.write(bytes, written, write);
                written += write;
            }
            os.flush();
        } finally {
            if(os != null && close) {
                try {
                    os.close();
                } catch(IOException ignore) {}
                os = null;
            }
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
}
