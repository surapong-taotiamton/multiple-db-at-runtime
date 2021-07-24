package blog.surapong.example.dynamicdb.service;

import blog.surapong.example.dynamicdb.entity.center.DatabaseInfo;
import blog.surapong.example.dynamicdb.repository.center.DatabaseInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.ConnectionBuilder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataSourceRouting extends AbstractRoutingDataSource {
    
    
    private static final Logger logger = LoggerFactory.getLogger(DataSourceRouting.class);

    private DatabaseInfoRepository databaseInfoRepository;

    private Map<Object, Object> mapDataSource;

    public DataSourceRouting(DatabaseInfoRepository databaseInfoRepository) {
        this.databaseInfoRepository = databaseInfoRepository;
        this.mapDataSource = new HashMap<>();

        DatabaseInfo firstDatabaseInfo = databaseInfoRepository.findAll(PageRequest.of(0, 1)).get().findFirst().orElseThrow();
        DataSource firstDataSource = createDataSource(firstDatabaseInfo.getDatabaseInfoId());

        this.mapDataSource.put(firstDatabaseInfo.getDatabaseInfoId(), firstDataSource);
        this.setTargetDataSources(mapDataSource);
    }

    @Override
    protected Object determineCurrentLookupKey() {

        if (BranchDataSourceContext.getCurrentBranchDataSourceId() == null) {
            BranchDataSourceContext.setCurrentBranchDataSourceId(
                    this.mapDataSource.entrySet().stream().findFirst().orElseThrow().getKey().toString());
        }

        logger.info("datasource : {}", BranchDataSourceContext.getCurrentBranchDataSourceId());
        if (this.mapDataSource.get(BranchDataSourceContext.getCurrentBranchDataSourceId()) == null) {
            logger.info("Case create new datasource : {}", BranchDataSourceContext.getCurrentBranchDataSourceId());
            DataSource dataSource = createDataSource(BranchDataSourceContext.getCurrentBranchDataSourceId());
            this.mapDataSource.put(BranchDataSourceContext.getCurrentBranchDataSourceId(), dataSource);
            this.afterPropertiesSet();
        }
        return BranchDataSourceContext.getCurrentBranchDataSourceId();
    }


    public DataSource createDataSource(String databaseInfoId) {

        DatabaseInfo databaseInfo = databaseInfoRepository.findById(databaseInfoId)
                .orElseThrow();

        String dbUrl = String.format("jdbc:mariadb://%s/%s?useUnicode=yes&characterEncoding=UTF-8&allowPublicKeyRetrieval=true&&serverTimezone=Asia/Bangkok",
                databaseInfo.getHost(), databaseInfo.getName());

        logger.info("dbUrl : {}", dbUrl);


        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        dataSource.setUsername(databaseInfo.getUsername());
        dataSource.setPassword(databaseInfo.getPassword());
        dataSource.setUrl(dbUrl);
        return dataSource;
    }
}
