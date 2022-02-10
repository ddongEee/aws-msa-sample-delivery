package com.aws.peach.infrastructure.configuration;

import com.aws.peach.infrastructure.dummy.Dummies;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackageClasses = {Dummies.class})
@EntityScan(basePackageClasses = {Dummies.class})
@EnableJpaRepositories(considerNestedRepositories = true, basePackageClasses = {Dummies.class})
public class DummyConfiguration {
}
