public class Main {
    public static void main(String[] args) {
        ArgumentParser argParser = new ArgumentParser(args);

        String mode = argParser.getServerMode();
        String protocol = argParser.getProtocol();
        int port = argParser.getPort();

        if ("server".equalsIgnoreCase(mode) && "tcp".equalsIgnoreCase(protocol)) {
            TCPServer server = new TCPServer(port);
            server.launch();
        } else if ("server".equalsIgnoreCase(mode) && "udp".equalsIgnoreCase(protocol)) {
            UDPServer server = new UDPServer(port);
            server.launch();
        } else {
            System.out.println("Invalid combination of mode and protocol. Currently supported: TCP server mode.");
        }
    }
}
