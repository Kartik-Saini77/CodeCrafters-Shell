package Components.Commands;

import Components.Config.Config;
import Components.Exceptions.NumericArgumentRequiredException;
import Components.Exceptions.TooManyArgumentsException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HistoryCommand implements Command {

    private final Config config;

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
        } else if (args.length > 2) {
            throw new TooManyArgumentsException("history");
        }

        for (int i = start; i < n; i++) {
            result.append(i+1).append('\t').append(commands.get(i)).append('\n');
        }

        return result.toString();
    }
}
