package Components.Commands;

public class InvalidCommand implements Command{

    @Override
    public String execute(String[] args) {
        System.out.println(String.join(" ", args) + ": command not found\n");

        return "";
    }
}
