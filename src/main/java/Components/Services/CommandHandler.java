package Components.Services;

import Components.Commands.*;
import Components.Config.Config;
import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Getter
@Service
public class CommandHandler {
    private final CommandParser commandParser;
    private final Config config;

    public CommandHandler(CommandParser commandParser, Config config, ApplicationContext context) {
        this.commandParser = commandParser;
        this.config = config;

        config.getBuiltinCommands().put("exit", context.getBean(ExitCommand.class));
        config.getBuiltinCommands().put("echo", context.getBean(EchoCommand.class));
        config.getBuiltinCommands().put("type", context.getBean(TypeCommand.class));
        config.getBuiltinCommands().put("pwd", context.getBean(PwdCommand.class));
        config.getBuiltinCommands().put("cd", context.getBean(CdCommand.class));
    }

    public String handleCommand(String input) {
        String[] args = commandParser.parse(input);

        return config.getBuiltinCommands().getOrDefault(args[0], new ExecuteCommand()).execute(args);
    }
}
