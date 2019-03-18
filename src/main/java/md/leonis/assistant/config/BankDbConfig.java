package md.leonis.assistant.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "bankEntityManagerFactory",
        transactionManagerRef = "bankTransactionManager", basePackages = {"md.leonis.assistant.dao.bank"})
public class BankDbConfig {

    @Bean(name = "bankDataSource")
    @ConfigurationProperties(prefix = "bank.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "bankEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean
    entityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("bankDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("md.leonis.assistant.domain.bank")
                .persistenceUnit("bank")
                .build();
    }

    @Bean(name = "bankTransactionManager")
    public PlatformTransactionManager
    transactionManager(@Qualifier("bankEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    @ConfigurationProperties(prefix = "bank.datasource.liquibase")
    public LiquibaseProperties bankLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    public SpringLiquibase bankLiquibase() {
        return DefaultDbConfig.springLiquibase(dataSource(), bankLiquibaseProperties());
    }
}