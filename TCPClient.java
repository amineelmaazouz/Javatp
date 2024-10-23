import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPClient {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java TCPClient <server> <port>");
            return;
        }

        String serverAddress = args[0];
        int serverPort = Integer.parseInt(args[1]);

        try (
                // Establish a TCP connection to the server
                Socket socket = new Socket(serverAddress, serverPort);

                // Prepare input and output streams
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            String inputLine;

            // Loop until the input is closed (Ctrl+D)
            while ((inputLine = userInput.readLine()) != null) {
                // Send the input to the server
                out.println(inputLine);

                // Read and print the response from the server (in hex format)
                String response = in.readLine();
                if (response != null) {
                    StringBuilder hexOutput = new StringBuilder();
                    for (char c : response.toCharArray()) {
                        hexOutput.append(String.format("%02x", (int) c));
                    }
                    System.out.println("Received (hex): " + hexOutput.toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

