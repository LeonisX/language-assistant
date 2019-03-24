package md.leonis.assistant.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

// https://pastebin.com/A96mfdBZ

//https://github.com/chrishumphreys/p2j
//https://www.jython.org/archive/21/docs/jythonc.html
//https://stackoverflow.com/questions/14205464/converting-jython-code-into-a-java-class

// TODO https://github.com/ssut/py-googletrans
// TODO https://github.com/matheuss/google-translate-api
// TODO https://github.com/cjvnjde/google-translate-api-browser
public class GoogleApi {

    // https://translate.google.ru/translate_a/t?client=mt&sl=en&tl=ru&hl=ru&v=1.0&source=baf&tk=538038.958517&q=number&q=the number
    public static String translate(String text) {
        try {
            String textEncoded = URLEncoder.encode(text, StandardCharsets.UTF_8.name());
            String url = "https://translate.googleapis.com/translate_a/single?client=gtx2&sl=es&tl=zh&dt=t&q=" + textEncoded;
            URL urlObject = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) urlObject.openConnection();
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            urlConnection.setRequestProperty("Accept", "*/*");
            urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            urlConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5"); // ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3
            urlConnection.setRequestProperty("Connection", "keep-alive");
            //urlConnection.setRequestProperty("Cookie", "NID=179=YWf77FrtzK8aOlTSMCv--9uGq-dgewC8gQT4DtKqEaT2bJ9nqrWb-nMVaWbZjMNOQDwcSAKopT-Hvbfw3e2NUxXsxZkLLFmj31eNT--pkyyPxA50UvxzGTdAsr4FFkYFl5PsFf-RqI7iprJpXaQdMilFUuOEHD1MfWurDZ8R-KA; _ga=GA1.3.667418802.1539681102; 1P_JAR=2019-3-23-19; _gid=GA1.3.1322275396.1553192478");
            urlConnection.setRequestProperty("Host", "translate.google.com");
            urlConnection.setRequestProperty("TE", "Trailers");
            urlConnection.setRequestProperty("Referer", "https://translate.google.com");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:66.0) Gecko/20100101 Firefox/66.0");

            urlConnection.getResponseCode();
            InputStream stream = urlConnection.getErrorStream();
            if (stream == null) {
                String response;
                if ("gzip".equals(urlConnection.getContentEncoding())) {
                    response = gzipInputStreamToString(urlConnection.getInputStream());
                } else {
                    response = inputStreamToString(urlConnection.getInputStream());
                }

                return response;
            } else {
                throw new RuntimeException(inputStreamToString(stream));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String inputStreamToString(InputStream inputStream) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }

            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String gzipInputStreamToString(InputStream inputStream) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new GZIPInputStream(inputStream)))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }

            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
