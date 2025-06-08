package Components.Services;

import Components.Commands.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommandHandler {
    CommandParser commandParser;
    Map<String, Command> builtinCommands;

    public CommandHandler(CommandParser commandParser) {
        this.commandParser = commandParser;
        this.builtinCommands = new HashMap<>();

        builtinCommands.put("exit", new ExitCommand());
        builtinCommands.put("echo", new EchoCommand());
        builtinCommands.put("type", new TypeCommand(builtinCommands));
    }

    public String handleCommand(String input) {
        String[] args = commandParser.parse(input);

        return builtinCommands.getOrDefault(args[0], new ExecuteCommand()).execute(args);
    }
}
