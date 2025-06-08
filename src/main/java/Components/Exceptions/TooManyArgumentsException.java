package Components.Exceptions;

public class TooManyArgumentsException extends RuntimeException {

    public TooManyArgumentsException(String command) {
        super("too many arguments");
        System.out.println(command + ": too many arguments");
    }
}
