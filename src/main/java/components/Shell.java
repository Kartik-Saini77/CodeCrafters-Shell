package components;

import components.services.CommandHandler;
import components.services.CommandParser;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class Shell {

    CommandHandler commandHandler;
    CommandParser commandParser;

    public Shell(CommandHandler commandHandler, CommandParser commandParser) {
        this.commandHandler = commandHandler;
        this.commandParser = commandParser;
    }


    public void startServer() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader((System.in)));

        System.out.print("$ ");
        String input = br.readLine();
        System.out.println(input + ": command not found");
    }
}
