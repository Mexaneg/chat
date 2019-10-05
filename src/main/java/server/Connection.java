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
    public void notifyObserverSendCommand(Command command) {
        super.getObserver().sendCommand(command, this);
    }

    @Override
    public void run() {

        Command command;
        JSONConverter converter = new JSONConverter();
        try {
            while (true) {
                command = converter.decodeCommand(input.readLine());
                if (command.isLast()) {
                    this.closeConnection();
                    break;
                }
                notifyObserverSendCommand(command);
            }
        } catch (IOException e) {
            this.closeConnection();
        }
    }


    public void sendMsg(Command command) {
        {
            JSONConverter converter = new JSONConverter();
            try {
                output.write(converter.codeCommand(command) + "\n");
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
