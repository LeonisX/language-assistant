package md.leonis.assistant;

import com.fasterxml.jackson.databind.ObjectMapper;
import md.leonis.assistant.dao.bank.RawDAO;
import md.leonis.assistant.domain.bank.raw.RawContainer;
import md.leonis.assistant.domain.bank.raw.RawData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class RawParserApp {

    private static final Logger log = LoggerFactory.getLogger(RawParserApp.class);

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext springContext = SpringApplication.run(RawParserApp.class, args);

        log.info("Parsing...");

        RawDAO rawDAO = springContext.getBean(RawDAO.class);

        ObjectMapper objectMapper = new ObjectMapper();

        if (3492 != rawDAO.count()) {
            throw new RuntimeException();
        }

        // 3492
        for (long i = 1; i <= rawDAO.count(); i++) {
            String json = rawDAO.findById(i).get().getRaw();

            RawContainer rawContainer = objectMapper.readValue(json, RawContainer.class);
            System.out.println(i);
            List<RawData> rawData = rawContainer.getData();
            if (34911 != rawContainer.getCount()) {
                throw new RuntimeException();
            }

        }
    }
}
