package md.leonis.assistant.service;

import md.leonis.assistant.dao.user.UserWordBankDAO;
import md.leonis.assistant.domain.user.MemorizationLevel;
import md.leonis.assistant.domain.user.UserWordBank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private TestService testService;

    public List<UserWordBank> getUserWordBank() {
        return userWordBankDAO.findAll();
    }

    public List<UserWordBank> getUserWordBank(int size) {
        Pageable page = PageRequest.of(0, size);
        return userWordBankDAO.findAll(page).getContent();
    }

    public List<UserWordBank> getUserWordBank(int size, Sort sort) {
        Pageable page = PageRequest.of(0, size, sort);
        return userWordBankDAO.findAll(page).getContent();
    }

    //TODO add ordering by date
    public List<UserWordBank> getWordsToLearn() {
        return userWordBankDAO.findByLevelOrderByRepeatTimeAsc(0);
    }

    public List<UserWordBank> getWordsToLearn(int size) {
        Pageable page = PageRequest.of(0, size, Sort.by("repeatTime").ascending());
        return userWordBankDAO.findByLevel(0, page);
    }

    public List<UserWordBank> getWordsToRepeat(int size) {
        Pageable page = PageRequest.of(0, size, Sort.by("repeatTime").ascending());
        return userWordBankDAO.findByLevelGreaterThan(0, page);
    }


    public List<UserWordBank> getWordsToRepeat(int size, Sort sort) {
        Pageable page = PageRequest.of(0, size, Sort.by("repeatTime").ascending().and(sort));
        return userWordBankDAO.findByLevelGreaterThan(0, page);
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

            boolean reading = random.nextInt(3) > 0;
            boolean writing = random.nextInt(2) > 0;

            long smoked = random.nextInt(13);
            long read = reading ? random.nextInt(5) : 0;
            long written = writing ? random.nextInt(3) : 0;

            MemorizationLevel memorizationLevel = MemorizationLevel.values()[random.nextInt(MemorizationLevel.values().length)];

            byte level = (byte) random.nextInt(2);

            UserWordBank userWordBank = new UserWordBank(
                    word,
                    testService.getWordLevel(word).getLevel(),
                    smoked,
                    read,
                    written,
                    memorizationLevel,
                    LocalDateTime.now(),
                    level
            );

            saveUserWordBank(userWordBank);
        }
    }

    public void saveWordsToLearn(List<String> words) {
        words.forEach(word -> {
            //TODO special constructor
            UserWordBank wordToLearn = new UserWordBank(
                    word,
                    testService.getWordLevel(word).getLevel(),
                    0,
                    0,
                    0,
                    MemorizationLevel.UNKNOWN,
                    LocalDateTime.now(),
                    (byte) 0
            );
            saveUserWordBank(wordToLearn);
        });
    }

}
