package com.example.demo.dbUtil;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;


public class DBUtil {

    private JdbcTemplate jdbcTemplate;

    public DBUtil(){
        System.out.println(this+"__构造DBUtil");
        createJdbcTemplate();
    }

    private void createJdbcTemplate(){
        Properties properties=new Properties();
        try {
            properties.load(new InputStreamReader(DBUtil.class.getClassLoader().getResourceAsStream("application.properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }


        DriverManagerDataSource driverManagerDataSource=new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(properties.getProperty("spring.datasource.driver-class-name"));
        driverManagerDataSource.setUrl(properties.getProperty("spring.datasource.url"));
        driverManagerDataSource.setUsername(properties.getProperty("spring.datasource.name"));
        driverManagerDataSource.setPassword(properties.getProperty("spring.datasource.password"));

        this.jdbcTemplate=new JdbcTemplate(driverManagerDataSource);


    }




    public JdbcTemplate getJdbcTemplate(){
        return this.jdbcTemplate;
    }

}
