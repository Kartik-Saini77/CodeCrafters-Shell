package Components.Commands;

import Components.Config.Config;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.util.List;
import java.util.Map;

@Component
public class TypeCommand implements Command {

    private final Config config;
    private final List<String> paths;

    public TypeCommand(Config config) {
        this.config = config;
        this.paths = List.of(System.getenv("PATH").split(":"));
    }

    @Override
    public String execute(String[] args) {
        int n = args.length;

        for (int i=1; i<n; i++) {
            if (config.getBuiltinCommands().containsKey(args[i]))
                System.out.println(args[i] + " is a shell builtin");
            else if (!checkInPath(args[i])){
                System.out.println(args[i] + ": not found");
            }
        }
        return "";
    }

    public boolean checkInPath(String command) {
        for (String dir : paths) {
            File executable = new File(dir, command);
            if (executable.exists() && executable.canExecute()) {
                System.out.println(command + " is " + executable.getAbsolutePath());
                return true;
            }
        }

        return false;
    }
}
