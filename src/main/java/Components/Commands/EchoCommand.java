package Components.Commands;

import org.springframework.stereotype.Component;

@Component
public class EchoCommand implements Command{

    @Override
    public String execute(String[] args) {
        int n = args.length;
        StringBuilder sb = new StringBuilder();

        for (int i=1; i<n; i++) {
            sb.append(args[i])
                    .append(" ");
        }

        return sb.append("\n").toString();
    }
}
