package Components.Config;

import Components.Commands.Command;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Config {
    private Path workingDirectory;
    private final Map<String, Command> builtinCommands;
    private List<String> commandHistory;

    public Config() {
        this.workingDirectory = Paths.get("");
        this.builtinCommands = new HashMap<>();
        this.commandHistory = new ArrayList<>();
    }

    public Path getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(Path workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public Map<String, Command> getBuiltinCommands() {
        return builtinCommands;
    }

    public List<String> getCommandHistory() {
        return commandHistory;
    }

    public void setCommandHistory(List<String> commandHistory) {
        this.commandHistory = commandHistory;
    }
}
