import java.io.Console;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java UDPClient <server> <port>");
            return;
        }

        String serverAddress = args[0];
        int serverPort = Integer.parseInt(args[1]);

        try {
            // Obtain console to read user input
            Console console = System.console();
            if (console == null) {
                System.out.println("No console available");
                return;
            }

            // Create a UDP socket
            DatagramSocket socket = new DatagramSocket();

            while (true) {
                // Read input from the console
                String input = console.readLine("Enter message: ");
                if (input == null || input.isEmpty()) {
                    System.out.println("No input provided. Exiting.");
                    break;
                }

                // Convert the input to bytes (UTF-8 encoding)
                byte[] buffer = input.getBytes("UTF-8");

                // Get the server address
                InetAddress serverInetAddress = InetAddress.getByName(serverAddress);

                // Create a DatagramPacket to send the data
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverInetAddress, serverPort);

                // Send the packet
                socket.send(packet);
                System.out.println("Message sent to " + serverAddress + ":" + serverPort);
            }

            // Close the socket after sending the data
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
