# {metaeffekt} Processors

## Purpose and Function

This folder contains a set of maven POM files (ending with .xml) and a set of markdown files (ending with .md) 
describing the purpose and details of the POM files. 

Each POM file addresses a particular task in the pipeline. The POM files serve as blueprint for a task and can be 
parameterized and executed. Please check the markdown files on the individual POMs for details.

![](../docs/concept-processor.png)

Each processor has user-controlled input, output and other params and environment settings. The user does not need a 
full understanding of the implementation of each processor. For detailed examples of how to control the processors and 
structure a project, please refer to the scripts at test/scripts/scripts-sh. The scripts show a basic execution of each 
processor with minimal parameterization, for optional parameters to further control each processor, please refer to the 
markdown documentation of each processor.

The POM files are platform independent configurations that can be combined into a workflow. The workflows are either 
defined by job-executor-specific actions/steps (GitHub, GitLab) or pipelines (i.e. Jenkins pipelines).

## Available Processors

### Util

* Aggregate Sources [util_aggregate-sources](util/util_aggregate-sources.md)
* Copy Inventories [util_copy-inventories](util/util_copy-inventories.md)
* Create Diff [util_create-diff](util/util_create-diff.md)
* Merge and Filter [util_merge-filter](util/util_merge-filter.md)
* Merge Inventories[util_merge-inventories](util/util_merge-inventories.md)
* Portfolio Download [util_portfolio-download](util/util_portfolio-download.md)
* Portfolio Upload [util_portfolio-upload](util/util_portfolio-upload.md)
* Transform Inventories [util_transform-inventories](util/util_transform-inventories.md)
* Update Mirror [util_update-mirror](util/util_update-mirror.md)
* Validate Reference Inventory [util_validate-reference-inventory](util/util_validate-reference-inventory.md)

### Analyze
* Resolve Artifacts in Inventory [analyze_resolve-inventory](analyze_resolve-inventory.md)

### Advise

* Attach Asset Metadata [advise_attach-metadata](advise_attach-metadata.md)
* Create Vulnerability Assessment Dashboard [badvise_create-dashboard](advise_create-dashboard.md)
* Enrich Inventory with Vulnerability/Advisory Data [advise_enrich-inventory](advise_enrich-inventory.md)
* Enrich Inventory with Curated Data [advise_enrich-reference](advise_enrich-reference.md)

### Convert

* Convert CycloneDX to inventory [convert_cyclonedx-to-inventory](convert/convert_cyclonedx-to-inventory.md)
* Convert Inventory to CycloneDX [convert_inventory-to-cyclonedx](convert/convert_inventory-to-cyclonedx.md)
* Convert Inventory to SPDX [convert_inventory-to-spdx](convert/convert_inventory-to-spdx.md)

### Portfolio Overview

* Aggregate Portfolio Resources [portfolio_copy-resources](portfolio_copy-resources.md)
* Create Portfolio Overview [portfolio_create-overview](portfolio_create-overview.md)

### Scan

* Scan Inventory [scan_scan-inventory](scan/scan_scan-inventory.md)

### Report

* Create Document [report_create-document](report/report_create-document.md)

### Diff 

* Diff Vulnerability-enriched Inventories [diff_create-diff](diff_create-diff.md)

## Processor Conventions

Each processor requires a series of parameters to be set to function correctly. The required and optional parameters
are grouped into three categories, input / output, parameters and environment.

### Input / Output

Input / output parameters usually describe files or directories which the processor requires to run.
These can be in the form of configuration files, inventories, SBOMs, property-files and so on.

Parameters in this category are prefixed with:
- input
- output

And suffixed with:
- file
- dir
- path

### Parameters
The "parameters" category simply describes any additional parameters which are needed for the processor to run or to
configure the processors flow and influence the output.

Parameters in this category are prefixed with:
- param

Suffixed with:
- enabled (for parameters which can be true or false)

### Environment
Environment parameters describe a series of prerequisites which are not necessarily specific to this single processor.
They usually describe directories or config files containing resources required by multiple processors which can be
shared project wide. Examples are the vulnerability database, maven mirror etc.

Parameters in this category are prefixed with:
- env

### Table of Contents
* [Introduction](README.md)
* [Getting Started](GETTING_STARTED.md)
* [Tests](tests/README.md)
* [Processors](processors/README.md)
    * [advise_attach-metadata](processors/advise/advise_attach-metadata.md)
    * [advise_create-dashboard](processors/advise/advise_create-dashboard.md)
    * [advise_enrich-inventory](processors/advise/advise_enrich-inventory.md)
    * [advise_enrich-with-reference](processors/advise/advise_enrich-with-reference.md)
    * [analyze_resolve-inventory](processors/analyze/analyze_resolve-inventory.md)
    * [convert_cyclonedx-to-inventory](processors/convert/convert_cyclonedx-to-inventory.md)
    * [convert_inventory-to-cyclonedx](processors/convert/convert_inventory-to-cyclonedx.md)
    * [convert_inventory-to-spdx](processors/convert/convert_inventory-to-spdx.md)
    * [portfolio_copy-resources](processors/portfolio/portfolio_copy-resources.md)
    * [portfolio_create-overview](processors/portfolio/portfolio_create-overview.md)
    * [report_create-document](processors/report/report_create-document.md)
    * [scan_scan-inventory](processors/scan/scan_scan-inventory.md)
    * [util_aggregate-sources](processors/util/util_aggregate-sources.md)
    * [util_copy-inventories](processors/util/util_copy-inventories.md)
    * [util_create-diff](processors/util/util_create-diff.md)
    * [util_merge-filter](processors/util/util_merge-filter.md)
    * [util_merge-inventories](processors/util/util_merge-inventories.md)
    * [util_portfolio-download](processors/util/util_portfolio-download.md)
    * [util_portfolio-download-jars](processors/util/util_portfolio-download-jars.md)
    * [util_portfolio-upload](processors/util/util_portfolio-upload.md)
    * [util_transform-inventories](processors/util/util_transform-inventories.md)
    * [util_update-mirror](processors/util/util_update-mirror.md)
    * [util_validate-reference-inventory](processors/util/util_validate-reference-inventory.md))