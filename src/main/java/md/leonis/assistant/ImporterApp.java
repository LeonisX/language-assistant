package md.leonis.assistant;

import md.leonis.assistant.dao.PersonDAO;
import md.leonis.assistant.domain.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class ImporterApp {

    private static final Logger log = LoggerFactory.getLogger(ImporterApp.class);

    //TODO unused
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    private static ConfigurableApplicationContext springContext;

    public static void main(String[] args) throws ParseException {
        springContext = SpringApplication.run(ImporterApp.class, args);


        log.info("Importing");

        PersonDAO personDAO = springContext.getBean(PersonDAO.class);

        long count = personDAO.count();

        if (count == 0) {
            Person p1 = new Person();

            p1.setFullName("John");

            Date d1 = df.parse("1980-12-20");
            p1.setDateOfBirth(d1);
            //
            Person p2 = new Person();

            p2.setFullName("Smith");
            Date d2 = df.parse("1985-11-11");
            p2.setDateOfBirth(d2);

            personDAO.save(p1);
            personDAO.save(p2);
        }

        Iterable<Person> all = personDAO.findAll();

        StringBuilder sb = new StringBuilder();

        all.forEach(p -> sb.append(p.getFullName()).append("<br>"));

        System.out.println(sb.toString());

    }
}
