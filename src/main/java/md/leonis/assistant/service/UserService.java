package md.leonis.assistant.service;

import md.leonis.assistant.dao.user.UserWordBankDAO;
import md.leonis.assistant.dao.user.WordToLearnDAO;
import md.leonis.assistant.domain.user.UserWordBank;
import md.leonis.assistant.domain.user.WordToLearn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Value("${debug.user.word.bank.count}")
    private int userWordBankCount;

    @Autowired
    private UserWordBankDAO userWordBankDAO;

    @Autowired
    private WordToLearnDAO wordToLearnDAO;

    public List<UserWordBank> getUserWordBank() {
        return userWordBankDAO.findAll();
    }

    public void saveWordToLearn(WordToLearn wordToLearn) {
        wordToLearnDAO.save(wordToLearn);
    }

    public List<WordToLearn> getWordsToLearn() {
        return wordToLearnDAO.findAll();
    }

    public UserWordBank saveUserWordBank(UserWordBank userWordBank) {
        return userWordBankDAO.save(userWordBank);
    }

    public long getUserWordBankCount() {
        return userWordBankDAO.count();
    }

    public void generateUserWordBank(List<String> words) {
        Random random = new Random();

        for (int i = 0; i < userWordBankCount; i++) {
            int index = random.nextInt(words.size());
            String word = words.get(index);

            boolean smoke = true;
            boolean reading = random.nextInt(3) > 0;
            boolean writing = random.nextInt(2) > 0;

            long smoked = random.nextInt(13);
            long read = reading ? random.nextInt(5) : 0;
            long written = writing ? random.nextInt(3) : 0;

            UserWordBank userWordBank = new UserWordBank(
                    word,
                    smoke,
                    reading,
                    writing,
                    smoked,
                    read,
                    written
            );
            saveUserWordBank(userWordBank);
        }
    }

}
