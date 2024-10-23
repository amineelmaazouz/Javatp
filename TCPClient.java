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

                Socket socket = new Socket(serverAddress, serverPort);


                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            String inputLine;


            while ((inputLine = userInput.readLine()) != null) {

                out.println(inputLine);


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

