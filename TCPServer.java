import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

    private int port;

    public TCPServer(int port) {
        this.port = port;
    }

    public TCPServer() {
        this.port = 8080;
    }

    public void launch() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("TCP Server started on port " + port);

            while (true) {

                Socket clientSocket = serverSocket.accept();
                System.out.println("New connection from " + clientSocket.getInetAddress());

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);


                String receivedMessage;
                while ((receivedMessage = in.readLine()) != null) {

                    System.out.println("Received from " + clientSocket.getInetAddress() + ": " + receivedMessage);

                    out.println(clientSocket.getInetAddress() + ": " + receivedMessage);
                }

                clientSocket.close();
                System.out.println("Connection closed with " + clientSocket.getInetAddress());
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "TCPServer is running on port: " + port;
    }

}

