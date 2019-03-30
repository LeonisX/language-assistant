package md.leonis.assistant.service;

import md.leonis.assistant.source.gse.dao.RawDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankService {

    private static final Logger log = LoggerFactory.getLogger(BankService.class);

    @Autowired
    private RawDAO rawDAO;

}
