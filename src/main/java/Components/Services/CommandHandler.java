package Components.Services;

import Components.Commands.*;
import Components.Config.Config;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class CommandHandler {
    private final CommandParser commandParser;
    private final Config config;
    ApplicationContext context;
    List<String> paths;

    public CommandHandler(CommandParser commandParser, Config config, ApplicationContext context) {
        this.commandParser = commandParser;
        this.config = config;
        this.context = context;

        this.paths = List.of(System.getenv("PATH").split(":"));

        config.getBuiltinCommands().put("exit", context.getBean(ExitCommand.class));
        config.getBuiltinCommands().put("echo", context.getBean(EchoCommand.class));
        config.getBuiltinCommands().put("type", context.getBean(TypeCommand.class));
        config.getBuiltinCommands().put("pwd", context.getBean(PwdCommand.class));
        config.getBuiltinCommands().put("cd", context.getBean(CdCommand.class));
    }

    public String handleCommands(String[] commands) {
        StringBuilder result = new StringBuilder();

        for (String command : commands) {
            String[] args = commandParser.parse(command);

            if (args.length == 0)
                return "";
            else if (config.getBuiltinCommands().containsKey(args[0])) {
                result = new StringBuilder(config.getBuiltinCommands().get(args[0]).execute(args));
            } else {
                boolean flag = true;
                for (String dir : paths) {
                    File executable = new File(dir, args[0]);
                    if (executable.exists() && executable.canExecute()) {
                        try {
                            flag = false;
                            ProcessBuilder processBuilder = new ProcessBuilder(args);
                            processBuilder.redirectErrorStream(true);

                            Process process = processBuilder.start();
                            if (!result.isEmpty()) {
                                process.getOutputStream().write(result.toString().getBytes());
                                process.getOutputStream().close();
                            }

                            result.setLength(0);

                            try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                                String line;
                                while ((line = br.readLine()) != null) {
                                    result.append(line).append("\r\n");
                                }
                            }
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    }
                }
                if (flag)
                    new InvalidCommand().execute(args);
            }
        }

        return result.toString();
    }

    public CommandParser getCommandParser() {
        return commandParser;
    }

    public Config getConfig() {
        return config;
    }
}
