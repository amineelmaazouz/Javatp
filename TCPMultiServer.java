import java.net.ServerSocket;
import java.net.Socket;

public class TCPMultiServer {
    private int port;

    public TCPMultiServer(int port) {
        this.port = port;
    }
    public TCPMultiServer() {
        this.port = 8080;
    }

    public void launch() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("TCP MultiServer started on port " + port);
            while (true) {

                Socket clientSocket = serverSocket.accept();
                System.out.println("New connection from " + clientSocket.getInetAddress());


                ConnectionThread connectionThread = new ConnectionThread(clientSocket);
                connectionThread.start(); // Démarrer le thread
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
