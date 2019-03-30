package md.leonis.assistant.source.gse.config;

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
@EnableJpaRepositories(entityManagerFactoryRef = "gseEntityManagerFactory",
        transactionManagerRef = "gseTransactionManager", basePackages = {"md.leonis.assistant.source.gse.dao"})
public class GseDbConfig {

    @Bean(name = "gseDataSource")
    @ConfigurationProperties(prefix = "gse.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "gseEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean
    entityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("gseDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("md.leonis.assistant.source.gse.domain")
                .persistenceUnit("gse")
                .build();
    }

    @Bean(name = "gseTransactionManager")
    public PlatformTransactionManager
    transactionManager(@Qualifier("gseEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    @ConfigurationProperties(prefix = "gse.datasource.liquibase")
    public LiquibaseProperties gseLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    public SpringLiquibase gseLiquibase() {
        return TestDbConfig.springLiquibase(dataSource(), gseLiquibaseProperties());
    }
}