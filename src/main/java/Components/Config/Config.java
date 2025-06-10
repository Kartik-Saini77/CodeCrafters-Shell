package Components.Config;

import Components.Commands.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Component
public class Config {
    private Path workingDirectory;
    private final Map<String, Command> builtinCommands;

    public Config() {
        this.workingDirectory = Paths.get("");
        this.builtinCommands = new HashMap<>();
    }
}
