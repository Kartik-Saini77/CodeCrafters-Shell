package Components.Services;

import org.springframework.stereotype.Service;

@Service
public class CommandParser {

    public String[] parse(String command) {

        return command.split(" ");
    }
}
