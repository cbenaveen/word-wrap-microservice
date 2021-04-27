package com.naveen.microservice.wordwrap.configurations;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hazelcast.repository.config.EnableHazelcastRepositories;

@Configuration
@EnableHazelcastRepositories(basePackages={"com.naveen.microservice.wordwrap.repository"})
public class ApplicationConfigurations {
    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}
