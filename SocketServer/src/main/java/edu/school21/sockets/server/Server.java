package edu.school21.sockets.server;

import edu.school21.sockets.listener.ServerListener;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.*;

@Component
public class Server {

    private ServerSocket serverSocket;

    public void start(int port, ApplicationContext context) {
        try {
            serverSocket = new ServerSocket(port);
            Thread listener = new Thread(new ServerListener(serverSocket));
            listener.start();
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                ServerClientHandler client = context.getBean(ServerClientHandler.class);
                client.setSocket(socket);
                client.start();
            }
        } catch (IOException | RuntimeException e) {
            System.out.println("server closing");
        }
    }
}
