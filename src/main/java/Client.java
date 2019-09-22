import java.io.*;
import java.net.*;

public class Client {
    private int port = 5555;
    private String addr = "localhost";
    private Socket clientSocket;
    private BufferedReader in;
    private BufferedWriter out;
    private BufferedReader reader;
    private ReadMessage input;
    private WriteMessage output;

    private class ReadMessage extends Thread {
        @Override
        public void run() {
            String message;
            try {
                while (true) {
                    message = in.readLine();
                    if (message.equals("stop")) {
                        Client.this.closeConnection();
                        break;
                    } else {
                        System.out.println("Получено сообщение:");
                        System.out.println(message);
                        System.out.println("Введите сообщение");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                Client.this.closeConnection();
            }

        }
    }

    private class WriteMessage extends Thread {
        @Override
        public void run() {

            String message;
            while (true) {
                try {
                    System.out.println("Введите сообщение");
                    message = reader.readLine();
                    if (message.equals("stop")) {
                        out.write("stop" + "\n");
                        Client.this.closeConnection();
                        break;
                    } else {
                        out.write(message + "\n");
                    }
                    out.flush();
                    System.out.println("Сообщение отправлено");
                } catch (IOException e) {
                    e.printStackTrace();
                    Client.this.closeConnection();
                }
            }
        }
    }

    public Client() {
        try {
            clientSocket = new Socket(addr, port);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Клиент запущен");
            input = new ReadMessage();
            output = new WriteMessage();
            input.start();
            output.start();


        } catch (IOException e) {
            e.printStackTrace();
            this.closeConnection();
        }
    }

    private void closeConnection() {
        try {
            if (!clientSocket.isClosed()) {
                clientSocket.close();
                in.close();
                out.close();
                System.out.println("Connect closed");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Client(int port, String addr) {
        this.port = port;
        this.addr = addr;
        try {
            clientSocket = new Socket(addr, port);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(System.in));
            input = new ReadMessage();
            output = new WriteMessage();
            input.start();
            output.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client();

    }


}
