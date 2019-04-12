package md.leonis.assistant.source.dsl;

import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static md.leonis.assistant.source.dsl.DslParser.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DslParserTest {

    @Test
    void parseM1Transcription() {
        for (int i = 0; i < 5; i++) {
            String prefix = generateRandomString();
            String postfix = generateRandomString();
            String body = generateNonEmplyRandomString();
            String taggedBody = TRANSCRIPTION_START + body + TRANSCRIPTION_END;
            String line = String.format("%s %s %s", prefix, taggedBody, postfix);
            Matcher matcher = TRANSCRIPTION_PATTERN.matcher(line);

            assertTrue(matcher.find());

            String value = matcher.group(0);
            assertEquals(taggedBody, value);
            assertEquals(String.format("%s  %s", prefix, postfix), matcher.replaceAll(""));
            assertEquals(body, value.replace(TRANSCRIPTION_START, "").replace(TRANSCRIPTION_END, ""));
        }
    }

    @Test
    void parseM1Link() {
        for (int i = 0; i < 5; i++) {
            String prefix = generateRandomString();
            String postfix = generateRandomString();
            String body = generateNonEmplyRandomString();
            String taggedBody = LINK_START + body + LINK_END;
            String line = String.format("%s %s %s", prefix, taggedBody, postfix);
            Matcher matcher = LINK_PATTERN.matcher(line);

            assertTrue(matcher.find());

            String value = matcher.group(0);
            assertEquals(taggedBody, value);
            assertEquals(String.format("%s  %s", prefix, postfix), matcher.replaceAll(""));
            assertEquals(body, value.replace(LINK_START, "").replace(LINK_END, ""));
        }
    }

    @Test
    void parseM1Notes() {
        for (int i = 0; i < 5; i++) {
            String prefix = generateRandomString();
            String postfix = generateRandomString();
            String body = Pattern.quote(generateNonEmplyRandomString());
            String taggedBody = NOTES_START + body + NOTES_END;
            String line = String.format("%s %s %s", prefix, taggedBody, postfix);
            Matcher matcher = NOTES_PATTERN.matcher(line);

            assertTrue(matcher.find());

            String value = matcher.group(0);
            assertEquals(taggedBody, value);
            assertEquals(String.format("%s  %s", prefix, postfix), matcher.replaceAll(""));
            assertEquals(body, value.replace(NOTES_START, "").replace(NOTES_END, ""));
        }
    }

    @Test
    void parseM1PoS() {
        for (int i = 0; i < 5; i++) {
            String prefix = generateRandomString();
            String postfix = generateRandomString();
            String body = generateNonEmplyRandomString();
            String taggedBody = POS_START + body + POS_END;
            String line = String.format("%s %s %s", prefix, taggedBody, postfix);
            Matcher matcher = POS_PATTERN.matcher(line);

            assertTrue(matcher.find());

            String value = matcher.group(0);
            assertEquals(taggedBody, value);
            assertEquals(String.format("%s  %s", prefix, postfix), matcher.replaceAll(""));
            assertEquals(body, value.replace(POS_START, "").replace(POS_END, ""));
        }
    }

    private String generateRandomString() {
        Random random = new Random();

        int leftLimit = '0';
        int rightLimit = 'z';
        int targetStringLength = random.nextInt(12);

        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    private String generateNonEmplyRandomString() {
        String result = generateRandomString();
        return result.isEmpty() ? "=" : result;
    }
}