package md.leonis.assistant.source.dsl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import md.leonis.assistant.source.Parser;
import md.leonis.assistant.source.dsl.domain.DslRaw;
import md.leonis.assistant.source.dsl.domain.DslRawAbbr;
import md.leonis.assistant.source.dsl.domain.parsed.DslRawAbbrParsed;
import md.leonis.assistant.source.dsl.parser.M0Parser;
import md.leonis.assistant.source.dsl.parser.M1Parser;
import md.leonis.assistant.source.dsl.parser.domain.IntermediateDslObject;
import md.leonis.assistant.source.dsl.parser.domain.ParserState;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualLinkedHashBidiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class DslParser implements Parser {

    private static final Logger log = LoggerFactory.getLogger(DslParser.class);

    @Lazy
    @Autowired
    private DslService dslService;

    @Value("${dsl.total.count}")
    private int totalCount;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public void parse() {
        log.info("Parsing...");

        parseAbbr();
        Iterable<DslRawAbbrParsed> abbrParsed = dslService.findAllRawParsedAbbr();
        //abbrParsed.forEach(e -> System.out.println(String.format("abbrs.put(\"%s\", \"%s\");", e.getWord(), e.getMeaning())));

        BidiMap<String, String> abbrs = new DualLinkedHashBidiMap<>(StreamSupport.stream(abbrParsed.spliterator(), false)
                .collect(Collectors.toMap(DslRawAbbrParsed::getWord, DslRawAbbrParsed::getMeaning)));

        //TODO revert and fix
        //dslService.clearRawParsed();

        for (DslRaw raw : dslService.findAllRaw()) {
            List<String> lines = objectMapper.readValue(raw.getRaw(), new TypeReference<List<String>>() {
            });
            IntermediateDslObject dslObject = parseWord(raw.getWord(), lines, abbrs);
        }

        //TODO revert
        //dslService.saveRawParsed(new DslRawParsed(null, word, null, null));

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

    }

    // flicker
    //	[m0]{{Roman}}[b]Ⅰ[/b]{{/Roman}}
    //	[m1]flicker [c lightslategray]{{t}}\[ˊflɪkə\]{{/t}}[/c]
    //	[trn]
    //	[m2][c brown]1.[/c] [p]n[/p]
    //	[m3][c saddlebrown]1)[/c] мерц[']а[/']ние
    //	[m3][c saddlebrown]2)[/c] трепет[']а[/']ние; дрож[']а[/']ние
    //	[m3][c saddlebrown]3)[/c] кор[']о[/']ткая всп[']ы[/']шка
    //	[m3][c saddlebrown]4)[/c] [p]pl[/p] [p]сл.[/p] (кино)ф[']и[/']льм
    //	[m2][c brown]2.[/c] [p]v[/p]
    //	[m3][c saddlebrown]1)[/c] мерц[']а[/']ть
    //	[m3][c saddlebrown]2)[/c] колых[']а[/']ться; дрож[']а[/']ть
    //	[m3][c saddlebrown]3)[/c] бить, мах[']а[/']ть кр[']ы[/']льями
    //	[m3][c saddlebrown]4)[/c] всп[']ы[/']хивать и г[']а[/']снуть ([i]о надежде[/i] [p]и т.п.[/p]);
    //	[m2][ex][c teal][lang id=1033]a faint hope still flickered in her breast[/lang][/c] сл[']а[/']бая над[']е[/']жда всё ещё т[']е[/']плилась в её душ[']е[/'][/ex]
    //	[/trn]
    //	[m0]{{Roman}}[b]Ⅱ[/b]{{/Roman}}
    //	[m1]flicker [c lightslategray]{{t}}\[ˊflɪkə\]{{/t}}[/c] [p]n[/p]
    //	[trn]
    //	[m1]д[']я[/']тел
    //	[/trn]
    public static IntermediateDslObject parseWord(String word, List<String> lines, BidiMap<String, String> abbrs) {
        IntermediateDslObject dslObject = new IntermediateDslObject(word);

        //log.debug(word);

        M0Parser m0Parser = new M0Parser(dslObject);
        M1Parser m1Parser = new M1Parser(dslObject, abbrs);

        for (String line : lines) {
            if (dslObject.getState() == ParserState.M0) {
                boolean isGroup = m0Parser.tryToReadGroup(line);
                if (isGroup) {
                    continue;
                }
            }

            if (dslObject.getState() == ParserState.M1) {
                m1Parser.parse(line);
                continue;
            }

            // m2

            if (isTrn(line)) {
                dslObject.setState(ParserState.TRN);
                //System.out.println("\t" + line);
                continue;
            }

            //TODO TRN BODY

            if (isTrnEnd(line)) {
                dslObject.setState(ParserState.M0);
                //System.out.println("\t" + line);
                continue;
            }

            //TODO uncovered cases, may be throw exception

            //System.out.println("\t" + line);
        }

        return dslObject;
    }

    private static boolean isTrn(String line) {
        return line.equals("[trn]");
    }

    private static boolean isTrnEnd(String line) {
        return line.equals("[/trn]");
    }


    //TODO attributive — атрибутивное употребление (в качестве определения)
    // long - to -
    // separate meanings: eng/rus
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
