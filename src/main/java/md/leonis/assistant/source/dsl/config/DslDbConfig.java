package md.leonis.assistant.source.dsl.config;

import liquibase.integration.spring.SpringLiquibase;
import md.leonis.assistant.config.TestDbConfig;
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
@EnableJpaRepositories(entityManagerFactoryRef = "dslEntityManagerFactory",
        transactionManagerRef = "dslTransactionManager", basePackages = {"md.leonis.assistant.source.dsl.dao"})
public class DslDbConfig {

    @Bean(name = "dslDataSource")
    @ConfigurationProperties(prefix = "dsl.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dslEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean
    entityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("dslDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("md.leonis.assistant.source.dsl.domain")
                .persistenceUnit("dsl")
                .build();
    }

    @Bean(name = "dslTransactionManager")
    public PlatformTransactionManager
    transactionManager(@Qualifier("dslEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    @ConfigurationProperties(prefix = "dsl.datasource.liquibase")
    public LiquibaseProperties dslLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    public SpringLiquibase dslLiquibase() {
        return TestDbConfig.springLiquibase(dataSource(), dslLiquibaseProperties());
    }
}