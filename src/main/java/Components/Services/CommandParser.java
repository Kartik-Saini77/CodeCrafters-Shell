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
        int last = 0;
        boolean isInQuotes = false;

        for (int i = 0; i < n; i++) {
            if (i < last) i = last;
            char ch = command.charAt(i);
            if (!isInQuotes && ch == ' ') {
                String com = command.substring(last, i);
                if (!com.isEmpty()) list.add(com);
                int t = i;
                while(t<n && command.charAt(t) == ' ') t++;
                last = t;
            } else if (ch == '\'') {
                if (!isInQuotes) {
                    isInQuotes = true;
                    last = i + 1;
                } else {
                    String com = command.substring(last, i);
                    if (!com.isEmpty()) list.add(com);
                    isInQuotes = false;
                    int t = i;
                    do t++;
                    while (t<n && command.charAt(t) == ' ');
                    last = t;
                }
            }
        }
        if (last != n-1) {
            String com = command.substring(last, n);
            if (!com.isEmpty()) list.add(com);
        }

        return list.toArray(new String[0]);
    }
}
