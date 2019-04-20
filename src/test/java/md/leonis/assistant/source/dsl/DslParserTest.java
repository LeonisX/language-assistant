package md.leonis.assistant.source.dsl;

import md.leonis.assistant.source.dsl.parser.domain.IntermediateDslObject;
import md.leonis.assistant.source.dsl.parser.domain.ParserState;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DslParserTest {

    @Test
    void test1() {
        String text =
                "[m0]{{Roman}}[b]Ⅰ[/b]{{/Roman}}\n" +
                "[m1]A, a [p]n[/p] [c lightslategray]{{t}}\\[eɪ\\]{{/t}}[/c] ([p]pl[/p] [c teal][lang id=1033]As, A's[/lang][/c] [c lightslategray]{{t}}\\[eɪz\\]{{/t}}[/c])\n" +
                "[trn]";
        IntermediateDslObject dslObject = DslParser.parseWord("A", Arrays.asList(text.split("\n")));

        assertEquals("A", dslObject.getWord());
        assertEquals("A, a", dslObject.getNewWord());
        assertEquals("\\[eɪ\\]", dslObject.getTranscription());
        assertEquals("[[n], [], []]", dslObject.getTags().toString());
        assertEquals("[[n], [], []]", dslObject.getTagsSeq().toString());
        assertNull(dslObject.getNotes());
        assertTrue(dslObject.getLinks().isEmpty());
        assertEquals(ParserState.TRN, dslObject.getState());
    }

    /*@Test
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
    }*/

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