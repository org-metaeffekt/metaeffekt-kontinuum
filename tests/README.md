# Tests

This directory contains all components required to execute and test the processors available in this repository. 
The test infrastructure is organized into three main categories: resources, scripts, and results. 
Scripts are further divided into cases, processor-specific scripts, and pipelines.

## Scripts

The script infrastructure forms the core testing framework and is designed for easy extensibility. 
It consists of three sections:

### Cases

Case scripts define parameter sets for specific processors. These scripts provide the necessary configuration parameters 
to processor-specific scripts, separating configuration from execution logic. This ensures that processor-specific 
scripts remain unchanged when input/output requirements change as only the corresponding cases have to be adjusted.

### Processor-Specific Scripts

Each processor-specific script corresponds to a single processor and handles its execution. These scripts manage all 
prerequisites beyond basic parameters, including:

- Service initialization
- Cleanup operations
- Crafting Maven commands
- Executing the underlying processor

Every processor-specific script requires at least one case script to provide the necessary parameters for execution.

### Pipelines

Pipeline scripts execute multiple processor-specific scripts in a predetermined sequence with their respective cases. 
Pipelines provide a straightforward method for running variable numbers of processors in a specific order.

## Resources

The resources in this directory support processor execution by providing necessary assets such as:
- Configuration files
- Security policies
- Asset descriptors
- Other required dependencies

These resources are considered essential for running the processors in this repository but should not be seen as a 
reference for each respective resource. For more information about specific resources, please refer 
to the [metaeffekt-workbench](https://github.com/org-metaeffekt/metaeffekt-workbench) project.

