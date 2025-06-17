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
        boolean isInSingleQuotes = false;
        boolean isInDoubleQuotes = false;

        for (int i = 0; i < n; i++) {
            char ch = command.charAt(i);

            if (!isInSingleQuotes && ch == '\"') {
                if (i < n - 1 && command.charAt(i+1) == '\"') {
                    i++;
                } else if (!isInDoubleQuotes) {
                    if (!current.isEmpty()) {
                        list.add(current.toString());
                        current = new StringBuilder();
                    }
                    isInDoubleQuotes = true;
                } else {
                    isInDoubleQuotes = false;
                }
            } else if (!isInDoubleQuotes && ch == '\'') {
                if (i < n - 1 && command.charAt(i+1) == '\'') {
                    i++;
                } else if (!isInSingleQuotes) {
                    if (!current.isEmpty()) {
                        list.add(current.toString());
                        current = new StringBuilder();
                    }
                    isInSingleQuotes = true;
                } else {
                    isInSingleQuotes = false;
                }
            } else if (!isInDoubleQuotes && !isInSingleQuotes && ch == ' ') {
                if (!current.isEmpty()) {
                    list.add(current.toString());
                    current = new StringBuilder();
                }
            } else if (!isInDoubleQuotes && !isInSingleQuotes && ch == '\\' && i < n - 1) {
                i++;
                current.append(command.charAt(i));
            } else if (isInDoubleQuotes){
                if (ch == '\\') {
                    if (i < n - 1 && (command.charAt(i+1) == '\\' || command.charAt(i+1) == '$' || command.charAt(i+1) == '\"' || command.charAt(i+1) == '`'))
                        i++;
                }
                current.append(command.charAt(i));
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
