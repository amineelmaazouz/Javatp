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

            Console console = System.console();
            if (console == null) {
                System.out.println("No console available");
                return;
            }


            DatagramSocket socket = new DatagramSocket();

            while (true) {

                String input = console.readLine("Enter message: ");
                if (input == null || input.isEmpty()) {
                    System.out.println("No input provided. Exiting.");
                    break;
                }


                byte[] buffer = input.getBytes("UTF-8");


                InetAddress serverInetAddress = InetAddress.getByName(serverAddress);


                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverInetAddress, serverPort);


                socket.send(packet);
                System.out.println("Message sent to " + serverAddress + ":" + serverPort);
            }


            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
