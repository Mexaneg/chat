import java.io.*;
import java.net.*;

public class Server {
    private static Socket clientSocket;
    private static ServerSocket server;
    private static BufferedReader input;
    private static BufferedWriter output;


    public static void main(String[] args) {
        try {
            try {
                server = new ServerSocket(5005);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Server started");
            try {
                clientSocket = server.accept();
                input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                String message = input.readLine();
                System.out.println(message);
                output.write("Сервера ответ "+message+"\n");
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Connect closed");
                try {
                    clientSocket.close();
                    input.close();
                    output.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }



            }
        } finally {
            System.out.println("Сервер закрыт");
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
