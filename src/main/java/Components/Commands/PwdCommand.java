package Components.Commands;

import Components.Config.Config;
import org.springframework.stereotype.Component;

@Component
public class PwdCommand implements Command {
    private final Config config;

    public PwdCommand(Config config) {
        this.config = config;
    }

    @Override
    public String execute(String[] args) {
        return config.getWorkingDirectory().toAbsolutePath() + "\r\n";
    }
}
