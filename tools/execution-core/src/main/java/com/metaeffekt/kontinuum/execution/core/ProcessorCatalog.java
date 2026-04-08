package com.metaeffekt.kontinuum.execution.core;

import com.metaeffekt.kontinuum.execution.contract.ProcessorDefinition;

import java.util.List;
import java.util.Optional;

public interface ProcessorCatalog {

    List<ProcessorDefinition> list();

    Optional<ProcessorDefinition> findById(String processorId);
}
