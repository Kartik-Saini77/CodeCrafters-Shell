package Components;

import Components.Services.CommandHandler;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class Shell {

    CommandHandler commandHandler;

    public Shell(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public void startServer() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader((System.in)));

        while (true) {
            System.out.print("$ ");
            String input = br.readLine();

            String result = commandHandler.handleCommand(input);

            System.out.print(result);
        }
    }
}
