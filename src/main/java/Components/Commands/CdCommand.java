package Components.Commands;

import Components.Config.Config;
import Components.Exceptions.TooManyArgumentsException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;

@Component
public class CdCommand implements Command {
    private final Config config;

    public CdCommand(Config config) {
        this.config = config;
    }

    @Override
    public String execute(String[] args) {
        try {
            if (args.length > 2)
                throw new TooManyArgumentsException("cd");
        } catch (TooManyArgumentsException e) {
            return "";
        }
        if (args.length < 2)
            return "";

        if (args[1].equals("~")) {
            config.setWorkingDirectory(Path.of(System.getenv("HOME")));
        } else {
            Path newPath = config.getWorkingDirectory().resolve(args[1]).toAbsolutePath().normalize();
            File directory = newPath.toFile();

            if (!directory.exists())
                return "cd: " + args[1] + ": No such file or directory\n";
            else if (directory.isFile())
                return "cd: " + args[1] + ": Not a directory\n";
            else
                config.setWorkingDirectory(newPath);
        }

        return "";
    }
}
