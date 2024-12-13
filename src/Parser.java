import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Scanner;

public class Parser {

	public static void argHandler(String[] args) throws IOException {
		if (args.length == 0) {
			displayError("args not provided", "null");
		}
		switch (args[0]) { // Flag handling
			default: displayError("unrecognized option", args[0]); break;
			case "-h": displayHelp(); break;
			case "--help": displayHelp(); break;
			case "-s": Interpreter.run(readString(args)); break;
			case "-f":
				if (args[1].isEmpty()) { displayError("file name not provided", "-f ..."); }
				try {
					String fileContents = readFile(args[1]);
					System.out.println(fileContents);
				} catch (IOException e) {
					throw new RuntimeException(e);
				} break;
		}
	}

	private static String readString(String[] args) {
        return Arrays.stream(args)
            .map(line -> {
                String sanitizedLine = line.split("[#;/']")[0]; // Discards comments
                return sanitizedLine.replaceAll("\\s+", ""); // The \\s+ regex strips whitespace
            }).collect(Collectors.joining())
		    .substring(2); // I'm removing 2 characters here because I'm an idiot, and forgot the "-s" flag is part of the args, whoops
	}

	private static String readFile(String pathInput) throws IOException {
		Path filePath = Paths.get(pathInput);
		if (!Files.exists(filePath)) { throw new IOException("File not found: " + pathInput); }

		try (Stream<String> lines = Files.lines(filePath)) {
			return lines.map(line -> {
				String sanitizedLine = line.split("[#;/']")[0]; // Discards comments
				return sanitizedLine.replaceAll("\\s+", ""); // The \\s+ regex strips whitespace
			}).collect(Collectors.joining());
		}
	}

	private static void displayError(String errorMessage, String arg) {
		System.err.println("sbfi: " + errorMessage + " '" + arg + "'");
		System.err.println("Usage: java sbfi -s <string> OR -f <filepath>");
		System.err.println("Try 'sbfi --help for more information.'");
		System.exit(1);
	}

	private static void displayHelp() {
		System.out.println("[ Shoddy Brainfuck Interpreter ]");
		System.out.println("\n-h || --help    Brings up this menu");
		System.out.println("-f <path-to-file>    Interprets the target file");
		System.out.println("-s <input>    Interprets text provided");
	}

	public static int requestChar() {
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.print("> ");
			String userInput = sc.nextLine();
			if (!userInput.isEmpty() && userInput.charAt(0) <= 255 && userInput.length() == 1) { return userInput.charAt(0); }
		}
	}
}