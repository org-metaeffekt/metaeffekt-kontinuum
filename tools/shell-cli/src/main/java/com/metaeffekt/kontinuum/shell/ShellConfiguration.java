package com.metaeffekt.kontinuum.shell;

import com.metaeffekt.kontinuum.execution.container.ContainerCommandClient;
import com.metaeffekt.kontinuum.execution.container.ContainerWrapperBackend;
import com.metaeffekt.kontinuum.execution.contract.ExecuteProcessorResult;
import com.metaeffekt.kontinuum.execution.core.GuidedProcessorExecutor;
import com.metaeffekt.kontinuum.execution.core.MavenCommandPlanner;
import com.metaeffekt.kontinuum.execution.core.ProcessorCatalog;
import com.metaeffekt.kontinuum.execution.core.RequiredPropertyValidator;
import com.metaeffekt.kontinuum.execution.core.YamlProcessorCatalog;
import com.metaeffekt.kontinuum.execution.local.LocalMavenBackend;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Configuration
public class ShellConfiguration {

    @Bean
    ProcessorCatalog processorCatalog() {
        return new YamlProcessorCatalog(Path.of("../../processors/processors.yaml"));
    }

    @Bean
    GuidedProcessorExecutor guidedProcessorExecutor(ProcessorCatalog processorCatalog) {
        return new GuidedProcessorExecutor(processorCatalog, new RequiredPropertyValidator(), new MavenCommandPlanner());
    }

    @Bean
    LocalMavenBackend localMavenBackend() {
        return new LocalMavenBackend();
    }

    @Bean
    ContainerWrapperBackend containerWrapperBackend(ContainerCommandClient containerCommandClient) {
        return new ContainerWrapperBackend(containerCommandClient);
    }

    @Bean
    ContainerCommandClient containerCommandClient() {
        return plan -> new ExecuteProcessorResult(
            2,
            String.join(" ", plan.command()),
            List.of("Container backend is not connected yet."),
            Map.of("reason", "No ContainerCommandClient adapter configured")
        );
    }
}
