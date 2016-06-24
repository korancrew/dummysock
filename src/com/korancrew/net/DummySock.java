package com.korancrew.net;

/**
 *
 * @author Korancrew
 */
public class DummySock {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*
        args = new String[] {
            "9999",
            "8888"
        };
        */
        String mode;
        
        if(args.length < 2) {
            System.err.println("Usage:\n"
                    + "java DummySock.jar server <port 1> <port 2> ... <port n>\n\n"
                    + "OR\n\n"
                    + "java DummySock.jar client <host 1:port 1> <host 2:port 2> ... <host n:port n>");
            System.exit(-1);
        }
        mode = args[0];
        if(!"server".equals(mode) && !"client".equals(mode)) {
            System.err.println("Supported mode is: server | client");
            System.exit(-1);
        }
        
        ThreadPool threadPool = new ThreadPool(20);
        for(int a = 1; a < args.length; a++) {            
            if("server".equals(mode)) {
                int port = Integer.parseInt(args[a]);
                threadPool.addWorker(new ServerSocketWorker(threadPool, port));
            } else if("client".equals(mode)) {
                String hostPort = args[a];
                String _hostPorts[] = hostPort.split(":");
                new SocketClientWorker(_hostPorts[0], Integer.parseInt(_hostPorts[1])).doTest();
            }
        }
    }
}
