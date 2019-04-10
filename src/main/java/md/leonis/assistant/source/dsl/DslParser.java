package md.leonis.assistant.source.dsl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import md.leonis.assistant.source.Parser;
import md.leonis.assistant.source.dsl.domain.DslRaw;
import md.leonis.assistant.source.dsl.domain.DslRawAbbr;
import md.leonis.assistant.source.dsl.domain.parsed.DslGroup;
import md.leonis.assistant.source.dsl.domain.parsed.DslRawAbbrParsed;
import md.leonis.assistant.source.dsl.domain.parsed.DslRawParsed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class DslParser implements Parser {

    static final String TRANSCRIPTION_START = "[c lightslategray]{{t}}";
    static final String TRANSCRIPTION_END = "{{/t}}[/c]";
    private static final String TRANSCRIPTION_REGEX = Pattern.quote(TRANSCRIPTION_START) + ".+" + Pattern.quote(TRANSCRIPTION_END);
    static final Pattern TRANSCRIPTION_PATTERN = Pattern.compile(TRANSCRIPTION_REGEX);

    static final String LINK_START = "[c mediumblue][b]=[/b][/c] <<";
    static final String LINK_END = ">>";
    private static final String LINK_REGEX = Pattern.quote(LINK_START) + ".+" + Pattern.quote(LINK_END);
    static final Pattern LINK_PATTERN = Pattern.compile(LINK_REGEX);

    static final String LINK2_START = "[i]от[/i] <<";
    static final String LINK2_END = ">>";
    private static final String LINK2_REGEX = Pattern.quote(LINK2_START) + ".+" + Pattern.quote(LINK2_END);
    static final Pattern LINK2_PATTERN = Pattern.compile(LINK2_REGEX);

    static final String NOTES_START = "(";
    static final String NOTES_END = ")";
    private static final String NOTES_REGEX = "\\(.+\\)";
    static final Pattern NOTES_PATTERN = Pattern.compile(NOTES_REGEX);

    static final String POS_START = "[p]";
    static final String POS_END = "[/p]";
    private static final String POS_REGEX = Pattern.quote(POS_START) + "((?! ).)+" + Pattern.quote(POS_END);
    static final Pattern POS_PATTERN = Pattern.compile(POS_REGEX);

    private static final Logger log = LoggerFactory.getLogger(DslParser.class);

    @Lazy
    @Autowired
    private DslService dslService;

    @Value("${dsl.total.count}")
    private int totalCount;

    @SneakyThrows
    public void parse() {
        log.info("Parsing...");

        ObjectMapper objectMapper = new ObjectMapper();

        //TODO revert and fix
        //dslService.clearRawParsed();

        for (DslRaw raw : dslService.findAllRaw()) {
            List<String> lines = objectMapper.readValue(raw.getRaw(), new TypeReference<List<String>>() {
            });
            //TODO logic
            boolean inTrn = false;
            String word = raw.getWord();
            String transcription = null;
            List<String> partOfSpeech = new ArrayList<>();
            String notes = null; //TODO deep parse to chunks
            String link = null;
            String link2 = null;
            DslRawParsed dslRawParsed = new DslRawParsed();
            dslRawParsed.setWord(raw.getWord());
            DslGroup group = new DslGroup();

            for (String line : lines) {

                // [m0]{{Roman}}[b]Ⅰ[/b]{{/Roman}}
                if (isNextGroup(line) && !inTrn) {
                    dslRawParsed.getGroups().add(group);
                    group = new DslGroup();
                    continue;
                }

                // m1 (not in [trn])
                // [m1]bootblack [c lightslategray]{{t}}\[ˊbu:tblæk\]{{/t}}[/c] [p]n[/p] ([p]преим.[/p] [p]амер.[/p])
                // [m1]boogie [c lightslategray]{{t}}\[ˊbu:gɪ\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<boogie-woogie>>
                if (isWord(line) && !inTrn) {
                    String unchangedLine = line;

                    line = line.replace("[m1]", "");

                    // ([p]преим.[/p] [p]амер.[/p])
                    Matcher matcher = NOTES_PATTERN.matcher(line);
                    if (matcher.find()) {
                        //TODO deep parse
                        notes = matcher.group(0).replace(NOTES_START, "").replace(NOTES_END, "").trim();
                        line = matcher.replaceAll("");
                    }

                    // [c lightslategray]{{t}}\[ˊbu:tblæk\]{{/t}}[/c]
                    matcher = TRANSCRIPTION_PATTERN.matcher(line);
                    if (matcher.find()) {
                        String value = matcher.group(0);
                        //TODO unescape []
                        transcription = value.replace(TRANSCRIPTION_START, "").replace(TRANSCRIPTION_END, "").trim();
                        line = matcher.replaceAll("");
                    }

                    // [c mediumblue][b]=[/b][/c] <<boogie-woogie>>
                    matcher = LINK_PATTERN.matcher(line);
                    if (matcher.find()) {
                        link = matcher.group(0).replace(LINK_START, "").replace(LINK_END, "").trim();
                        line = matcher.replaceAll("");
                    }

                    // [i]от[/i] <<abacus>>
                    matcher = LINK2_PATTERN.matcher(line);
                    if (matcher.find()) {
                        link2 = matcher.group(0).replace(LINK2_START, "").replace(LINK2_END, "").trim();
                        line = matcher.replaceAll("");
                    }

                    // [p]n[/p]
                    matcher = POS_PATTERN.matcher(line);
                    //TODO few, use while
                    while (matcher.find()) {
                        partOfSpeech.add(matcher.group(0).replace(POS_START, "").replace(POS_END, "").trim());
                        line = matcher.replaceAll("");
                    }

                    //TODO investigate
                    if (!word.equals(line.trim())) {
                        //throw new RuntimeException(line);
                        //System.out.println("---------------------------------" + line);
                    }
                    word = line.trim();

                    String result = String.format("[m1]%s", word);
                    if (transcription != null) {
                        result += String.format(" %s%s%s", TRANSCRIPTION_START, transcription, TRANSCRIPTION_END);
                    }
                    if (!partOfSpeech.isEmpty()) {
                        result += partOfSpeech.stream().map(p -> String.format(" %s%s%s", POS_START, p, POS_END)).collect(Collectors.joining(" "));
                    }
                    if (notes != null) {
                        result += String.format(" %s%s%s", NOTES_START, notes, NOTES_END);
                    }
                    if (link != null) {
                        result += String.format(" %s%s%s", LINK_START, link, LINK_END);
                    }
                    if (link2 != null) {
                        result += String.format(" %s%s%s", LINK2_START, link2, LINK2_END);
                    }

                    if (!result.equals(unchangedLine)) {
                        System.out.println(unchangedLine);
                        System.out.println(result);
                        System.out.println();
                    }

                    transcription = null;
                    partOfSpeech = new ArrayList<>();
                    notes = null;
                    link = null;
                    link2 = null;
                    continue;
                }

                // m2

                if (isTrn(line)) {
                    inTrn = true;
                    //System.out.println("\t" + line);
                    continue;
                }

                if (isTrnEnd(line)) {
                    inTrn = false;
                    //System.out.println("\t" + line);
                    continue;
                }

                //System.out.println("\t" + line);
            }

            //TODO revert
            //dslService.saveRawParsed(new DslRawParsed(null, word, null, null));
        }

        // Split by words. Starts from "". not from "/t"
        //[m0]{{Roman}}[b]Ⅰ[/b]{{/Roman}} группа
        //[m1]: main block часть речи
        //[trn]
        //[m2][c brown]: part of speech
        //[m2][ex][c teal]: examples
        //[m3][c saddlebrown]: meanings
        //
        //trim ['], [/'], [lang id=1033], [/lang]
        //
        //[m1]:
        //[c lightslategray]{{t}}\[ded\]{{/t}}[/c]
        //[p]a[/p]
        //
        //[m2]
        //[c brown]1.[/c]
        //[p]a[/p]
        //
        //Вытащить:
        //- название
        //- транскрипция
        //[
        //- аббревиатура (verb)
        //- переводы
        //- примеры
        //]


        //TODO revert
        //parseAbbr();
    }

    private boolean isNextGroup(String line) {
        return isGroup(line) && !line.contains("[b]Ⅰ[/b]");
    }

    // [m0]{{Roman}}[b]Ⅰ[/b]{{/Roman}}
    private boolean isGroup(String line) {
        return line.startsWith("[m0]{{Roman}}[b]");
    }

    // [m1] (not in [trn])
    // [m1]bookbinding [c lightslategray]{{t}}\[ˊbυkˏbaɪndɪŋ\]{{/t}}[/c] [p]n[/p]
    // [m1]boogie [c lightslategray]{{t}}\[ˊbu:gɪ\]{{/t}}[/c] [c mediumblue][b]=[/b][/c] <<boogie-woogie>>
    private boolean isWord(String line) {
        return line.startsWith("[m1]");
    }

    private boolean isTrn(String line) {
        return line.equals("[trn]");
    }

    private boolean isTrnEnd(String line) {
        return line.equals("[/trn]");
    }

    @SneakyThrows
    private void parseAbbr() {
        dslService.clearRawAbbrParsed();
        ObjectMapper objectMapper = new ObjectMapper();
        for (DslRawAbbr raw : dslService.findAllRawAbbr()) {
            List<String> lines = objectMapper.readValue(raw.getRaw(), new TypeReference<List<String>>() {
            });
            dslService.saveRawAbbrParsed(new DslRawAbbrParsed(null, raw.getWord(), lines.get(0)));
        }
    }

    @Override
    public boolean isParsed() {
        return totalCount == dslService.getRawDataCount();
    }

    @Override
    public String getStatus() {
        if (isParsed()) {
            return "OK";
        } else {
            return String.format("%d/%d", dslService.getRawDataCount(), totalCount);
        }
    }
}
