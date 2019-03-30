package md.leonis.assistant.service;

import md.leonis.assistant.dao.bank.ParsedDataDAO;
import md.leonis.assistant.domain.bank.ParsedData;
import md.leonis.assistant.domain.test.WordLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankService {

    private static final Logger log = LoggerFactory.getLogger(BankService.class);

    @Autowired
    private ParsedDataDAO parsedDataDAO;

    public List<ParsedData> getParsedRawData() {
        return parsedDataDAO.findAll();
    }

    public long getParsedRawDataCount() {
        return parsedDataDAO.count();
    }

    public List<String> findAllWords() {
        return parsedDataDAO.findAllWords();
    }

    public List<ParsedData> saveParsedRawData(Iterable<ParsedData> parsedRawData) {
        return parsedDataDAO.saveAll(parsedRawData);
    }

    public List<WordLevel> getWordLevels() {
        return getParsedRawData().stream().map(ParsedData::toWordLevel).collect(Collectors.toList());
    }
}
