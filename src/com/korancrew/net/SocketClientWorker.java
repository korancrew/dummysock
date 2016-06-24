package com.korancrew.net;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author Korancrew
 */
public class SocketClientWorker {

    private final String host;
    private final int port;

    public SocketClientWorker(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    public void doTest() {
        Socket socket = null;
        InputStream in = null;
        try {
            System.out.println("Opening connection to '"+ host + ":" + port + "' ...");
            socket = new Socket(host, port);
            System.out.println(".... OK, connected!\n\n");
            
            in = socket.getInputStream();
            if(in.available() > 0) {
                System.out.println(new String(IOUtil.read(in)));
            }
            
            Thread.currentThread().sleep(5000);
            if(socket.isConnected() && !socket.isClosed()) {
                System.out.println("Force closing connection.\n\n");
                IOUtil.close(in);
                IOUtil.close(socket);
            }
        } catch(Exception ex) {
            //ex.printStackTrace();
            if(ex instanceof java.net.ConnectException) {
                if("Connection refused".equals(ex.getMessage())) {
                    System.err.println("Cannot open connection to '" + host + ":" + port + "': Connection Refused.\n\n");
                } else if("Connection timed out: connect".equals(ex.getMessage())) {
                    System.err.println("Cannot open connection to '" + host + ":" + port + "': Connection Timed Out.\n\n");
                }
            } else {
                ex.printStackTrace();
            }
        } finally {
            IOUtil.close(in);
            IOUtil.close(socket);
        }
    }
    
}
