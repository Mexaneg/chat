package server;

import logs.*;

import java.io.*;
import java.net.*;

public class Connection extends Subject implements Runnable {
    private Socket clientSocket;
    private BufferedReader input;
    private BufferedWriter output;
    private boolean active;


    public Connection(Socket socket, Observer observer) throws IOException {
        super(observer);
        active = true;
        clientSocket = socket;
        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

    }

    @Override
    public void notifyObserverDeleteConnection() {
        super.getObserver().updateConnectionList();
    }

    @Override
    public void notifyObserverSendMessage(String message) {
        super.getObserver().sendMessage(message,this);
    }

    @Override
    public void run() {
        String message;
        try {
            while (true) {
                message = input.readLine();
                if (message.equals("stop")) {
                    this.closeConnection();
                    break;
                }
                notifyObserverSendMessage(message);
            }
        } catch (IOException e) {
            this.closeConnection();
        }
    }


    public void sendMsg(String message) {
        {
            try {
                output.write(message + "\n");
                output.flush();
            } catch (IOException e) {
                Log.LOG_CONNECTION.error("Message send with error: " + e.getMessage());
            }

        }


    }

    public void closeConnection() {
        try {
            if (!clientSocket.isClosed()) {
                clientSocket.close();
                input.close();
                output.close();
                active = false;
                Log.LOG_CONNECTION.info("Connection closed");
                notifyObserverDeleteConnection();

            }
        } catch (IOException e) {
            Log.LOG_CONNECTION.error("Connection closed with error: " + e.getMessage());

        }
    }

    public boolean isActive() {
        return active;
    }
}
