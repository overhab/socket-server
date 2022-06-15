package edu.school21.sockets.app;

import edu.school21.sockets.server.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        int port = 0;
        if (args.length == 1) {
            if (args[0].startsWith("--server-port=")) {
                try {
                    port = Integer.parseInt(args[0].substring(args[0].indexOf('=') + 1));
                } catch (IllegalArgumentException exception) {
                    System.out.println("Cannot parse port");
                    System.exit(-1);
                }
            }

            Server server = new Server();
            try {
                server.startConnection(port);
            } catch (IOException exception) {
                System.out.println("Connection failed [" + exception.getMessage() + "]");
                System.exit(-1);
            }
        } else {
            System.out.println("Please enter port as an argument (ex: --port=8081)");
        }
    }
}
