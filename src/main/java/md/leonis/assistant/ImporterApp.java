package md.leonis.assistant;

import md.leonis.assistant.dao.RawDAO;
import md.leonis.assistant.dao.WordFrequencyDAO;
import md.leonis.assistant.dao.WordLevelDAO;
import md.leonis.assistant.dao.WordPlaceDAO;
import md.leonis.assistant.domain.Raw;
import md.leonis.assistant.domain.WordFrequency;
import md.leonis.assistant.domain.WordPlace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.zip.GZIPInputStream;

@SpringBootApplication
public class ImporterApp {

    private static final Logger log = LoggerFactory.getLogger(ImporterApp.class);

    private static final Random random = new Random();

    //TODO unused
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    private static ConfigurableApplicationContext springContext;

    public static void main(String[] args) throws IOException, InterruptedException {
        springContext = SpringApplication.run(ImporterApp.class, args);


        log.info("Importing");

        RawDAO rawDAO = springContext.getBean(RawDAO.class);

        String prefix = springContext.getEnvironment().getProperty("very.recommendable.url");

        // 34911
        // 3492
        for (int i = 1; i <= 3492; i++) {
            int delay = random.nextInt(1100) + 400;
            log.info("{}: {}", i, delay);
            String url = String.format("%s/api/v1/vocabulary/search?filters={\"gseRange\":{\"from\":\"10\",\"to\":\"90\"},\"topics\":[],\"audiences\":[\"GL\"],\"grammaticalCategories\":[]}&page=%d&query_string=*&sort=expression.raw", prefix, i);
            String referrer = String.format("%s/vocabulary?page=%s&sort=vocabulary;asc&gseRange=10;90&audience=GL", prefix, i);

            URL urlObject = new URL(url);
            URLConnection urlConnection = urlObject.openConnection();
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            urlConnection.setRequestProperty("Accept", "application/json, text/plain, */*");
            urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            urlConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            urlConnection.setRequestProperty("Connection", "keep-alive");
            //urlConnection.setRequestProperty("Cookie", "_ga=GA1.2.1713043323.155265069…d=GA1.2.1604573161.1552890963");
            urlConnection.setRequestProperty("Host", "www.english.com");
            urlConnection.setRequestProperty("Referer", referrer);
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:65.0) Gecko/20100101 Firefox/65.0");

            String response;
            if ("gzip".equals(urlConnection.getContentEncoding())) {
                response = toString2(urlConnection.getInputStream());
            } else {
                response = toString(urlConnection.getInputStream());
            }
            rawDAO.save(new Raw(response));
            Thread.sleep(delay);
        }


        WordFrequencyDAO wordFrequencyDAO = springContext.getBean(WordFrequencyDAO.class);
        WordPlaceDAO wordPlaceDAO = springContext.getBean(WordPlaceDAO.class);
        WordLevelDAO wordLevelDAO = springContext.getBean(WordLevelDAO.class);

        ClassLoader classLoader = ImporterApp.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("banks/nor.txt")).getFile());
        List<String> list = Files.readAllLines(file.toPath(), Charset.defaultCharset());

        long place = 1;
        for (String nextLine : list) {
            String line = nextLine.trim();
            if (!line.isEmpty() && !line.startsWith("#")) {
                String[] chunks = line.split("\t");
                WordFrequency wordFrequency = new WordFrequency(chunks[0], Long.parseLong(chunks[1]));
                WordPlace wordPlace = new WordPlace(chunks[0], place++);
                wordFrequencyDAO.save(wordFrequency);
                wordPlaceDAO.save(wordPlace);
            }
        }

        System.out.println(wordFrequencyDAO.count());
        System.out.println(wordPlaceDAO.count());

        /*PersonDAO personDAO = springContext.getBean(PersonDAO.class);

        long count = personDAO.count();

        if (count == 0) {
            Person p1 = new Person();

            p1.setFullName("John");

            Date d1 = df.parse("1980-12-20");
            p1.setDateOfBirth(d1);
            //
            Person p2 = new Person();

            p2.setFullName("Smith");
            Date d2 = df.parse("1985-11-11");
            p2.setDateOfBirth(d2);

            personDAO.save(p1);
            personDAO.save(p2);
        }

        Iterable<Person> all = personDAO.findAll();

        StringBuilder sb = new StringBuilder();

        all.forEach(p -> sb.append(p.getFullName()).append("<br>"));

        System.out.println(sb.toString());*/
    }

    private static String toString(InputStream inputStream) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }

            return stringBuilder.toString();
        }
    }

    private static String toString2(InputStream inputStream) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new GZIPInputStream(inputStream)))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }

            return stringBuilder.toString();
        }
    }
}
