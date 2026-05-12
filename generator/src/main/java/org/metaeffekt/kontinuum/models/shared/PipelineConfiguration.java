package org.metaeffekt.kontinuum.models.shared;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Data
public class PipelineConfiguration {

    ProjectProperties projectProperties;
    List<Report> reports;
    List<Dashboard> dashboards;
    Options options;

    @Data
    public static class ProjectProperties {

        Project project;
        List<Asset> assets;

        @Data
        public static class Project {
            String name;
            String version;
            String tenant;

            @Override
            public String toString() {
                StringBuilder sb = new StringBuilder();
                if (StringUtils.isNotBlank(name)) {
                    sb.append(name);
                } else {
                    sb.append("unnamed-project");
                }

                if (StringUtils.isNotBlank(version)) {
                    sb.append("-").append(version);
                }
                return sb.toString();
            }
        }

        @Data
        public static class Asset {
            String id;
            String name;
            String version;
            String assessmentId;
            String reference;
            UrlResolver urlResolver;
            MavenResolver mavenResolver;
            ContainerResolver containerResolver;

            @Data
            public static class UrlResolver {
                String url;
                String urlPattern;
            }

            @Data
            public static class MavenResolver {
                String groupId;
                String artifactId;
                String artifactVersion;
            }

            @Data
            public static class ContainerResolver {
                String image;
                String tag;
            }

            public String getReferenceDir() throws IllegalStateException{
                if (StringUtils.isBlank(reference)) {
                    throw new IllegalStateException("Tried to access reference inventory for asset " + this + " but is not set.");
                }

                if (Files.isDirectory(Path.of(reference))) {
                    return reference;
                } else {
                    File referenceFile = new File(reference);
                    return referenceFile.getParentFile().getPath();
                }
            }

            @Override
            public String toString() {
                StringBuilder sb = new StringBuilder();
                if (StringUtils.isNotBlank(name)) {
                    sb.append(name);
                } else {
                    sb.append("unnamed-asset");
                }

                if (StringUtils.isNotBlank(version)) {
                    sb.append("-").append(version);
                }
                return sb.toString();
            }
        }
    }

    @Data
    public static class Report {
        List<String> assets;
        String type;
    }

    @Data
    public static class Dashboard {
        String id;
        List<String> assets;
    }

    @Data
    public static class Options {

        EnrichmentOptions enrichment = new EnrichmentOptions();
        GlobalOptions global = new GlobalOptions();

        @Data
        public static class EnrichmentOptions{
            String securityPolicyFile;
            List<String> securityPolicyActiveIds = new ArrayList<>();
            Boolean activateMsrc = true;
            Boolean activateNvd = true;
            Boolean activateCertFr = true;
            Boolean activateCertEu = true;
            Boolean activateCertSei = true;
            Boolean activateOsv = true;
            Boolean activateKev = true;
            Boolean activateEpss = true;
            Boolean activateEol = true;
            Boolean activateCsaf = true;
        }

        @Data
        public static class GlobalOptions{
            String documentLanguage = "en";
            Boolean enableResolve = false;
            Boolean enableSpdxBom = false;
            Boolean enableCycloneDxBom = false;
        }
    }

}
