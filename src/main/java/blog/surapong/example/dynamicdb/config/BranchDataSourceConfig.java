package blog.surapong.example.dynamicdb.config;

import blog.surapong.example.dynamicdb.repository.center.DatabaseInfoRepository;
import blog.surapong.example.dynamicdb.service.DataSourceRouting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;


@Configuration
@EnableJpaRepositories(
        basePackages = "blog.surapong.example.dynamicdb.repository.branch",
        entityManagerFactoryRef = "branchEntityManagerFactory",
        transactionManagerRef = "branchTransactionManager"
)
public class BranchDataSourceConfig {

    @Value("${spring.center.hibernate-dialect}")
    private String hibernateDialect;

    @Bean(name = "branchDataSource")
    public DataSource branchDataSource(@Autowired DatabaseInfoRepository databaseInfoRepository) {
        return new DataSourceRouting(databaseInfoRepository);
    }


    @Bean(name = "branchEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("branchDataSource") DataSource branchDataSource
    ) {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(branchDataSource);
        em.setPackagesToScan(new String[] { "blog.surapong.example.dynamicdb.entity.branch" });

        Properties properties = new Properties();
        properties.put("hibernate.dialect", hibernateDialect);

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(properties);

        return em;
    }

    @Bean(name = "branchTransactionManager")
    public PlatformTransactionManager centerTransactionManager(
            @Qualifier("branchEntityManagerFactory") LocalContainerEntityManagerFactoryBean branchEntityManagerFactory) {
        return new JpaTransactionManager(branchEntityManagerFactory.getObject());
    }

}
