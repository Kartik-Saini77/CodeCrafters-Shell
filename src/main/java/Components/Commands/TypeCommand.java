package Components.Commands;

import java.io.File;
import java.util.List;
import java.util.Map;

public class TypeCommand implements Command {
    Map<String, Command> builtinCommands;
    List<String> paths;

    public TypeCommand(Map<String, Command> builtinCommands) {
        this.builtinCommands = builtinCommands;
        this.paths = List.of(System.getenv("PATH").split(":"));
    }

    @Override
    public String execute(String[] args) {
        int n = args.length;

        for (int i=1; i<n; i++) {
            if (builtinCommands.containsKey(args[i]))
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
