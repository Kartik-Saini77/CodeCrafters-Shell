package Components;

import Components.Config.Config;
import Components.Services.CommandHandler;
import Components.Services.RawInputParser;
import org.jline.reader.EndOfFileException;
import org.jline.reader.UserInterruptException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class Shell {

    CommandHandler commandHandler;
//    private final LineReader lineReader;
//    private final Terminal terminal;
    private final Config config;
    private int commandHistoryLength;

    public Shell(CommandHandler commandHandler, Config config) throws IOException {
        Logger jlineLogger = Logger.getLogger("org.jline");
        jlineLogger.setLevel(Level.OFF);
        for (Handler handler : jlineLogger.getHandlers()) {
            handler.setLevel(Level.OFF);
        }

        this.commandHistoryLength = 0;

        this.commandHandler = commandHandler;
        this.config = config;
//        this.terminal = TerminalBuilder.builder()
//                .system(true)
//                .build();
//        this.lineReader = LineReaderBuilder.builder()
//                .terminal(terminal)
//                .parser(new RawInputParser())
//                .option(LineReader.Option.HISTORY_VERIFY, false)
//                .option(LineReader.Option.HISTORY_BEEP, false)
//                .build();
    }

    public void startServer() throws IOException {
        File historyFile = null;
        try {
            historyFile = new File(System.getenv("HISTFILE"));
        } catch (Exception e) {
            //
        }

        if (historyFile != null && historyFile.exists() && historyFile.isFile() && historyFile.canRead()) {
            List<String> history = config.getCommandHistory();

            try (BufferedReader br = new BufferedReader(new FileReader(historyFile))) {
                String line = br.readLine();
                while (line != null) {
                    commandHistoryLength++;
                    if(!line.isBlank())
                        history.add(line);
                    line = br.readLine();
                }
            } catch (IOException e) {
                //
            }
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((System.in)));

        while (true) {
            System.out.print("$ ");
            String input = br.readLine();
            if (!input.isEmpty()) {
                config.getCommandHistory().add(input);
            }

            String[] commands = input.split(" \\| ");
            String result = commandHandler.handleCommands(commands);

            System.out.print(result);
        }

//        while (true) {
//            String input;
//            try {
//                input = lineReader.readLine("$ ");
//                if (!input.isEmpty()) {
//                    config.getCommandHistory().add(input);
//                }
//
//                String[] commands = input.split(" \\| ");
//                String result = commandHandler.handleCommands(commands);
//
//                terminal.writer().print(result);
//                terminal.writer().flush();
//            } catch (UserInterruptException e) {
//                terminal.writer().println();
//                terminal.writer().flush();
//            } catch (EndOfFileException e) {
//                break;
//            } catch (Exception e) {
//                System.out.println("Error: " + e.getMessage());
//                terminal.writer().flush();
//            }
//        }
    }

    public void saveHistory() {
        File historyFile = null;
        try {
            historyFile = new File(System.getenv("HISTFILE"));
        } catch (Exception e) {
            return;
        }

        try (FileWriter writer = new FileWriter(historyFile, true)) {
            List<String> history = config.getCommandHistory();
            for (int i = commandHistoryLength; i < history.size(); i++) {
                writer.write(history.get(i));
                writer.write("\n");
            }
        } catch (IOException e) {
            //
        }
    }
}
