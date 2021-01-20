package org.repocrud.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * @author Denis B. Kulikov<br/>
 * date: 14.09.2018:8:54<br/>
 */
@Order(2)
@Configuration
public class DataSourceConfig {


    @Autowired
    Environment env;

//    @Autowired
//    private Server server;

    @Primary
    @Bean
    public DataSource customDataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("app.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("app.datasource.url"));
        dataSource.setUsername(env.getProperty("app.datasource.username"));
        dataSource.setPassword(env.getProperty("app.datasource.password"));

        return dataSource;

    }
}
