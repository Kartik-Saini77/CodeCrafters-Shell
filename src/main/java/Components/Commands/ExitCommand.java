package Components.Commands;

import Components.Config.Config;
import Components.Exceptions.NumericArgumentRequiredException;
import Components.Exceptions.TooManyArgumentsException;
import Components.Shell;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ExitCommand implements Command{

    private final ApplicationContext context;

    public ExitCommand(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public String execute(String[] args) {
        if (args.length == 1) {
            context.getBean(Shell.class).saveHistory();
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
                context.getBean(Shell.class).saveHistory();
                System.exit(status);
            } catch (NumberFormatException e) {
                throw new NumericArgumentRequiredException("exit", args[1]);
            }
        } catch (NumericArgumentRequiredException e) {
            context.getBean(Shell.class).saveHistory();
            System.exit(1);
        }
        return args[1];
    }
}
