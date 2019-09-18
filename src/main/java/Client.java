import java.io.*;
import java.net.*;

public class Client {
    private static Socket clientSocket;
    private static BufferedReader in;
    private static BufferedWriter out;

    public static void main(String[] args) {
        try {

            try {
                clientSocket = new Socket("localhost", 5005);
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out =  new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                System.out.println("Введите текст с клавиатуры");
                String message = reader.readLine();
                out.write(message + "\n");
                out.flush();
                String answer = in.readLine();
                System.out.println(answer);


            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            System.out.println("Connect closed");
            try {
                clientSocket.close();
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


}
