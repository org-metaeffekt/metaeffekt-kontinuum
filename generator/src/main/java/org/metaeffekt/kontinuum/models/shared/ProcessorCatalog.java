package org.metaeffekt.kontinuum.models.shared;

import java.util.List;

public interface ProcessorCatalog {

    List<ProcessorDefinitions.MavenProcessor> getProcessors();

    ProcessorDefinitions.MavenProcessor getProcessorById(String processorId);

}
