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

### Table of Contents
* [Introduction](../README.md)
* [Getting Started](../GETTING_STARTED.md)
* [Tests](../tests/README.md)
* [Processors](../processors/README.md)
    * [advise_attach-metadata](../processors/advise/advise_attach-metadata.md)
    * [advise_create-dashboard](../processors/advise/advise_create-dashboard.md)
    * [advise_enrich-inventory](../processors/advise/advise_enrich-inventory.md)
    * [advise_enrich-with-reference](../processors/advise/advise_enrich-with-reference.md)
    * [analyze_resolve-inventory](../processors/analyze/analyze_resolve-inventory.md)
    * [convert_cyclonedx-to-inventory](../processors/convert/convert_cyclonedx-to-inventory.md)
    * [convert_inventory-to-cyclonedx](../processors/convert/convert_inventory-to-cyclonedx.md)
    * [convert_inventory-to-spdx](../processors/convert/convert_inventory-to-spdx.md)
    * [portfolio_copy-resources](../processors/portfolio/portfolio_copy-resources.md)
    * [portfolio_create-overview](../processors/portfolio/portfolio_create-overview.md)
    * [report_create-document](../processors/report/report_create-document.md)
    * [scan_scan-inventory](../processors/scan/scan_scan-inventory.md)
    * [util_aggregate-sources](../processors/util/util_aggregate-sources.md)
    * [util_copy-inventories](../processors/util/util_copy-inventories.md)
    * [util_create-diff](../processors/util/util_create-diff.md)
    * [util_merge-filter](../processors/util/util_merge-filter.md)
    * [util_merge-inventories](../processors/util/util_merge-inventories.md)
    * [util_portfolio-download](../processors/util/util_portfolio-download.md)
    * [util_portfolio-download-jars](../processors/util/util_portfolio-download-jars.md)
    * [util_portfolio-upload](../processors/util/util_portfolio-upload.md)
    * [util_transform-inventories](../processors/util/util_transform-inventories.md)
    * [util_update-mirror](../processors/util/util_update-mirror.md)
    * [util_validate-reference-inventory](../processors/util/util_validate-reference-inventory.md))