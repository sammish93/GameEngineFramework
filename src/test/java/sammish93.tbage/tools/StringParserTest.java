package sammish93.tbage.tools;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import sammish93.tbage.GameEngine;

import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class StringParserTest {

    GameEngine gameEngine = mock(GameEngine.class);

    @Test
    void assertsValidCommandInput() {
        String input = "help";

        try (MockedStatic<StringParser> stringParser = mockStatic(StringParser.class)) {
            stringParser.when(() -> StringParser.read(gameEngine, input))
                    .thenReturn(new TreeMap<String, String>() {{ put("command", input); }});

            TreeMap<String, String> map = StringParser.read(gameEngine, input);
            assertEquals(input, map.get("command"));
        }
    }
}