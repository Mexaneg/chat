package client;

import logs.*;
import server.*;

import java.io.*;

public class CommandReader implements Runnable {
    private BufferedReader in;
    private Client client;


    @Override
    public void run() {
    Command command;
    JSONConverter converter = new JSONConverter();
        try {
            while (true) {
                command = converter.decodeCommand(in.readLine());
                if (command.isLast()) {
                    in.close();
                    client.closeConnection();
                    break;
                } else {
                    System.out.println("Получено сообщение:");
                    System.out.println(command.getMessage());
                    Log.LOG_MESSAGE_READER.debug("Message received");
                    System.out.println("Введите сообщение");

                }
            }

        } catch (IOException e) {
            Log.LOG_MESSAGE_READER.error("Message red with error: "+e.getMessage());
            client.closeConnection();
        }

    }

    public CommandReader(Client client) {
        this.client = client;
        try {
            in = new BufferedReader(new InputStreamReader(client.getClientSocket().getInputStream()));
        } catch (IOException e) {
            Log.LOG_MESSAGE_READER.error("Reader didn't create: "+e.getMessage());
        }


    }
}
