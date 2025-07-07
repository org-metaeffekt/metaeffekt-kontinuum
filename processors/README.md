# {metaeffekt} Processors

## Purpose and Function

This folder contains a set of maven POM files (ending with .xml) and a set of markdown files (ending with .md) 
describing the purpose and details of the POM files. 

Each POM file addresses a particular task. The POM files basically are a blueprint for a task and can be parameterized
and executed. Please check the markdown files on the individual POMs for details.

The POM files are basically platform independent configurations that can be combined into a workflow. The workflow are
either defined by job-executor-specific actions/steps (GitHub, GitLab) or pipelines (i.e. Jenkins pipelines).

## Available Processors

### Util / General Purpose

* Merge Inventories [util_merge-inventories](util_merge-inventories.md)
* Transform Inventories [util_transform-inventories](util_transform-inventories.md)
* Validate Reference Inventory [util_validate-reference-inventory](util_validate-reference-inventory.md)
* Update Local Vulnerability Mirror [util_update-vulnerability-mirror](util_update-update-mirror.md)

### Analyze
* Resolve Artifacts in Inventory [analyze_resolve-inventory](analyze_resolve-inventory.md)

### Advise

* Attach Asset Metadata [advise_attach-metadata](advise_attach-metadata.md)
* Create Vulnerability Assessment Dashboard [badvise_create-dashboard](advise_create-dashboard.md)
* Enrich Inventory with Vulnerability/Advisory Data [advise_enrich-inventory](advise_enrich-inventory.md)
* Enrich Inventory with Curated Data [advise_enrich-reference](advise_enrich-reference.md)

### Portfolio Overview

* Aggregate Portfolio Resources [portfolio_copy-resources](portfolio_copy-resources.md)
* Create Portfolio Overview [portfolio_create-overview](portfolio_create-overview.md)

### Diff 

* Diff Vulnerability-enriched Inventories [diff_create-diff](diff_create-diff.md)
