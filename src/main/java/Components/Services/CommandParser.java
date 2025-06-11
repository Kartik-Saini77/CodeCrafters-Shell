package Components.Services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommandParser {

    public String[] parse(String command) {
        command = command.trim();
        List<String> list = new ArrayList<>();
        int n = command.length();
        StringBuilder current = new StringBuilder();
        boolean isInQuotes = false;

        for (int i = 0; i < n; i++) {
            char ch = command.charAt(i);

            if (ch == '\'') {
                if (!isInQuotes) {
                    if (!current.isEmpty()) {
                        list.add(current.toString());
                        current = new StringBuilder();
                    }
                    isInQuotes = true;
                } else {
                    isInQuotes = false;
                }
                continue;
            }

            if (!isInQuotes && ch == ' ') {
                if (!current.isEmpty()) {
                    list.add(current.toString());
                    current = new StringBuilder();
                }
            } else {
                current.append(ch);
            }
        }

        if (!current.isEmpty()) {
            list.add(current.toString());
        }

        return list.toArray(new String[0]);
    }
}
