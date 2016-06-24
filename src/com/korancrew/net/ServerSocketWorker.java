package com.korancrew.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Korancrew
 */
public class ServerSocketWorker implements Runnable {

    private final ThreadPool threadPool;
    private final int port;
    private ServerSocket serverSocket;
    
    public ServerSocketWorker(ThreadPool threadPool, int port) {
        this.threadPool = threadPool;
        this.port = port;
    }
    
    public void run() {        
        try {
            System.out.println("Listening at port: " + port + " ...");
            serverSocket = new ServerSocket(port);
            System.out.println(" ... port " + port + " opened, ready to accept connection.");
            Socket socket = null;
            while((socket = serverSocket.accept()) != null) {
                System.out.println("** connection accepted from '" + socket.getRemoteSocketAddress() + "' ..");
                threadPool.addWorker(new SocketWorker(socket));
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if(serverSocket != null && serverSocket.isBound()) {
                try {
                    serverSocket.close();
                } catch(IOException ignore) {}
                serverSocket = null;
            }
        }
    }
    
}
