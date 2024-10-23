import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer {

    private int port;

    public UDPServer(int port) {
        this.port = port;
    }

    public UDPServer() {
        this.port = 8080;
    }

    public void launch() {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("UDP Server started on port " + port);
            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();
                String receivedData = new String(packet.getData(), 0, packet.getLength(), "UTF-8");

                System.out.println("Received from " + clientAddress + ":" + clientPort + " - " + receivedData);

                if (receivedData.length() > 1024) {
                    receivedData = receivedData.substring(0, 1024);
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @Override
    public String toString() {
        return "UDPServer is running on port: " + port;
    }

}

