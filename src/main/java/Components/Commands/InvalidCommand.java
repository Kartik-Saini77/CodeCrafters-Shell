package Components.Commands;

public class InvalidCommand implements Command{

    @Override
    public String execute(String[] args) {
        return String.join(" ", args) + ": command not found\r\n";
    }
}
