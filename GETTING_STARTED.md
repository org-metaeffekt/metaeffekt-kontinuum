# Getting Started

Before proceeding, please review the top-level [README](README.md) to understand this repository's purpose and intended 
use cases.

## Repository Structure

This repository is organized into two main sections: **processors** and **tests**.

The **processors** section contains XML files that execute predefined parameterized steps. Each processor 
represents a specific isolated workflow or task. For detailed information about each processor's capabilities, usage 
instructions, and expected results, refer to the processor-specific README files.

The **tests** section provides basic test infrastructure for:
- Testing individual processors
- Testing processor sequences through pipeline scripts
- Demonstrating how to call each processor with available parameters
- Showing how to connect processors into custom pipelines

## Prerequisites

To ensure all reference processes and pipelines in this repository can run, we need to create an external.rc file
in the root of this repository. A template for this file with additional hints and details has been provided here: 
[external-template.rc](external-template.rc).

Depending on which processors will be run, an instance of the [metaeffekt-workbech](https://github.com/org-metaeffekt/metaeffekt-workbench)
might be required and checked out locally. The only additional requirement is a local instance of the vulnerability mirror
which can be generated via the [mirror_update-index.xml](processors/mirror/mirror_update-index.xml) processor.

## Running the Reference Pipeline

To get started with executing processors, you can either:

1. Run the [complete.sh](tests/scripts/pipelines/001_complete.sh) pipeline script (produces all available results but may take several minutes)
2. Run any other pipeline script from the [`tests/scripts/pipelines`](tests/scripts/pipelines) directory

**Note:** Scripts can be executed from any directory.

### Running Without a Vulnerability Mirror

Since only a couple processors currently require the vulnerability mirror, you can omit these processors from your pipeline:

1. Copy the [complete.sh](tests/scripts/pipelines/001_complete.sh) script
2. Remove the following entries from the pipeline:
   ```bash
   bash "$PROCESSOR_SCRIPTS_DIR/mirror/mirror_download-index.sh" -c "$CASES_DIR/mirror/mirror_download-index-01.sh" -f "$LOG_FILE"
   bash "$PROCESSOR_SCRIPTS_DIR/resolve/resolve_resolve-inventory.sh" -c "$CASES_DIR/resolve/resolve_resolve-inventory-01.sh" -f "$LOG_FILE"
   bash "$PROCESSOR_SCRIPTS_DIR/advise/advise_enrich-inventory.sh" -c "$CASES_DIR/advise/advise_enrich-inventory-01.sh" -f "$LOG_FILE"
   bash "$PROCESSOR_SCRIPTS_DIR/advise/advise_create-dashboard.sh" -c "$CASES_DIR/advise/advise_create-dashboard-01.sh" -f "$LOG_FILE"
   ```
3. Run the newly created pipeline script


## Running a Single Processor

To execute an individual processor, run the corresponding script in the [`processors`](tests/scripts/processors) directory. Note that some 
processors may require a vulnerability mirror instance.

All processor results are stored in the [`target`](tests/target) directory.

## Creating Custom Implementations

While this repository is not designed as a custom workbench for executing processors with arbitrary resources and 
parameters, it is technically possible. To create a custom script or processor execution:

1. Create a new "case script" defining all necessary parameters
2. Call the processor script with your case as a parameter:
   ```bash
   sh your-processor.sh -c /path/to/your/case.sh
   ```

This repository is mainly meant to be used in conjunction with the [metaeffekt-workbech](https://github.com/org-metaeffekt/metaeffekt-workbench)
or via CI/CD components like the [metaeffekt-components](https://gitlab.opencode.de/metaeffekt/metaeffekt-components).