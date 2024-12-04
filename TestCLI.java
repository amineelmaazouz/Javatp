import org.apache.commons.cli.*;

public class TestCLI {
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("h", "help", false, "Show help");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("h")) {
                System.out.println("Help option detected!");
            } else {
                System.out.println("No help option provided.");
            }
        } catch (ParseException e) {
            System.err.println("Error parsing arguments: " + e.getMessage());
        }
    }
}
