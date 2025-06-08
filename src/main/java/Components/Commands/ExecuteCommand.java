package Components.Commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ExecuteCommand implements Command {
    List<String> paths;

    public ExecuteCommand() {
        this.paths = List.of(System.getenv("PATH").split(":"));
    }

    @Override
    public String execute(String[] args) {
        for (String dir : paths) {
            File executable = new File(dir, args[0]);
            if (executable.exists() && executable.canExecute()) {
                try {
                    ProcessBuilder processBuilder = new ProcessBuilder(args);
                    processBuilder.redirectErrorStream(true);

                    Process process = processBuilder.start();

                    try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            System.out.println(line);
                        }
                    }
                    return "";
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return new InvalidCommand().execute(args);
    }
}
