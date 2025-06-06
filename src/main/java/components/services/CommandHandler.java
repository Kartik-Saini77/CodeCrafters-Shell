package components.services;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommandHandler {
    CommandParser commandParser;
    Set<String> builtinCommands;

    public CommandHandler(CommandParser commandParser) {
        this.commandParser = commandParser;
    }

    public String handleCommand(String input) {
        String[] command = commandParser.parse(input);

        builtinCommands = new HashSet<>(Arrays.asList(new String[]{"exit", "echo", "type"}));

        return switch (command[0].toLowerCase()) {
            case "echo" -> handleEcho(command);
            case "type" -> handleType(command);
            default -> input + ": command not found";
        };
    }

    private String handleEcho(String[] command) {
        int n = command.length;
        StringBuilder sb = new StringBuilder();

        for (int i=1; i<n; i++) {
            sb.append(command[i])
                    .append(" ");
        }

        return sb.toString();
    }

    private String handleType(String[] command) {
        if (builtinCommands.contains(command[1]))
            return command[1] + " is a shell builtin";

        return command[1] + ": not found";
    }
}
