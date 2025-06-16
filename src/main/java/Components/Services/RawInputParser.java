package Components.Services;

import org.jline.reader.ParsedLine;
import org.jline.reader.Parser;
import org.jline.reader.SyntaxError;

import java.util.Collections;
import java.util.List;

public class RawInputParser implements Parser {
    @Override
    public ParsedLine parse(String line, int cursor, ParseContext context) throws SyntaxError {
        return new ParsedLine() {
            @Override
            public String word() {
                return line;
            }

            @Override
            public int wordCursor() {
                return cursor;
            }

            @Override
            public int wordIndex() {
                return 0;
            }

            @Override
            public List<String> words() {
                return Collections.singletonList(line);
            }

            @Override
            public String line() {
                return line;
            }

            @Override
            public int cursor() {
                return cursor;
            }
        };
    }

    @Override
    public ParsedLine parse(String line, int cursor) throws SyntaxError {
        return parse(line, cursor, ParseContext.UNSPECIFIED);
    }
}
