package com.aws.peach.infrastructure.configuration.api;

import com.aws.peach.infrastructure.configuration.DummyConfiguration;
import com.aws.peach.infrastructure.configuration.MessageProducerConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
        MessageProducerConfiguration.class,
        DummyConfiguration.class
})
public class PeachApiInfrastructureContextConfig {
}
