package com.rest.api.example;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * Created by vbarros on 16/09/2019 .
 */
@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
public class AppConfiguration{


}
