package md.leonis.assistant;

import md.leonis.assistant.dao.bank.RawDAO;
import md.leonis.assistant.domain.bank.Raw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.zip.GZIPInputStream;

@SpringBootApplication
public class RawImporterApp {

    private static final Logger log = LoggerFactory.getLogger(RawImporterApp.class);

    private static final Random random = new Random();

    public static void main(String[] args) throws IOException, InterruptedException {
        ConfigurableApplicationContext springContext = SpringApplication.run(RawImporterApp.class, args);

        log.info("Importing");

        String prefix = springContext.getEnvironment().getProperty("very.recommendable.url");

        RawDAO rawDAO = springContext.getBean(RawDAO.class);

        long startIndex = rawDAO.count();

        // 34911
        // 3492
        for (long i = startIndex + 1; i <= 3492; i++) {
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
            rawDAO.save(new Raw(i, response));
            Thread.sleep(delay);
        }
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
