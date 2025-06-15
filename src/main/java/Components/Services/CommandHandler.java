package Components.Services;

import Components.Commands.*;
import Components.Config.Config;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class CommandHandler {
    private final CommandParser commandParser;
    private final Config config;
    ApplicationContext context;

    public CommandHandler(CommandParser commandParser, Config config, ApplicationContext context) {
        this.commandParser = commandParser;
        this.config = config;
        this.context = context;

        config.getBuiltinCommands().put("exit", context.getBean(ExitCommand.class));
        config.getBuiltinCommands().put("echo", context.getBean(EchoCommand.class));
        config.getBuiltinCommands().put("type", context.getBean(TypeCommand.class));
        config.getBuiltinCommands().put("pwd", context.getBean(PwdCommand.class));
        config.getBuiltinCommands().put("cd", context.getBean(CdCommand.class));
    }

    public String handleCommand(String input) {
        String[] args = commandParser.parse(input);

        if(args.length == 0)
            return "";
        return config.getBuiltinCommands().getOrDefault(args[0], context.getBean(ExecuteCommand.class)).execute(args);
    }

    public CommandParser getCommandParser() {
        return commandParser;
    }

    public Config getConfig() {
        return config;
    }
}