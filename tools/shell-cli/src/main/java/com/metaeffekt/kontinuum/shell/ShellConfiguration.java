package com.metaeffekt.kontinuum.shell;


import com.metaeffekt.kontinuum.execution.MavenProcessorExecutor;
import com.metaeffekt.kontinuum.models.ConfigurationFileManager;
import com.metaeffekt.kontinuum.models.ProjectPropertiesLoader;
import com.metaeffekt.kontinuum.models.YamlProcessorCatalog;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ShellConfiguration {

    @Bean
    ProjectPropertiesLoader projectPropertiesLoader() {
        return new ProjectPropertiesLoader();
    }

    @Bean
    YamlProcessorCatalog processorCatalog() { return new YamlProcessorCatalog(); }

    @Bean
    MavenProcessorExecutor mavenProcessorExecutor() {
        return new MavenProcessorExecutor();
    }

    @Bean
    ConfigurationFileManager configurationFileManager() {
        return new ConfigurationFileManager();
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
            System.out.println("      --processor-id  - The processor ID or index (required for interactive mode)");
            System.out.println("      --dry-run       - Enable dry-run mode (default: false)");
            System.out.println("      --debug         - Enable Maven debug flag -X (default: false)");
            System.out.println("      --configuration - Path to a saved configuration file");
            System.out.println();
        };
    }
}
