package com.hog.bigdata;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.hog.bigdata.mapper")
@SpringBootApplication
public class DataImportApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataImportApplication.class, args);
    }

}
