package edu.school21.sockets.server;

import edu.school21.sockets.listener.ServerListener;
import edu.school21.sockets.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

@Component
@Scope("prototype")
public class ServerClientHandler extends Thread {

    @Autowired
    @Qualifier("usersService")
    private UsersService usersService;

    private static final ArrayList<ServerClientHandler> clients = new ArrayList<>();
    private Socket clientSocket;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;

    public void setSocket(Socket socket) {
        this.clientSocket = socket;
    }

    public void sendMessages(String username, String message) {
        for (ServerClientHandler client : clients) {
            client.printWriter.println(username + ": " + message);
        }
    }

    void startChat(String username) throws IOException {
        StringBuilder input = new StringBuilder();
        String message;

        while ((message = bufferedReader.readLine()) != null) {
            input.append(message);
            if (input.toString().equals("Exit")) {
                clients.remove(this);
                printWriter.close();
                bufferedReader.close();
                clientSocket.close();
                break ;
            }
            if (input.length() != 0) {
                sendMessages(username, input.toString());
                usersService.saveMessage(username, input.toString());
                input.setLength(0);
            }
        }
    }

    private void inputReader() throws IOException {
        String input;
        String[] inputData = new String[2];

        while ((input = bufferedReader.readLine()) != null) {
            if (input.equalsIgnoreCase("signup")) {
                printWriter.println("Enter username:");
                inputData[0] = bufferedReader.readLine();
                printWriter.println("Enter password:");
                inputData[1] = bufferedReader.readLine();
                if (usersService.signUp(inputData[0], inputData[1])) {
                    printWriter.println("success");
                    printWriter.println("Successful!");
                } else {
                    printWriter.println("fail");
                    printWriter.println("Failed: user with username=[" + inputData[0] + "] already exists!");
                }
            }
            else if (input.equalsIgnoreCase("signin")) {
                printWriter.println("Enter username:");
                inputData[0] = bufferedReader.readLine();
                printWriter.println("Enter password:");
                inputData[1] = bufferedReader.readLine();
                if (usersService.signIn(inputData[0], inputData[1])) {
                    printWriter.println("success");
                    printWriter.println("Logged in!");
                    System.out.println(inputData[0] + " connected to the server");
                    startChat(inputData[0]);
                    System.out.println(inputData[0] + " disconnected");
                    break ;
                } else {
                    printWriter.println("fail");
                    printWriter.println("User does not exist or wrong password!");
                }
            }
            printWriter.println("accepted");
        }
    }

    public void run() {
        try {
            clients.add(this);
            printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            printWriter.println("Hello from Server!");
            inputReader();
            printWriter.close();
            bufferedReader.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
