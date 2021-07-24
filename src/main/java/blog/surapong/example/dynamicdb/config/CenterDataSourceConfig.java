package blog.surapong.example.dynamicdb.config;

import com.sun.istack.NotNull;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
        basePackages = "blog.surapong.example.dynamicdb.repository.center",
        entityManagerFactoryRef = "centerEntityManagerFactory",
        transactionManagerRef = "centerTransactionManager"
)
public class CenterDataSourceConfig {

    @Value("${spring.center.hibernate-dialect}")
    private String hibernateDialect;

    @Bean(name = "centerDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.center.datasource")
    public DataSourceProperties centerDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "centerDataSource")
    public DataSource dataSource(
            @Qualifier("centerDataSourceProperties") DataSourceProperties dataSourceProperties
    ) {
        return centerDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean(name = "centerEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("centerDataSource") DataSource dataSource
    ) {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource( dataSource );
        em.setPackagesToScan(new String[] { "blog.surapong.example.dynamicdb.entity.center" });
        Properties properties = new Properties();

        properties.put("hibernate.dialect", hibernateDialect);

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(properties);

        return em;
    }

    @Bean(name = "centerTransactionManager")
    public PlatformTransactionManager centerTransactionManager(
            @Qualifier("centerEntityManagerFactory") LocalContainerEntityManagerFactoryBean centerEntityManagerFactory) {
        return new JpaTransactionManager(centerEntityManagerFactory.getObject());
    }
}
