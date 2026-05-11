package org.metaeffekt.kontinuum.generator.gitlab;

import lombok.Data;

@Data
public class GeneratorConfig {

    private String pipelineConfigPath;
    private String containerImage;
    private int gitDepth = 1;
    private String gitStrategy = "clone";
    private String runnerTag;
    private String mountedVolume = "/shared";
    private String mavenCliOpts;
    private String setupCommand;

}
