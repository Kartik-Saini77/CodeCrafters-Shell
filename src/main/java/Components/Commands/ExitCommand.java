package Components.Commands;

import Components.Exceptions.NumericArgumentRequiredException;
import Components.Exceptions.TooManyArgumentsException;

public class ExitCommand implements Command{

    @Override
    public String execute(String[] args) {
        if (args.length == 1) {
            System.exit(0);
            return "0";
        }
        try {
            if (args.length > 2)
                throw new TooManyArgumentsException("exit");
        } catch (TooManyArgumentsException e) {
            return "";
        }
        try {
            try {
                int status = Integer.parseInt(args[1]);
                System.exit(status);
            } catch (NumberFormatException e) {
                throw new NumericArgumentRequiredException("exit", args[1]);
            }
        } catch (NumericArgumentRequiredException e) {
            System.exit(1);
        }
        return args[1];
    }
}
