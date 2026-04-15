package com.metaeffekt.kontinuum.models;

import java.util.List;

public interface ProcessorCatalog {

    List<ProcessorDefinitions.Processor> getProcessors();

    ProcessorDefinitions.Processor getProcessorById(String processorId);

}
