import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChatServer {
    private int port;
    private Map<String, PrintWriter> clients = new HashMap<>(); // Pseudonymes et flux de sortie
    private Set<String> bannedIPs = new HashSet<>(); // Liste des IP bannies

    public ChatServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Chat Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                // Vérifier si l'adresse IP est bannie
                if (bannedIPs.contains(clientSocket.getInetAddress().getHostAddress())) {
                    System.out.println("Rejected banned IP: " + clientSocket.getInetAddress());
                    clientSocket.close();
                    continue;
                }

                System.out.println("New connection from " + clientSocket.getInetAddress());
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true)
        ) {
            String clientIP = clientSocket.getInetAddress().getHostAddress();
            String nickname = "User-" + clientIP; // Par défaut : pseudonyme basé sur l'IP
            clients.put(nickname, out);

            out.println("Welcome to the chat! Your default nickname is: " + nickname);
            out.println("Type #nickname [new_name] to change your nickname.");

            String receivedMessage;
            while ((receivedMessage = in.readLine()) != null) {
                System.out.println("Received from " + nickname + ": " + receivedMessage);

                if (receivedMessage.startsWith("#nickname")) {
                    handleNicknameCommand(receivedMessage, nickname, out);
                } else if (receivedMessage.startsWith("#ban")) {
                    handleBanCommand(receivedMessage, out);
                } else if (receivedMessage.startsWith("#stat")) {
                    handleStatCommand(out);
                } else if (receivedMessage.startsWith("@")) {
                    handlePrivateMessage(receivedMessage, nickname, out);
                } else {
                    broadcastMessage(nickname, receivedMessage, out);
                }
            }

            clients.remove(nickname);
            clientSocket.close();
            System.out.println("Connection closed with " + nickname);
        } catch (Exception e) {
            System.err.println("Error handling client: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleNicknameCommand(String message, String currentNickname, PrintWriter out) {
        String[] parts = message.split(" ", 2);
        if (parts.length == 2) {
            String newNickname = parts[1];
            clients.remove(currentNickname); // Retirer l'ancien pseudonyme
            clients.put(newNickname, out);
            out.println("Your nickname has been changed to: " + newNickname);
        } else {
            out.println("Usage: #nickname [new_name]");
        }
    }

    private void handleBanCommand(String message, PrintWriter out) {
        String[] parts = message.split(" ", 2);
        if (parts.length == 2) {
            String ipToBan = parts[1];
            bannedIPs.add(ipToBan);
            out.println("Banned IP: " + ipToBan);
        } else {
            out.println("Usage: #ban [ip_address]");
        }
    }

    private void handleStatCommand(PrintWriter out) {
        out.println("Connected clients: " + clients.size());
        out.println("Banned IPs: " + bannedIPs.size());
    }

    private void handlePrivateMessage(String message, String senderNickname, PrintWriter out) {
        String[] parts = message.split(" ", 2);
        if (parts.length == 2) {
            String targetUser = parts[0].substring(1);
            String privateMessage = parts[1];
            PrintWriter targetOut = clients.get(targetUser);
            if (targetOut != null) {
                targetOut.println("(Private) " + senderNickname + ": " + privateMessage);
                out.println("Message sent to " + targetUser);
            } else {
                out.println("User " + targetUser + " not found.");
            }
        } else {
            out.println("Usage: @user [message]");
        }
    }

    private void broadcastMessage(String senderNickname, String message, PrintWriter out) {
        for (PrintWriter writer : clients.values()) {
            if (writer != out) {
                writer.println(senderNickname + ": " + message);
            }
        }
    }
}
