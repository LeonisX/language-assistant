package md.leonis.assistant;

import md.leonis.assistant.dao.standard.ParsedRawDataDAO;
import md.leonis.assistant.dao.standard.UserWordBankDAO;
import md.leonis.assistant.domain.standard.UserWordBank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.Random;

@SpringBootApplication
public class CreateUserBankApp {

    private static final Logger log = LoggerFactory.getLogger(CreateUserBankApp.class);

    private static final Random random = new Random();

    public static void main(String[] args) {
        ConfigurableApplicationContext springContext = SpringApplication.run(CreateUserBankApp.class, args);

        log.info("Creating...");

        ParsedRawDataDAO rawDataDAO = springContext.getBean(ParsedRawDataDAO.class);
        UserWordBankDAO wordBankDAO = springContext.getBean(UserWordBankDAO.class);

        List<String> words = rawDataDAO.findAllWords();

        for (int i = 0; i < 3000; i++) {
            int index = random.nextInt(words.size());
            String word = words.get(index);

            boolean smoke = true;
            boolean reading = random.nextInt(3) > 0;
            boolean writing = random.nextInt(2) > 0;

            long smoked = random.nextInt(13);
            long read = reading ? random.nextInt(5): 0;
            long written = writing ? random.nextInt(3): 0;

            UserWordBank userWordBank = new UserWordBank(
                    word,
                    smoke,
                    reading,
                    writing,
                    smoked,
                    read,
                    written
            );
            wordBankDAO.save(userWordBank);
        }
    }
}
