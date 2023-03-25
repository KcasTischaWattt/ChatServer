package org.example;

import java.io.*;
import java.net.*;

public class ChatClient {
    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(HOST, PORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Read welcome message from server
            System.out.println(in.readLine());

            // Prompt user to enter name
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter your name: ");
            String name = userInput.readLine();
            out.println(name);

            // Start a separate thread to handle server messages
            Thread serverThread = new Thread(new ServerHandler(socket));
            serverThread.start();

            // Read user input and send messages to server
            String input;
            while ((input = userInput.readLine()) != null) {
                out.println(input);
            }

            // Clean up and exit
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ServerHandler implements Runnable {
        private final Socket socket;

        public ServerHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

