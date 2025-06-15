package Components;

import Components.Config.Config;
import Components.Services.CommandHandler;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class Shell {

    CommandHandler commandHandler;
    private final LineReader lineReader;
    private final Terminal terminal;
    private final Config config;

    public Shell(CommandHandler commandHandler, Config config) throws IOException {
        this.commandHandler = commandHandler;
        this.config = config;
        this.terminal = TerminalBuilder.builder()
                .system(true)
//                .jansi(true)
//                .jna(true)
                .build();
        this.lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .variable(LineReader.HISTORY_FILE, System.getProperty("user.home") + "/.shell_history")
                .option(LineReader.Option.HISTORY_VERIFY, false)
                .option(LineReader.Option.HISTORY_BEEP, false)
                .build();
    }

    public void startServer() {
        while (true) {
            String input;
            try {
                input = lineReader.readLine("$ ");
                if (!input.isEmpty()) {
                    config.getCommandHistory().add(input);
                }

                String[] commands = input.split("\\s*\\|\\s*");
                String result = commandHandler.handleCommands(commands);

                terminal.writer().print(result);
                terminal.writer().flush();
            } catch (UserInterruptException e) {
                terminal.writer().println();
                terminal.writer().flush();
            } catch (EndOfFileException e) {
                break;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                terminal.writer().flush();
            }
        }
    }
}
