package md.leonis.assistant.service;

import md.leonis.assistant.dao.user.UserWordBankDAO;
import md.leonis.assistant.dao.user.WordToLearnDAO;
import md.leonis.assistant.domain.user.UserWordBank;
import md.leonis.assistant.domain.user.WordToLearn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

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

}
