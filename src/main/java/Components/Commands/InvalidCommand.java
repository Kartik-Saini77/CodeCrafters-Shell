package Components.Commands;

import org.springframework.stereotype.Component;

@Component
public class InvalidCommand implements Command{

    @Override
    public String execute(String[] args) {
        System.out.print(String.join(" ", args) + ": command not found\n");

        return "";
    }
}
