import org.apache.commons.cli.*;

public class ArgumentParser {
    private String serverMode;
    private String protocol;
    private int port;

    public ArgumentParser(String[] args) {
        parseArguments(args);
    }

    private void parseArguments(String[] args) {
        Options options = new Options();

        options.addOption("m", "mode", true, "Mode of operation (server or client)");
        options.addOption("p", "port", true, "Port number to bind or connect");
        options.addOption("t", "protocol", true, "Protocol to use (tcp or udp)");
        options.addOption("h", "help", false, "Show help message");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("h")) {
                formatter.printHelp("TCP/UDP Chat Application", options);
                System.exit(0);
            }

            serverMode = cmd.getOptionValue("mode", "server"); // Default to server
            protocol = cmd.getOptionValue("protocol", "tcp"); // Default to TCP
            port = Integer.parseInt(cmd.getOptionValue("port", "8080")); // Default to 8080

        } catch (ParseException | NumberFormatException e) {
            System.err.println("Invalid arguments: " + e.getMessage());
            formatter.printHelp("TCP/UDP Chat Application", options);
            System.exit(1);
        }
    }

    public String getServerMode() {
        return serverMode;
    }

    public String getProtocol() {
        return protocol;
    }

    public int getPort() {
        return port;
    }
}
