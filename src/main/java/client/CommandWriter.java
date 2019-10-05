package client;

import logs.*;
import server.*;

import java.io.*;

public class CommandWriter implements Runnable {

    private BufferedReader reader;
    private BufferedWriter out;
    private Client client;


    @Override
    public void run() {
        String message;
        Command command = new Command();
        JSONConverter converter = new JSONConverter();
        while (true) {
            try {
                System.out.println("Введите сообщение");
                message = reader.readLine();
                if (message.equals("stop")) {
                    command.setLast(true);
                    out.write(converter.codeCommand(command) + "\n");
                    reader.close();
                    out.flush();
                    out.close();
                    client.closeConnection();
                    break;
                } else {
                    command.setLast(false);
                    command.setMessage(message);
                    out.write(converter.codeCommand(command) + "\n");
                    out.flush();
                }
                System.out.println("Сообщение отправлено");
                Log.LOG_MESSAGE_WRITER.debug("Message from " + this.toString() + " sent");
            } catch (IOException e) {
                Log.LOG_MESSAGE_WRITER.error("Message write with error: " + e.getMessage());
                client.closeConnection();
            }
        }
    }

    public CommandWriter(Client client) {
        this.client = client;
        try {
            out = new BufferedWriter(new OutputStreamWriter(client.getClientSocket().getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            Log.LOG_MESSAGE_WRITER.error("Writer didn't create: " + e.getMessage());
        }
    }
}
