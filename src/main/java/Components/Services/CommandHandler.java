package Components.Services;

import Components.Commands.*;
import Components.Config.Config;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
        config.getBuiltinCommands().put("history", context.getBean(HistoryCommand.class));
    }

    public String handleCommands(String[] commands) {
        if (commands.length == 1) {
            String[] args = commandParser.parse(commands[0]);
            return executeCommand(args, null, null);
        }

        try {
            PipedInputStream currentInput = new PipedInputStream();
            PipedOutputStream currentOutput = new PipedOutputStream(currentInput);

            String[] firstCommand = commandParser.parse(commands[0]);
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    executeCommand(firstCommand, null, currentOutput);
                } finally {
                    try {
                        currentOutput.close();
                    } catch (IOException e) {
                        //
                    }
                }
            });

            PipedInputStream prevInput = currentInput;
            for (int i = 1; i < commands.length - 1; i++) {
                PipedInputStream nextInput = new PipedInputStream();
                PipedOutputStream nextOutput = new PipedOutputStream(nextInput);

                String[] currentCommand = commandParser.parse(commands[i]);
                PipedInputStream finalPrevInput = prevInput;
                PipedOutputStream finalNextOutput = nextOutput;

                future = future.thenRunAsync(() -> {
                    try {
                        executeCommand(currentCommand, finalPrevInput, finalNextOutput);
                    } finally {
                        try {
                            finalPrevInput.close();
                            finalNextOutput.close();
                        } catch (IOException e) {
                            //
                        }
                    }
                });

                prevInput = nextInput;
            }

            String[] lastCommand = commandParser.parse(commands[commands.length - 1]);
            PipedInputStream finalInput = prevInput;
            CompletableFuture<String> lastFuture = future.thenApplyAsync(v -> {
                try {
                    return executeCommand(lastCommand, finalInput, null);
                } finally {
                    try {
                        finalInput.close();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            });

            return lastFuture.join();
        } catch (Exception e) {
            return "Error: " + e.getMessage() + "\n";
        }
    }

    private String executeCommand(String[] args, InputStream input, OutputStream output) {
        if (args.length == 0)
            return "";

        if (config.getBuiltinCommands().containsKey(args[0])) {
            String result = config.getBuiltinCommands().get(args[0]).execute(args);
            try {
                if (output != null && result != null) {
                    output.write(result.getBytes());
                    output.flush();
                    return "";
                }
                return result;
            } catch (IOException e) {
                return "Error: " + e.getMessage() + "\n";
            }
        }

        int n = paths.size();
        for (int i = -1; i < n; i++) {
            File executable;
            if (i == -1)
                executable = new File(config.getWorkingDirectory().toAbsolutePath().toString(), args[0]);
            else {
                String dir = paths.get(i);
                executable = new File(dir, args[0]);
            }
            if (executable.exists() && executable.canExecute()) {
                try {
                    ProcessBuilder pb = new ProcessBuilder(args)
                            .directory(config.getWorkingDirectory().toAbsolutePath().toFile())
                            .redirectErrorStream(true);
                    Process process = pb.start();

                    if (input != null) {
                        try (OutputStream processInput = process.getOutputStream()) {
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = input.read(buffer)) != -1) {
                                processInput.write(buffer, 0, bytesRead);
                                processInput.flush();
                            }
                        }
                    }

                    if (output != null) {
                        try (InputStream processOutput = process.getInputStream()) {
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = processOutput.read(buffer)) != -1) {
                                output.write(buffer, 0, bytesRead);
                                output.flush();
                            }
                        }
                        process.waitFor();
                        return "";
                    } else {
                        StringBuilder result = new StringBuilder();
                        try (BufferedReader reader = new BufferedReader(
                                new InputStreamReader(process.getInputStream()))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                result.append(line).append("\n");
                            }
                        }
                        process.waitFor();
                        return result.toString();
                    }
                } catch (IOException | InterruptedException e) {
                    return "Error: " + e.getMessage() + "\n";
                }
            }
        }
        return new InvalidCommand().execute(args);
    }

    public CommandParser getCommandParser() {
        return commandParser;
    }

    public Config getConfig() {
        return config;
    }
}
