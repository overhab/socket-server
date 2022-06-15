package edu.school21.sockets.app;

import edu.school21.sockets.config.SocketsApplicationConfig;
import edu.school21.sockets.server.Server;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        int port = 0;
        if (args.length == 1) {
            if (args[0].startsWith("--port=")) {
                try {
                    port = Integer.parseInt(args[0].substring(args[0].indexOf('=') + 1));
                } catch (IllegalArgumentException exception) {
                    System.out.println("Cannot parse port");
                    System.exit(-1);
                }
            }

            ApplicationContext context = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
            Server server = context.getBean(Server.class);
            server.start(port, context);

        } else {
            System.out.println("Please enter port as an argument (ex: --port=8081)");
        }
    }
}
