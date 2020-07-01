package Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private InetAddress serverIP;
    private int port;
    private IClientStrategy clientStrategy;

    public Client(InetAddress serverIP, int port, IClientStrategy clientStrategy) {
        this.serverIP = serverIP;
        this.port = port;
        this.clientStrategy = clientStrategy;
    }

    public void communicateWithServer() {
        try{
            Socket socket = new Socket(serverIP, port);
            System.out.println("Client is connected to server!");
            clientStrategy.clientStrategy(socket.getInputStream(), socket.getOutputStream());
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}