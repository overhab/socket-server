package edu.school21.sockets.listener;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

@Component
@Scope("prototype")
public class ServerListener implements Runnable {

    private final Scanner scanner = new Scanner(System.in);
    private final ServerSocket serverSocket;

    public ServerListener(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        String input;

        while (true) {
            input = scanner.nextLine();
            if (input.equals("shutdown")) {
                try {
                    serverSocket.close();
                    return ;
                } catch (IOException e) {
                    return ;
                }
            }
        }
    }
}
