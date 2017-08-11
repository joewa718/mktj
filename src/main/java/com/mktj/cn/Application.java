package com.mktj.cn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


//@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = {"com.mktj.cn.*"})
public class Application extends SpringBootServletInitializer {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        logger.error("msg");
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

}