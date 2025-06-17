package Components.Commands;

import Components.Config.Config;
import Components.Exceptions.NumericArgumentRequiredException;
import Components.Exceptions.TooManyArgumentsException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class HistoryCommand implements Command {

    private final Config config;
    private int lastCommandAppended = -1;

    public HistoryCommand(Config config) {
        this.config = config;
    }

    @Override
    public String execute(String[] args) {
        List<String> commands = config.getCommandHistory();
        StringBuilder result = new StringBuilder();
        int n = commands.size();
        int start = 0;

        if (args.length == 2) {
            try {
                start = Math.max(n - Integer.parseInt(args[1]), 0);
            } catch (NumberFormatException e) {
                throw new NumericArgumentRequiredException("history", args[1]);
            }
        } else if (args.length == 3) {
            Path newPath = config.getWorkingDirectory().resolve(args[2]).toAbsolutePath().normalize();
            File file = newPath.toFile();
            switch(args[1]) {
                case "-r" :
                    if (file.exists() && file.isFile() && file.canRead()) {
                        List<String> newHistory = new ArrayList<>();
                        newHistory.add(config.getCommandHistory().getLast());

                        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                            String line = br.readLine();
                            while (line != null) {
                                if(!line.isBlank())
                                    newHistory.add(line);
                                line = br.readLine();
                            }
                        } catch (IOException e) {
                            //
                        }

                        config.setCommandHistory(newHistory);
                        lastCommandAppended = 0;
                    }
                    return "";
                case "-w" :
                    try (FileWriter writer = new FileWriter(file)) {
                        List<String> history = config.getCommandHistory();
                        for (String command : history) {
                            writer.write(command);
                            writer.write("\n");
                        }
                    } catch (IOException e) {
                        //
                    }
                    return "";
                case "-a" :
                    try (FileWriter writer = new FileWriter(file, true)) {
                        List<String> history = config.getCommandHistory();
                        for (int i = lastCommandAppended + 1; i < history.size(); i++) {
                            writer.write(history.get(i));
                            writer.write("\n");
                            lastCommandAppended++;
                        }
                    } catch (IOException e) {
                        //
                    }
                    return "";
            }
        } else if (args.length > 3) {
            throw new TooManyArgumentsException("history");
        }

        for (int i = start; i < n; i++) {
            result.append(i+1).append('\t').append(commands.get(i)).append('\n');
        }

        return result.toString();
    }
}
