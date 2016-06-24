package com.korancrew.net;

import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author Korancrew
 */
public class SocketWorker implements Runnable {

    private final Socket socket;

    public SocketWorker(Socket socket) {
        this.socket = socket;
    }
    
    public void run() {
        OutputStream out = null;
        try {
            out = socket.getOutputStream();
            out.write("OK, you're connected!\nI will disconnect your connection after 1s.\n\n".getBytes());
            out.flush();
            
            Thread.currentThread().sleep(1000);
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            IOUtil.close(out);
            IOUtil.close(socket);
        }
    }
    
}
