package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port;
    private IServerStrategy serverStrategy;
    private volatile boolean stop;
    private int listeningInterval;
    private ExecutorService threadPoolExecutor;

    public Server(int port, int listeningInterval, IServerStrategy serverStrategy)
    {
        this.port = port;
        this.listeningInterval = listeningInterval;
        this.serverStrategy = serverStrategy;
        this.stop = false;
        this.threadPoolExecutor = Executors.newFixedThreadPool(5);
        Configurations.configurationsInit();
        String mexThread = Configurations.loadProperty("ThreadPool");
        if(mexThread != null)
            this.threadPoolExecutor = Executors.newFixedThreadPool(Integer.parseInt(mexThread));
        else
            this.threadPoolExecutor = Executors.newFixedThreadPool(5);
    }

    private void clientHandle (Socket clientSocket)
    {
        try {
            InputStream inFromClient = clientSocket.getInputStream();
            OutputStream outToClient = clientSocket.getOutputStream();
            this.serverStrategy.handleClient(inFromClient, outToClient);
            inFromClient.close();
            outToClient.close();
            clientSocket.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void stop ()
    {
        System.out.println("The server has stopped!");
        this.stop = true;
    }

    public void start() {
        Thread T = new Thread(()->run());
        T.start();
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(listeningInterval);

            while (!stop) {
                try {
                    Socket clientSocket = serverSocket.accept();

                    threadPoolExecutor.execute(() ->{
                        clientHandle(clientSocket);;
                    });

                } catch (IOException e) {
                    //System.out.println("Where are the clients??");
                }
            }
            serverSocket.close();
            threadPoolExecutor.shutdown();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}