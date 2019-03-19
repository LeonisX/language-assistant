package md.leonis.assistant.service;

import md.leonis.assistant.dao.standard.UserWordBankDAO;
import md.leonis.assistant.dao.standard.WordLevelDAO;
import md.leonis.assistant.domain.LanguageLevel;
import md.leonis.assistant.domain.standard.WordLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class SampleService {

    private static final Logger log = LoggerFactory.getLogger(SampleService.class);

    @Autowired
    private WordLevelDAO wordLevelDAO;

    @Autowired
    private UserWordBankDAO wordBankDAO;

    public void echo() {
        System.out.println("ECHO");
    }

    public LanguageLevel getLevel(String word) {
        List<WordLevel> wordLevelList = wordLevelDAO.findByWord(word);
        System.out.println(wordLevelList.size());
        if (wordLevelList.isEmpty()) {
            return LanguageLevel.UNK;
        }
        if (wordLevelList.size() > 1) {
            log.info(wordLevelList.toString());
        }
        return wordLevelList.stream().min(Comparator.comparing(WordLevel::getWord)).get().getLevel();
    }

    public boolean getKnownStatus(String word) {
        return wordBankDAO.findById(word).isPresent();
    }
}
