package Components.Commands;

import Components.Config.Config;
import Components.Exceptions.NumericArgumentRequiredException;
import Components.Exceptions.TooManyArgumentsException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Component
public class ExitCommand implements Command{

    private final Config config;

    public ExitCommand(Config config) {
        this.config = config;
    }

    @Override
    public String execute(String[] args) {
        if (args.length == 1) {
            saveHistory();
            System.exit(0);
            return "0";
        }
        try {
            if (args.length > 2)
                throw new TooManyArgumentsException("exit");
        } catch (TooManyArgumentsException e) {
            return "";
        }
        try {
            try {
                int status = Integer.parseInt(args[1]);
                saveHistory();
                System.exit(status);
            } catch (NumberFormatException e) {
                throw new NumericArgumentRequiredException("exit", args[1]);
            }
        } catch (NumericArgumentRequiredException e) {
            saveHistory();
            System.exit(1);
        }
        return args[1];
    }

    private void saveHistory() {
        File historyFile = null;
        try {
            historyFile = new File(System.getenv("HISTFILE"));
        } catch (Exception e) {
            return;
        }

        try (FileWriter writer = new FileWriter(historyFile, true)) {
            List<String> history = config.getCommandHistory();
            for (String command : history) {
                writer.write(command);
                writer.write("\n");
            }
        } catch (IOException e) {
            //
        }
    }
}
