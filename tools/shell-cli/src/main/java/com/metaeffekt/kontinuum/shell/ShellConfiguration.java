package com.metaeffekt.kontinuum.shell;


import com.metaeffekt.kontinuum.execution.MavenProcessorExecutor;
import com.metaeffekt.kontinuum.models.YamlProcessorCatalog;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ShellConfiguration {

    @Bean
    YamlProcessorCatalog processorCatalog() { return new YamlProcessorCatalog(); }

    @Bean
    MavenProcessorExecutor mavenProcessorExecutor() {
        return new MavenProcessorExecutor();
    }
}
