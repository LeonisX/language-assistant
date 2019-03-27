package md.leonis.assistant;

import md.leonis.assistant.dao.standard.VarianceDAO;
import md.leonis.assistant.domain.standard.Variance;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

//http://ucrel.lancs.ac.uk/bncfreq/flists.html
//http://ucrel.lancs.ac.uk/bncfreq/lists/1_1_all_fullalpha.txt.Z


/*http://ucrel.lancs.ac.uk/bncfreq/flists.html
        http://ucrel.lancs.ac.uk/bncfreq/lists/1_1_all_fullalpha.txt.Z

        Фактически, на базе этого надо сделать несколько списков.

        1. Полный, как есть (20 Мб) - необходимо замапить на наши объекты и сохранить в базу
        2. Для поиска вариантов (3-4 Мб) key:value - готово
        3. Какой-то ещё

        На самом деле, тут не полный список, следует свериться с https://en.wiktionary.org/wiki/appetitively
        И совместить два списка, т.е. если у слова 1	0.00 или что-то похожее, но есть статья в вики, то не удаляем


        Clean this list.
        1. v Combine to groups (normal -> sub)
        2. v Clear groups w/o children
        3. v Convert any &rehy;, &mdash; to symbols
        4. v a_bit -> a bit
        5. May be remove duplicated children
        6. Map type (see banks/screenshot) (PartOfSpeech enum is ready)
        7. v Generate text to check
        8. Smt else
        9. Export to DB (only for simply list)

        ### TODO

        1. Import to DB only headwords (word, ), no variants
        2. Investigate strange Parts of Speech
        3. May be optimize
        */


@SpringBootApplication
public class LemmasImporterApp {

    private static final Logger log = LoggerFactory.getLogger(LemmasImporterApp.class);

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext springContext = SpringApplication.run(LemmasImporterApp.class, args);

        log.info("Importing");


        //Word = Word type (headword followed by any variant forms) - see pp.4-5
        //PoS  = Part of speech (grammatical word class - see pp. 12-13)
        //Freq = Rounded frequency per million word tokens (down to a minimum of 10 occurrences of a lemma per million)- see pp. 5
        //Ra   = Range: number of sectors of the corpus (out of a maximum of 100) in which the word occurs
        //Disp = Dispersion value (Juilland's D) from a minimum of 0.00 to a maximum of 1.00.

        //  Word PoS Freq Ra Disp
        //	%	NoC	%	6	53	0.64

        //	book	NoC	%	374	100	0.95
        //	@	@	book	243	100	0.95
        //	@	@	books	131	100	0.94
        //	book	Uncl	:	0	1	0.00
        //	book	Verb	%	21	97	0.92
        //	@	@	book	5	83	0.88
        //	@	@	booked	12	97	0.92
        //	@	@	booking	4	78	0.88
        //	@	@	bookking	0	1	0.00
        //	@	@	books	0	25	0.81

        String home = System.getProperty("user.home");

        List<String> lines = Files.readAllLines(new File(home + File.separatorChar + "1_1_all_fullalpha.txt").toPath(), Charset.forName("utf8"));
        List<String[]> chunks = lines.stream().map(line -> line.trim().split("\t")).collect(Collectors.toList());

        Map<String[], List<String[]>> map = new LinkedHashMap<>();

        String[] currentRoot = null;
        for (String[] chunk : chunks) {
            if (chunk[0].equals("@")) {
                map.get(currentRoot).add(chunk);
            } else {
                currentRoot = chunk;
                map.put(currentRoot, new ArrayList<>());
            }
        }

        map.entrySet().removeIf(e -> e.getValue().isEmpty());

        FileWriter writer = new FileWriter(home + File.separatorChar + "1_1_all_fullalpha2.txt");
        for (Map.Entry<String[], List<String[]>> entry : map.entrySet()) {
            writer.write("\t" + format(String.join("\t", entry.getKey())) + "\n");
            for (String[] value : entry.getValue()) {
                writer.write("\t" + format(String.join("\t", value)) + "\n");
            }
        }
        writer.close();

        Map<String, String> keyValues = new LinkedHashMap<>();
        map.forEach((key, value) -> value.forEach(v -> {
            if (!v[2].equalsIgnoreCase(key[0])) {
                keyValues.put(v[2], key[0]);
            }
        }));

        FileWriter writer2 = new FileWriter(home + File.separatorChar + "1_1_all_fullalpha3.txt");
        for (Map.Entry<String, String> entry : keyValues.entrySet()) {
            writer2.write(format(entry.getKey()) + " \t" + format(entry.getValue()) + "\n");
        }
        writer2.close();

        VarianceDAO varianceDAO = springContext.getBean(VarianceDAO.class);

        for (Map.Entry<String, String> entry : keyValues.entrySet()) {
            varianceDAO.save(new Variance(null, entry.getKey(), entry.getValue()));
        }

        System.out.println(keyValues);
    }

    private static String format(String text) {
        return Jsoup.parse(text).text().replace("_", " ");
    }
}
