package edu.school21.sockets.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private Socket clientSocket;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    private Scanner scanner;

    void startChat() {
        String message;

        while (true) {
            message = scanner.nextLine();
            if (message.equals("Exit")) {
                System.out.println("You have left the chat");
                stopConnection();
                break ;
            }
            printWriter.println(message);
        }
    }

    public void listenForMessages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message;
                while (!clientSocket.isClosed()) {
                    try {
                        while ((message = bufferedReader.readLine()) != null) {
                            System.out.println(message);
                        }
                    } catch (IOException exception1) {
                        try {
                            printWriter.close();
                            bufferedReader.close();
                            clientSocket.close();
                        } catch (IOException exception2) {
                            exception2.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    public void sendInput() throws IOException {
        String message;
        String response;

        while (true) {
            message = scanner.nextLine();
            response = sendMessage(message);
            if (message.equalsIgnoreCase("signup")) {
                if (response.equals("accepted")) {
                    response = bufferedReader.readLine();
                }
                System.out.println("----Sign Up----\n" + response);
                message = scanner.nextLine();
                response = sendMessage(message);
                System.out.println(response);
                message = scanner.nextLine();
                response = sendMessage(message);
                if (response.equals("success") || response.equals("fail")) {
                    response = bufferedReader.readLine();
                    System.out.println(response);
                }
            }
            else if (message.equalsIgnoreCase("signin")) {
                if (response.equals("accepted")) {
                    response = bufferedReader.readLine();
                }
                System.out.println("----Sign In----\n" + response);
                message = scanner.nextLine();
                response = sendMessage(message);
                System.out.println(response);
                message = scanner.nextLine();
                response = sendMessage(message);
                if (response.equals("success")) {
                    response = bufferedReader.readLine();
                    System.out.println(response);
                    listenForMessages();
                    startChat();
                    break ;
                } else if (response.equals("fail")) {
                    response = bufferedReader.readLine();
                    System.out.println(response);
                }
            }
            else if (message.equalsIgnoreCase("Exit")) {
                break ;
            }
        }
    }

    public void startConnection(int port) throws IOException {
        try {
            clientSocket = new Socket("127.0.0.1", port);
            printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println(bufferedReader.readLine());
            scanner = new Scanner(System.in);
            sendInput();
        } catch (IOException | RuntimeException e) {
            throw new IOException(e.getMessage());
        }
    }

    public String sendMessage(String message) throws IOException {
        printWriter.println(message);
        String response;
        if ((response = bufferedReader.readLine()) != null) {
            return response;
        }
        return "";
    }

    public void stopConnection() {
        try {
            printWriter.close();
            bufferedReader.close();
            clientSocket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
