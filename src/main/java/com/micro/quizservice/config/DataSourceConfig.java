package com.micro.quizservice.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
@MapperScan("com.micro.quizservice.mappers")

public class DataSourceConfig {
    private static final Logger LOGGER = LogManager.getLogger(DataSourceConfig.class.getName());

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory2() {
        SqlSessionFactory sessionFactory = null;
        try {
            SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
            bean.setDataSource(primaryDataSource());
            bean.setTypeAliasesPackage("com.micro.quizservice.mappers");
            bean.setMapperLocations(
                    new PathMatchingResourcePatternResolver().getResources("classpath*:mappers/Quiz.xml"));
            sessionFactory = bean.getObject();

        } catch (Exception e) {
            LOGGER.error("Exception " + e.getCause());
        }
        return sessionFactory;
    }

    @Bean
    SqlSessionTemplate sqlSessionTemplate2() {
        return new SqlSessionTemplate(sqlSessionFactory2());
    }

}