package com.metaeffekt.kontinuum.shell;


import com.metaeffekt.kontinuum.execution.MavenProcessorExecutor;
import com.metaeffekt.kontinuum.models.YamlProcessorCatalog;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.jline.PromptProvider;


@Configuration
public class ShellConfiguration {

    @Bean
    YamlProcessorCatalog processorCatalog() { return new YamlProcessorCatalog(); }

    @Bean
    MavenProcessorExecutor mavenProcessorExecutor() {
        return new MavenProcessorExecutor();
    }

    @Bean
    CommandLineRunner clearScreenAndPrintHelp() {
        return args -> {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("Kontinuum Shell CLI");
            System.out.println("===================");
            System.out.println();
            System.out.println("Available commands:");
            System.out.println("  processor list    - List all known processors");
            System.out.println("  processor show    - Show required and optional parameters for one processor");
            System.out.println("  processor run     - Execute a processor in local or container mode");
            System.out.println("    Options:");
            System.out.println("      --processor-id  - The processor ID or index (required)");
            System.out.println("      --dry-run       - Enable dry-run mode (default: false)");
            System.out.println("      --debug         - Enable Maven debug flag -X (default: false)");
            System.out.println();
        };
    }
}
