package com.metaeffekt.kontinuum.shell;


import com.metaeffekt.kontinuum.execution.MavenProcessorExecutor;
import com.metaeffekt.kontinuum.models.ConfigurationFileManager;
import com.metaeffekt.kontinuum.models.ProjectPropertiesLoader;
import com.metaeffekt.kontinuum.models.YamlProcessorCatalog;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;


@Configuration
public class ShellConfiguration {

    private static final String BANNER = """
            
            ╔══════════════════════════════════════════════════════════════╗
            ║                                                            ║
            ║   ███╗   ██╗███████╗ ██████╗ ███╗   ██╗                    ║
            ║   ████╗  ██║██╔════╝██╔═══██╗████╗  ██║                    ║
            ║   ██╔██╗ ██║█████╗  ██║   ██║██╔██╗ ██║                    ║
            ║   ██║╚██╗██║██╔══╝  ██║   ██║██║╚██╗██║                    ║
            ║   ██║ ╚████║███████╗╚██████╔╝██║ ╚████║                    ║
            ║   ╚═╝  ╚═══╝╚══════╝ ╚═════╝ ╚═╝  ╚═══╝                    ║
            ║                                                            ║
            ║   Software Bill of Materials & Vulnerability Assessment     ║
            ║                                                            ║
            ╚══════════════════════════════════════════════════════════════╝
            """;

    @Bean
    ProjectPropertiesLoader projectPropertiesLoader() {
        return new ProjectPropertiesLoader();
    }

    @Bean
    @DependsOn("projectPropertiesLoader")
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
            System.out.println(BANNER);
            System.out.println("  Type 'help' for a list of available commands");
            System.out.println("  Type 'processor list' to see available processors");
            System.out.println();
        };
    }
}
