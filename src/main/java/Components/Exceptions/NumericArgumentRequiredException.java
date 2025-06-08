package Components.Exceptions;

public class NumericArgumentRequiredException extends RuntimeException {

    public NumericArgumentRequiredException(String command, String message) {
        super("numeric argument required");
        System.out.println(command + ": " + message + ": numeric argument required");
    }
}
